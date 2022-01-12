package com.example.openlog.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.example.openlog.databinding.FragmentDisplayGraphBinding
import com.example.openlog.viewModel.DataViewModel
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DisplayGraph : Fragment() {

    private val dataViewModel: DataViewModel by activityViewModels()
    private lateinit var binding: FragmentDisplayGraphBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //Obtains graph from layout
        binding = FragmentDisplayGraphBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = dataViewModel.getInputList()
        val fireBaseUser = dataViewModel.getCurrentFirebaseUser()
        val graph = binding.graphBlodsukker
        labelFormat(graph,"dd:HH")
        getData(graph)?.let { setOptions(graph, it) }
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
        val dataList = dataViewModel.getInputList()[0]
        val dataPoints = dataList?.let { arrayOfNulls<DataPoint>(it.size) }

        if (dataList != null) {
            for ((index, item) in dataList.withIndex()) {
                dataPoints?.set(index,
                    DataPoint(item.getInputTwoAsDate(), item.firstInput.toDouble())
                )
            }
        }

        if (dataList != null) {
            if (dataList.isNotEmpty()) {
                graph.viewport.setMinX(dataList?.get(0)?.getInputTwoAsDate()?.time.toDouble())
                dataList?.get(dataList.size-1)?.getInputTwoAsDate()?.time?.let {
                    graph.viewport.setMaxX(
                        it.toDouble())
                }
                graph.viewport.isXAxisBoundsManual = true
            }
        }

        return dataPoints
    }
}