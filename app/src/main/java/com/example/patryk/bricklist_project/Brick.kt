package com.example.patryk.bricklist_project

class Brick {
    var itemType:String = ""
    var itemID:String = ""
    var qty:String = ""
    var color:String = ""
    var extra:String = ""
    var alternate:String = ""
    var qtyInStore:String = ""
    //var picture:
    constructor(itemtype: String, itemID: String, qty: String, color: String, extra: String, alternate: String) {
        this.itemType = itemtype
        this.itemID = itemID
        this.qty = qty
        this.color = color
        this.extra = extra
        this.alternate = alternate
    }

    constructor( itemID: String, qty: String, qtyInStore: String, color: String) {
        this.itemID = itemID
        this.qty = qty
        this.qtyInStore = qtyInStore
        this.color = color
    }
}