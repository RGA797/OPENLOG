package com.example.openlog.view

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val graph = binding.graphOne

        dataViewModel.setGraph(graph)
        setOptions()
        binding.timeUnit.text = getTimeUnit()
    }

    // returns the timeunit of x-axis
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
        return timeUnit
    }

    private fun addToTimeUnit(timeUnit: String, addition: String): String {
        if (timeUnit.isNotEmpty())
            return "$timeUnit/$addition"
        else return addition
    }

    // Sets the format of the label on the x-axis example "hh:mm:ss"
    private fun labelFormat(graph: GraphView, pattern: String) {
        graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {

            @SuppressLint("SimpleDateFormat")
            val sdf = SimpleDateFormat(pattern)

            override fun formatLabel(value: Double, isValueX: Boolean): String {
                if (isValueX) {
                    return sdf.format(Date(value.toLong()))
                } else {
                    return super.formatLabel(value, isValueX)
                }
            }
        }
    }
    //TODO : pattern is based on selected dates, not x-axis range (needs to be updated)
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
        }
        return pattern
    }

    private fun addToPattern(pattern: String, addition: String): String {
        if (pattern.isNotEmpty())
        return "$pattern:$addition"
        else return addition
    }

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

    private fun setOptions() {
        //Create curve/series for graph
        val lineGraphSeriesList = loadData()
        val graphOne = binding.graphOne

        for ((i, line) in lineGraphSeriesList.withIndex()) {
            setLineSettings(line, i)
            when (i) {
                0 -> {
                    setGraphSettings(graphOne)
                    graphOne.gridLabelRenderer.isHighlightZeroLines = false;
                    graphOne.addSeries(line)
                    labelFormat(binding.graphOne, getPattern())
                    graphOne.visibility = View.VISIBLE
                    lineData[i]?.let { graphOne.gridLabelRenderer.verticalLabelsColor = it.color }
                    graphOne.gridLabelRenderer.setHumanRounding(false, true)
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
                    graphTwo.visibility = View.VISIBLE
                    labelFormat(binding.graphTwo, getPattern())

                    lineData[i]?.let { graphTwo.gridLabelRenderer.verticalLabelsColor = it.color }
                    graphTwo.gridLabelRenderer.setHumanRounding(false, true)
                }
            }
        }

        setXAxisRange(getXAxisRange(lineGraphSeriesList))
    }

    private fun setLineSettings(line: LineGraphSeries<DataPoint>, index: Int) {
        line.color = lineData[index]?.color!!
        //series.title = "Blodsukker" //Needed for creating legend described below
        line.isDrawDataPoints = true //Shows datapoints as circles in the curve
        line.dataPointsRadius = 16F //layout for datapoints
        line.thickness = 8 //Layout for datapoints
    }

    private fun setGraphSettings(graph: GraphView) {
        graph.visibility = View.VISIBLE
        labelFormat(binding.graphTwo, getPattern())

        graph.titleTextSize = 90.0F
//        graphTwo.titleColor = Color.BLUE

        //Legend (used for displaying overview of curves)
        //graph.legendRenderer.isVisible = true
//        graph.legendRenderer.align = com.jjoe64.graphview.LegendRenderer.LegendAlign.TOP

        //Axis titles
        val gridLabel = graph.gridLabelRenderer as GridLabelRenderer
        //gridLabel.horizontalAxisTitle = "X Axis Title"
        gridLabel.horizontalAxisTitleTextSize = 50F
        //gridLabel.verticalAxisTitle = "Y Axis Title"
        gridLabel.verticalAxisTitleTextSize = 25F
        // this will draw a border aroung graph and will also show both axis
        graph.viewport.setDrawBorder(true)

        graph.gridLabelRenderer.setHumanRounding(false, true)
    }

    private fun setSecondScaleYAxis(line: LineGraphSeries<DataPoint>, secondScale: SecondScale) {
        secondScale.setMinY(line.lowestValueY)
        secondScale.setMaxY(line.highestValueY)
    }

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

    fun onDelDropdown() {
        if (activity?.let { ContextCompat.checkSelfPermission(it,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) } == PackageManager.PERMISSION_DENIED) {
            activity?.let { ActivityCompat.requestPermissions(it,arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),1 ) }
        }

        if (activity?.let { ContextCompat.checkSelfPermission(it,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) } == PackageManager.PERMISSION_GRANTED) {
            val graph = dataViewModel.getGraph()
            graph?.setBackgroundColor(resources.getColor(R.color.white));
            graph?.takeSnapshotAndShare(activity, "Graph snapshot", "Del!")
        }

        else{
            Toast.makeText(context, "Du mangler tilladelse til at dele", Toast.LENGTH_SHORT).show()
        }
    }
}