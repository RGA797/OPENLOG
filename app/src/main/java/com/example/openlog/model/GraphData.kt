package com.example.openlog.model

import com.jjoe64.graphview.GraphView

//Model class that holds the current graph,
class GraphData() {
    private var graph_one: GraphView? = null
    private var graph_two: GraphView? = null

    //sets a given graphview to be its instance variable
    fun setGraphData(graph_one: GraphView, graph_two: GraphView){
        this.graph_one = graph_one
        this.graph_two = graph_two
    }

    //returns the graph it holds. Null if none has been set
    fun getGraphOne(): GraphView? {
        return graph_one
    }

    fun getGraphTwo(): GraphView? {
        return graph_two
    }
}