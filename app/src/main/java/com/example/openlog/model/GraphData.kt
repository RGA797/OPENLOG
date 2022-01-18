package com.example.openlog.model

import com.jjoe64.graphview.GraphView

//Model class that holds the current graph,
class GraphData() {
    var graph: GraphView? = null

    //sets a given graphview to be its instance variable
    fun setGraphData(graphData: GraphView){
        this.graph = graphData
    }

    //returns the graph it holds. Null if none has been set
    fun getGraphData(): GraphView? {
        return graph
    }

}