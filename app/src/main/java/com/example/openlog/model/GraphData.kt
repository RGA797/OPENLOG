package com.example.openlog.model

import com.jjoe64.graphview.GraphView

class GraphData() {
    var graph: GraphView? = null

    fun setGraphData(graphData: GraphView){
        graph = graphData
    }

    fun getGraphData(): GraphView? {
        return graph
    }

}