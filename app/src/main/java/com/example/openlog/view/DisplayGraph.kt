package com.example.openlog.view

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.openlog.R
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.example.openlog.databinding.FragmentDisplayGraphBinding
import com.example.openlog.viewModel.DataViewModel
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class DisplayGraph : Fragment() {

    private val dataViewModel: DataViewModel by activityViewModels()
    private lateinit var binding: FragmentDisplayGraphBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        //Obtains graph from layout
        binding = FragmentDisplayGraphBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val graph = binding.graphView
        labelFormat(graph,"dd:HH")
        getData(graph)?.let { setOptions(graph, it) }
        dataViewModel.setGraph(graph)
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


    private fun setOptions(graph: GraphView, dataArray: Array<DataPoint?>) {
        //Create curve/series for graph
        val series = LineGraphSeries(dataArray)

        //Add curve to graph
        graph.addSeries(series)
        //Set colour, title of curve, DataPoints radius, thickness
        series.color = Color.RED //or Color.rgb(0,80,100)
        //series.title = "Blodsukker" //Needed for creating legend described below
        series.isDrawDataPoints = true //Shows datapoints as circles in the curve
        series.dataPointsRadius = 16F //layout for datapoints
        series.thickness = 8 //Layout for datapoints


        //Title of graph
        //graph.title = "Kulhydrater"
        graph.titleTextSize = 90.0F
        graph.titleColor = Color.BLUE

        //Legend (used for displaying overview of curves)
        //graph.legendRenderer.isVisible = true
        graph.legendRenderer.align = com.jjoe64.graphview.LegendRenderer.LegendAlign.TOP

        //Axis titles
        val gridLabel = graph.gridLabelRenderer as GridLabelRenderer
        //gridLabel.horizontalAxisTitle = "X Axis Title"
        gridLabel.horizontalAxisTitleTextSize = 50F
        //gridLabel.verticalAxisTitle = "Y Axis Title"
        gridLabel.verticalAxisTitleTextSize = 50F

        gridLabel.verticalLabelsSecondScaleColor
    }

    private fun getData(graph: GraphView): Array<DataPoint?>? {
        val dataList = dataViewModel.getInputList()[1]
        val dataPoints = dataList?.let { arrayOfNulls<DataPoint>(it.size) }
//        Date(2222,1,1,1,1,1)
        if (dataList != null) {
            for ((index, item) in dataList.withIndex()) {
                dataPoints?.set(index,
                    DataPoint(item.getInputTwoAsDate(), item.firstInput.toDouble())
                )
            }
        }

        if (dataList != null) {
            if (dataList.isNotEmpty()) {
                graph.viewport.setMinX(dataList.get(0).getInputTwoAsDate().time.toDouble())
                dataList.get(dataList.size-1).getInputTwoAsDate().time.let {
                    graph.viewport.setMaxX(
                        it.toDouble())
                }
                graph.viewport.isXAxisBoundsManual = true
            }
        }
//        graph.viewport.setMinY(0.0);
//        graph.viewport.setMaxY(200.0);

        return dataPoints
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