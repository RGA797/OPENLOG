package com.example.openlog.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.example.openlog.databinding.FragmentStatusBarBlodsukkerBinding


class Status_bar_Blodsukker : Fragment() {

    private lateinit var binding: FragmentStatusBarBlodsukkerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //Optains graph from layout
        binding = FragmentStatusBarBlodsukkerBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Create curve/series for graph
        val seriesKulhydrater = LineGraphSeries(
            arrayOf(
                DataPoint(4.0, 2.0),
                DataPoint(7.0, 4.0),
                DataPoint(8.0, 5.0)
            )
        )
        val graphBlodSukker = binding.graphBlodsukker

        //Add curve to graph
        graphBlodSukker.addSeries(seriesKulhydrater)

        //Set colour, title of curve, DataPoints radius, thickness
        seriesKulhydrater.setColor(Color.RED) //or Color.rgb(0,80,100)
        seriesKulhydrater.setTitle("Kulhydrater") //Needed for creating legend described below
        seriesKulhydrater.setDrawDataPoints(true) //Shows datapoints as circles in the curve
        seriesKulhydrater.setDataPointsRadius(16F) //layout for datapoints
        seriesKulhydrater.setThickness(8) //Layout for datapoints

        //Title of graph
        graphBlodSukker.setTitle("Kulhydrater")
        graphBlodSukker.setTitleTextSize(90.0F)
        graphBlodSukker.setTitleColor(Color.BLUE)

        //Legend (used for displaying overview of curves)
        graphBlodSukker.getLegendRenderer().setVisible(true)
        graphBlodSukker.getLegendRenderer().setAlign(com.jjoe64.graphview.LegendRenderer.LegendAlign.TOP)

        //Axis titles
        val gridLabelKulhydrat = graphBlodSukker.getGridLabelRenderer() as GridLabelRenderer
        gridLabelKulhydrat.setHorizontalAxisTitle("X Axis Title")
        gridLabelKulhydrat.setHorizontalAxisTitleTextSize(50F)
        gridLabelKulhydrat.setVerticalAxisTitle("Y Axis Title")
        gridLabelKulhydrat.setVerticalAxisTitleTextSize(50F)

    }
}