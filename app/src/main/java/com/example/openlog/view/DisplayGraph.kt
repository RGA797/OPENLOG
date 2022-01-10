package com.example.openlog.view

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
import java.util.*


class DisplayGraph : Fragment() {

    private val dataViewModel: DataViewModel by activityViewModels()
    private lateinit var binding: FragmentDisplayGraphBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //Optains graph from layout
        binding = FragmentDisplayGraphBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOptions()
    }


    fun setOptions() {

        val graph = binding.graphBlodsukker

        //Create curve/series for graph
        val series = LineGraphSeries(
            arrayOf(
                DataPoint(Date().time.toDouble(),1.0),
                DataPoint(Date().time.toDouble(),1.0),
                DataPoint(Date().time.toDouble(),1.0),
                DataPoint(Date().time.toDouble(),1.0)
            )
        )

        //Add curve to graph
        graph.addSeries(series)
        //Set colour, title of curve, DataPoints radius, thickness
        series.setColor(Color.RED) //or Color.rgb(0,80,100)
        series.setTitle("Blodsukker") //Needed for creating legend described below
        series.setDrawDataPoints(true) //Shows datapoints as circles in the curve
        series.setDataPointsRadius(16F) //layout for datapoints
        series.setThickness(8) //Layout for datapoints

        //Title of graph
        graph.setTitle("Kulhydrater")
        graph.setTitleTextSize(90.0F)
        graph.setTitleColor(Color.BLUE)

        //Legend (used for displaying overview of curves)
        graph.getLegendRenderer().setVisible(true)
        graph.getLegendRenderer().setAlign(com.jjoe64.graphview.LegendRenderer.LegendAlign.TOP)

        //Axis titles
        val gridLabel = graph.getGridLabelRenderer() as GridLabelRenderer
        gridLabel.setHorizontalAxisTitle("X Axis Title")
        gridLabel.setHorizontalAxisTitleTextSize(50F)
        gridLabel.setVerticalAxisTitle("Y Axis Title")
        gridLabel.setVerticalAxisTitleTextSize(50F)

        gridLabel.verticalLabelsSecondScaleColor
    }
}