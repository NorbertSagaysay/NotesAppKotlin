package com.example.notesappkotlin.data

class Note {

    var nodeID: Int? = null
    //var nodeUser: String? = null
    var nodeName: String? = null
    var nodeDesc: String? = null

    constructor(nodeID: Int, nodeName: String, nodeDesc: String) {
        this.nodeID = nodeID
        //this.nodeUser = nodeUser
        this.nodeName = nodeName
        this.nodeDesc = nodeDesc
    }
}