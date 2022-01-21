package com.example.openlog.view

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.openlog.R
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.example.openlog.databinding.FragmentDisplayGraphBinding
import com.example.openlog.model.DataDTO
import com.example.openlog.model.LineData
import com.example.openlog.viewModel.DataViewModel
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.SecondScale
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DisplayGraph : Fragment() {

    private val dataViewModel: DataViewModel by activityViewModels()
    private lateinit var binding: FragmentDisplayGraphBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }
    private var lineData = ArrayList<LineData?>()
    private var userInputList = arrayOfNulls<ArrayList<DataDTO>>(3)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        //Obtains graph from layout
        binding = FragmentDisplayGraphBinding.inflate(inflater, container, false)
        userInputList = dataViewModel.getInputList()

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val graph1 = binding.graphOne
        val graph2 = binding.graphTwo

        dataViewModel.setGraphs(graph1, graph2)
        generateGraph()
    }


    // Sets the format of the label on the x-axis example "hh:mm:ss"
    private fun setLabelFormat(graph: GraphView, pattern: String) {
        graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {

            @SuppressLint("SimpleDateFormat")
            val sdf = SimpleDateFormat(pattern)

            //slightly modified code from
            //http://www.java2s.com/Open-Source/Android_Free_Code/Graphics/graph/com_jjoe64_graphview_helperDateAsXAxisLabelFormatter_java.htm
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                if (isValueX) {
                    return sdf.format(Date(value.toLong()))
                } else {
                    return super.formatLabel(value, isValueX)
                }
            }
        }
    }
    // returns timeformat for x-axis labels
    private fun getPattern(): String {
        val dates = getLabelFormatRange()
        val startDate = dates[0]
        val endDate = dates[1]
        var pattern = ""
        val msInDay = 86400000
        val buffer = msInDay/100

        when {
            startDate.year != endDate.year -> {
                pattern = "yy:MM"
                if (endDate.time - startDate.time < (msInDay * 24 + buffer)) {
                    pattern = addToPattern(pattern, "dd")
                }
            }
            startDate.month != endDate.month -> pattern = "MM:dd"
            startDate.date != endDate.date -> pattern = "dd:HH"
        }
        if (pattern != "dd:HH") {
            if (endDate.time - startDate.time < (msInDay * 3 + buffer))
                pattern = addToPattern(pattern, "HH")
            if (endDate.time - startDate.time < (msInDay + buffer))
                pattern = addToPattern(pattern, "mm")
        }
        return pattern
    }

    private fun addToPattern(pattern: String, addition: String): String {
        if (pattern.isNotEmpty())
        return "$pattern:$addition"
        else return addition
    }

    // returns timeunit such that humans can understand timefomat on x-axis
    private fun getTimeUnit(): String {
        val pattern = getPattern()
        var timeUnit = ""

        if (pattern.contains("y"))
            timeUnit = addToTimeUnit(timeUnit, "år")
        if (pattern.contains("M"))
            timeUnit = addToTimeUnit(timeUnit, "måned")
        if (pattern.contains("d"))
            timeUnit = addToTimeUnit(timeUnit, "dag")
        if (pattern.contains("H"))
            timeUnit = addToTimeUnit(timeUnit, "time")
        if (pattern.contains("m"))
            timeUnit = addToTimeUnit(timeUnit, "minut")
        return timeUnit
    }

    private fun addToTimeUnit(timeUnit: String, addition: String): String {
        if (timeUnit.isNotEmpty())
            return "$timeUnit/$addition"
        else return addition
    }

    // returns earliest and latest date of logged values in dataset
    private fun getLabelFormatRange(): Array<Date> {
        var startDate = Date(250, 1, 1, 1, 1, 1)
        var endDate = Date(1, 1, 1, 1, 1, 1)

        for (list in userInputList) {
            if (!list.isNullOrEmpty()) {
                if (list[0].getInputTwoAsDate().before(startDate)) {
                    startDate = list[0].getInputTwoAsDate()
                }

                if (list[list.size - 1].getInputTwoAsDate().after(endDate)) {
                    endDate = list[list.size - 1].getInputTwoAsDate()
                }
            }
        }
        return arrayOf(startDate, endDate)
    }

    // generates graph with certain options
    private fun generateGraph() {
        val lineGraphSeriesList = loadData()
        val graphOne = binding.graphOne

        for ((i, line) in lineGraphSeriesList.withIndex()) {
            setLineSettings(line, i)
            when (i) {
                0 -> {
                    setGraphSettings(graphOne)
                    graphOne.addSeries(line)
                    lineData[i]?.let { graphOne.gridLabelRenderer.verticalLabelsColor = it.color }
                }
                1 -> {
                    val canvas = Canvas()
                    line.draw(graphOne, canvas, true)
                    setSecondScaleYAxis(line, graphOne.secondScale)
                    graphOne.secondScale.addSeries(line)
                    lineData[i]?.let { graphOne.gridLabelRenderer.verticalLabelsSecondScaleColor = it.color }
                }
                2 -> {
                    val graphTwo = binding.graphTwo
                    setGraphSettings(graphTwo)
                    graphTwo.addSeries(line)
                    setLabelFormat(binding.graphTwo, getPattern())

                    lineData[i]?.let { graphTwo.gridLabelRenderer.verticalLabelsColor = it.color }
                }
            }
        }

        setXAxisRange(getXAxisRange(lineGraphSeriesList))
    }

    // settings for line representing data
    private fun setLineSettings(line: LineGraphSeries<DataPoint>, index: Int) {
        line.color = lineData[index]?.color!!
        line.title = lineData[index]?.title!!
        line.isDrawDataPoints = true //Shows datapoints as circles in the curve
        line.dataPointsRadius = 16F //layout for datapoints
        line.thickness = 8 //Layout for datapoints
    }

    // sets various setting for the graph
    private fun setGraphSettings(graph: GraphView) {
        graph.visibility = View.VISIBLE
        setLabelFormat(graph, getPattern())

        val legendRenderer = graph.legendRenderer
        legendRenderer.isVisible = true
        legendRenderer.align = com.jjoe64.graphview.LegendRenderer.LegendAlign.TOP
        legendRenderer.textSize
        legendRenderer.backgroundColor = Color.WHITE

        //Axis titles
        val gridLabel = graph.gridLabelRenderer as GridLabelRenderer
        gridLabel.labelsSpace = -20
        gridLabel.horizontalAxisTitle = getTimeUnit()
        gridLabel.horizontalAxisTitleTextSize = 40F
        gridLabel.labelHorizontalHeight = 95

        // this will draw a border aroung graph and will also show both axis
        graph.viewport.setDrawBorder(true)
        graph.gridLabelRenderer.setHumanRounding(false, true)
        graph.gridLabelRenderer.isHighlightZeroLines = false
    }


    private fun setSecondScaleYAxis(line: LineGraphSeries<DataPoint>, secondScale: SecondScale) {
        secondScale.setMinY(line.lowestValueY)
        secondScale.setMaxY(line.highestValueY)
    }

    // manually sets range of the graph along the x-axis
    private fun getXAxisRange(lineGraphSeriesList: ArrayList<LineGraphSeries<DataPoint>>): Array<Double>  {
        var greatest = Double.MIN_VALUE
        var smallest = Double.MAX_VALUE

        for (list in lineGraphSeriesList) {
            if (list.highestValueX > greatest) {
                greatest = list.highestValueX
            }
            if (list.lowestValueX < smallest) {
                smallest = list.lowestValueX
            }
        }
        //padding between line start/end and y-axis
        greatest += (greatest-smallest)/16
        smallest += -(greatest-smallest)/16
        if (lineGraphSeriesList.isEmpty()) {
            return arrayOf(0.0,1.0)
        }
        return arrayOf(smallest, greatest)
    }


    private fun setXAxisRange(range: Array<Double>) {
        val graphOne = binding.graphOne
        val graphTwo = binding.graphTwo

        graphOne.viewport.setMinX(range[0])
        graphOne.viewport.setMaxX(range[1])
        graphOne.viewport.isXAxisBoundsManual = true

        graphTwo.viewport.setMinX(range[0])
        graphTwo.viewport.setMaxX(range[1])
        graphTwo.viewport.isXAxisBoundsManual = true
    }

    // loads all data selected by user into arraylist of linegraphseries to be used by graphview
    private fun loadData(): ArrayList<LineGraphSeries<DataPoint>> {
        val dataList = userInputList
        val dataPoints = arrayOfNulls<Array<DataPoint?>>(dataList.size)
        val lineData = dataViewModel.getLineData()

        val lineGraphSeriesList = ArrayList<LineGraphSeries<DataPoint>>(0)

        for ((i, list) in dataList.withIndex()) {
            if (list != null && list.isNotEmpty()) {
                dataPoints[i] = Array(list.size) { DataPoint(0.0,0.0) }
                for ((j, inputDTO) in list.withIndex()) {
                    dataPoints[i]?.set(j,
                        (DataPoint(inputDTO.getInputTwoAsDate(), inputDTO.firstInput.toDouble()))
                    )
                }
                this.lineData.add(lineData[i])
                lineGraphSeriesList.add(LineGraphSeries(dataPoints[i]))
            }
        }
        return lineGraphSeriesList
    }
    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater){
        menuInflater.inflate(R.menu.nav_menu_displaygraph,menu)
    }

    //dropdown menu navigation
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.delingDropdown -> onDelDropdown()
        }

        return super.onOptionsItemSelected(item)
    }

    //shares the shown graphs
    fun onDelDropdown() {
        if (activity?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            } == PackageManager.PERMISSION_DENIED) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            }
        }

        if (activity?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)} == PackageManager.PERMISSION_GRANTED) {
            val graph_one = dataViewModel.getGraphOne()
            graph_one?.setBackgroundColor(resources.getColor(R.color.white))
            val graph_two = dataViewModel.getGraphTwo()
            graph_two?.setBackgroundColor(resources.getColor(R.color.white))

            var bitmap: Bitmap? = null

            if (graph_one?.isVisible == true && graph_two?.isVisible == true){
                bitmap = graph_one.takeSnapshot()?.let { graph_two.takeSnapshot()?.let { it1 -> combineImages(it, it1) } }
            }

            else if( graph_one?.isVisible == true){
                bitmap = graph_one.takeSnapshot()
            }

            if (bitmap == null){
                Toast.makeText(context, "Der er intet at dele", Toast.LENGTH_SHORT).show()
            }

            if (bitmap != null){
                shareImage(bitmap)
            }
        }
    }


    //not made by us!! taken from graphview.takeSnapshotAndShare, and modified: https://github.com/jjoe64/GraphView/blob/master/src/main/java/com/jjoe64/graphview/GraphView.java
    fun shareImage(bitmap: Bitmap){
        val path: String = MediaStore.Images.Media.insertImage(context!!.contentResolver, bitmap, "Graph snapshot", null) ?: // most likely a security problem
        throw SecurityException("Could not get path from MediaStore. Please check permissions.")
        val i = Intent(Intent.ACTION_SEND)
        i.type = "image/*"
        i.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
        try {
            context!!.startActivity(Intent.createChooser(i, "Del!"))
        } catch (ex: ActivityNotFoundException) {
            ex.printStackTrace()
        }
    }


    //not made by us. taken from https://stackoverflow.com/questions/4863518/combining-two-bitmap-image-side-by-side/4863551
    fun combineImages(
        c: Bitmap,
        s: Bitmap
    ): Bitmap? {
        var cs: Bitmap? = null
        val width: Int
        var height = 0
        if (c.width > s.width) {
            width = c.width + s.width
            height = c.height
        } else {
            width = s.width + s.width
            height = c.height
        }
        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val comboImage = Canvas(cs)
        comboImage.drawBitmap(c, 0f, 0f, null)
        comboImage.drawBitmap(s, c.width.toFloat(), 0f, null)

        return cs
    }
}