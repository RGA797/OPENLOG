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
        val fireBaseUser = dataViewModel.getCurrentFirebaseUser()
        val graph = binding.graphBlodsukker
        val date = Calendar.getInstance()
        date.set(1,1,1,1,1,1)
        val start = date.time
        date.set(2222,1,1,1,1,1)
        val end = date.time

        dataViewModel.updateInputData(fireBaseUser!!,"insulin", start, end)
        Thread.sleep(2500)
        labelFormat(graph, "hh:mm:ss")
        setOptions(graph, getData())
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


    private fun setOptions(graph: GraphView, dataArray: Array<DataPoint>) {
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

    private fun getData(): Array<DataPoint> {
        val dataList = dataViewModel.getDataList()
        val dataPoints = emptyArray<DataPoint>()
//        Date(2222,1,1,1,1,1)
        binding.category.text = dataList[0].firstInput
        for ((index, item) in dataList.withIndex()) {
            dataPoints[index] = DataPoint(item.getInputTwoAsDate(), item.firstInput.toDouble())
        }
//        val date = Calendar.getInstance()
//        date.set(1,1,1,1,1,1)
//        val one = date.time
//        date.set(1,1,1,2,1,1)
//        val two = date.time
//        date.set(1,1,1,3,1,1)
//        val three = date.time
//        date.set(1,1,1,4,1,1)
//        val four = date.time

//        dataPoints[0] = DataPoint(one, 1.0)
//        dataPoints[1] = DataPoint(two, 2.0)
//        dataPoints[2] = DataPoint(three, 3.0)
//        dataPoints[3] = DataPoint(four, 4.0)

//
//        val dataPoints = arrayOf(
//            DataPoint(one, 1.0),
//            DataPoint(two, 2.0),
//            DataPoint(three, 3.0),
//            DataPoint(four, 4.0),
//            )

        return dataPoints
    }
}