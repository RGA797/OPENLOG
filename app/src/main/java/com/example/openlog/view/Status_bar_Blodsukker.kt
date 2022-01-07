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
import com.example.openlog.databinding.FragmentStatusBarBlodsukkerBinding
import com.example.openlog.viewModel.DataViewModel


class Status_bar_Blodsukker : Fragment() {

    private val dataViewModel: DataViewModel by activityViewModels()
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

        dataViewModel.getUserData(dataViewModel.getCurrentFirebaseUser()!!, "kulhydrat")
        val dataList = dataViewModel.currentDataList


        val dataArray = emptyArray<DataPoint>()


        var i = 0
        for (item in dataList.value!!) {

            dataArray[i] = DataPoint(i++.toDouble(),item.value.toDouble())
        }

        val seriesKulhydrater = LineGraphSeries(dataArray)


        //Create curve/series for graph
//        val seriesKulhydrater = LineGraphSeries(
//            arrayOf(
//                DataPoint(2.0, 200.0),
//                DataPoint(3.0, 100.0),
//                DataPoint(4.0, 0.0)
//            )
//        )
//
//        val seriesKulhydrater2 = LineGraphSeries(
//            arrayOf(
//                DataPoint(2.0, 0.0),
//                DataPoint(3.0, 5.0),
//                DataPoint(4.0, 10.0)
//            )
//        )
        val graph = binding.graphBlodsukker

        //Add curve to graph
        graph.addSeries(seriesKulhydrater)
        //graphBlodSukker.addSeries(seriesKulhydrater2)

        //Set colour, title of curve, DataPoints radius, thickness
        seriesKulhydrater.setColor(Color.RED) //or Color.rgb(0,80,100)
        seriesKulhydrater.setTitle("Blodsukker") //Needed for creating legend described below
        seriesKulhydrater.setDrawDataPoints(true) //Shows datapoints as circles in the curve
        seriesKulhydrater.setDataPointsRadius(16F) //layout for datapoints
        seriesKulhydrater.setThickness(8) //Layout for datapoints

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