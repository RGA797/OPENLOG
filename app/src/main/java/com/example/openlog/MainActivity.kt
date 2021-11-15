package com.example.openlog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* Test any desired layout by replacing the activity_main (xml)
           with another xml-file (ex. fragment_status_bar__insulin etc.)
        * */
        setContentView(R.layout.fragment_status_bar__kulhydrater)

        //arrayOf(DataPoint...) kan benyttes som viewmodel, mens selve grafen vises i view.
        //Data til DataPoint hentes fra en DB connector som derfra henter data fra en database
        //Grafen indsættes vha. datapoints hvor x-værdien altid forøges,
        // dvs. DataPoint(5,0, 1.0), DataPoint(4.0, 1.0) giver fejl og programmet crasher
        //Graphviews der ikke vises i et bestemt view(eks. fragment_status_bar_kulhydrater)
        //vil resultere i et NullPointerException

        //val graphInsulin = findViewById(R.id.graph_insulin) as GraphView
        //val seriesInsulin = LineGraphSeries(arrayOf(DataPoint(1.0, 1.0), DataPoint(1.0,5.0), DataPoint(2.0, 3.0)))
        //graphInsulin.addSeries(seriesInsulin)

        val graphKulhydrater = findViewById(R.id.graph_kulhydrater) as GraphView
        val seriesKulhydrater = LineGraphSeries(arrayOf(DataPoint(4.0, 2.0), DataPoint(7.0, 4.0), DataPoint(8.0, 5.0)))
        graphKulhydrater.addSeries(seriesKulhydrater)

        //val graphBlodsukker = findViewById(R.id.graph_blodsukker) as GraphView
        //val seriesBlodsukker = LineGraphSeries(arrayOf(DataPoint(1.0, 2.0), DataPoint(4.0, 5.0), DataPoint(6.0, 3.0)))
        //graphBlodsukker.addSeries(seriesBlodsukker)
    }
}