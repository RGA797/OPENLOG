package com.example.openlog.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.openlog.R
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries


class Status_bar_Kulhydrater : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //Optains graph from layout
        val graphKulhydrater = findViewById(R.id.graph_kulhydrater) as Graphview

        //Create curve/series for graph
        val seriesKulhydrater = com.jjoe64.graphview.series.LineGraphSeries(
            arrayOf(
                com.jjoe64.graphview.series.DataPoint(4.0, 2.0),
                DataPoint(7.0, 4.0),
                DataPoint(8.0, 5.0)
            )
        )

        //Add curve to graph
        graphKulhydrater.addSeries(seriesKulhydrater)

        //Set colour, title of curve, DataPoints radius, thickness
        seriesKulhydrater.setColor(Color.RED) //or Color.rgb(0,80,100)
        seriesKulhydrater.setTitle("Kulhydrater") //Needed for creating legend described below
        seriesKulhydrater.setDrawDataPoints(true) //Shows datapoints as circles in the curve
        seriesKulhydrater.setDataPointsRadius(16) //layout for datapoints
        seriesKulhydrater.setThickness(8) //Layout for datapoints

        //Title of graph
        graphKulhydrater.setTitle("Kulhydrater")
        graphKulhydrater.setTitleTextSize(90)
        graphKulhydrater.setTitleColor(Color.BLUE)

        //Legend (used for displaying overview of curves)
        graphkulhydrater.getLegendRenderer().setVisible(true)
        graphkulhydrater.getLegendRenderer().setAlign(com.jjoe64.graphview.LegendRenderer.LegendAlign.TOP)

        //Axis titles
        val gridLabelKulhydrat = graphkulhydrater.getGridLabelRenderer() as GridLabelRenderer
        gridLabelKulhydrat.setHorizontalAxisTitle("X Axis Title")
        gridLabelKulhydrat.setHorizontalAxisTitleTextSize(50)
        gridLabelKulhydrat.setVerticalAxisTitle("Y Axis Title")
        gridLabelKulhydrat.setVerticalAxisTitleTextSize(50)

        return inflater.inflate(R.layout.fragment_status_bar__kulhydrater, container, false)
    }
}