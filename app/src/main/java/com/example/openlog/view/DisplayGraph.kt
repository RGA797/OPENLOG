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
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.seconds


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

        setOptions()
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


    private fun setOptions() {
        //Create curve/series for graph
        val lineGraphSeriesList = loadData()
        val graphOne = binding.graphOne


        var lineNumber = 0
        for ((i, line) in lineGraphSeriesList.withIndex()) {

            if (lineNumber == 0) {
                graphOne.addSeries(line)
                line.color = Color.BLUE //or Color.rgb(0,80,100)
                //series.title = "Blodsukker" //Needed for creating legend described below
                line.isDrawDataPoints = true //Shows datapoints as circles in the curve
                line.dataPointsRadius = 16F //layout for datapoints
                line.thickness = 8 //Layout for datapoints
                labelFormat(binding.graphOne, "dd:HH")
                //graphOne.viewport.setMinX(line.lowestValueX)

                lineNumber++
            } else if (lineNumber == 1) {
                line.color = Color.RED
                graphOne.addSeries(line)
//                        graphOne.viewport.setMaxX(line.highestValueX)
//                        graphOne.viewport.isXAxisBoundsManual = true
                lineNumber++
            } else {
                val graphTwo = binding.graphTwo
                graphTwo.addSeries(line)
                line.color = Color.RED //or Color.rgb(0,80,100)
                //series.title = "Blodsukker" //Needed for creating legend described below
                line.isDrawDataPoints = true //Shows datapoints as circles in the curve
                line.dataPointsRadius = 16F //layout for datapoints
                line.thickness = 8 //Layout for datapoints
                graphTwo.visibility = View.VISIBLE
                labelFormat(binding.graphTwo, "dd:HH")

                graphTwo.titleTextSize = 90.0F
                graphTwo.titleColor = Color.BLUE

                //Legend (used for displaying overview of curves)
                //graph.legendRenderer.isVisible = true
                graphTwo.legendRenderer.align = com.jjoe64.graphview.LegendRenderer.LegendAlign.TOP

                //Axis titles
                val gridLabel = graphTwo.gridLabelRenderer as GridLabelRenderer
                //gridLabel.horizontalAxisTitle = "X Axis Title"
                gridLabel.horizontalAxisTitleTextSize = 50F
                //gridLabel.verticalAxisTitle = "Y Axis Title"
                gridLabel.verticalAxisTitleTextSize = 25F

                gridLabel.verticalLabelsSecondScaleColor
            }
        }

        setXAxisRange(getXAxisRange(lineGraphSeriesList))


        //Title of graph
        //graph.title = "Kulhydrater"
        graphOne.titleTextSize = 90.0F
        graphOne.titleColor = Color.BLUE

        //Legend (used for displaying overview of curves)
        //graph.legendRenderer.isVisible = true
        graphOne.legendRenderer.align = com.jjoe64.graphview.LegendRenderer.LegendAlign.TOP

        //Axis titles
        val gridLabel = graphOne.gridLabelRenderer as GridLabelRenderer
        //gridLabel.horizontalAxisTitle = "X Axis Title"
        gridLabel.horizontalAxisTitleTextSize = 50F
        //gridLabel.verticalAxisTitle = "Y Axis Title"
        gridLabel.verticalAxisTitleTextSize = 25F

        gridLabel.verticalLabelsSecondScaleColor


//        line.color = Color.RED //or Color.rgb(0,80,100)
//        //series.title = "Blodsukker" //Needed for creating legend described below
//        line.isDrawDataPoints = true //Shows datapoints as circles in the curve
//        line.dataPointsRadius = 16F //layout for datapoints
//        line.thickness = 8 //Layout for datapoints
//        graphTwo.visibility = View.VISIBLE
//        labelFormat(binding.graphTwo, "dd:HH")
//
//        graphTwo.titleTextSize = 90.0F
//        graphTwo.titleColor = Color.BLUE
//
//        //Legend (used for displaying overview of curves)
//        //graph.legendRenderer.isVisible = true
//        graphTwo.legendRenderer.align = com.jjoe64.graphview.LegendRenderer.LegendAlign.TOP
//
//        //Axis titles
//        val gridLabel = graphTwo.gridLabelRenderer as GridLabelRenderer
//        //gridLabel.horizontalAxisTitle = "X Axis Title"
//        gridLabel.horizontalAxisTitleTextSize = 50F
//        //gridLabel.verticalAxisTitle = "Y Axis Title"
//        gridLabel.verticalAxisTitleTextSize = 25F
//
//        gridLabel.verticalLabelsSecondScaleColor
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
        val dataList = dataViewModel.getInputList()
        val dataPoints = arrayOfNulls<Array<DataPoint?>>(dataList.size)

        val lineGraphSeriesList = ArrayList<LineGraphSeries<DataPoint>>(0)

        for ((i, list) in dataList.withIndex()) {
            if (list != null) {
                dataPoints[i] = Array(list.size) { DataPoint(0.0,0.0) }
                for ((j, inputDTO) in list.withIndex()) {
                    dataPoints[i]?.set(j,
                        (DataPoint(inputDTO.getInputTwoAsDate(), inputDTO.firstInput.toDouble()))
                    )
                }
                lineGraphSeriesList.add(LineGraphSeries(dataPoints[i]))
            }
        }
        return lineGraphSeriesList
    }
}