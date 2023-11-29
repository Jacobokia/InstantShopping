package com.cobs.instantshopping.model

class Bread{
    var id:String?=null
    var name:String?=null
    var image:String?=null
    var description:String?=null
    var price:String?=null
    var quantity:String?=null

    constructor(
        id: String?,
        name: String?,
        price: String?,
        image: String?,
        description: String?,
        quantity: String?
    ) {
        this.id = id
        this.name = name
        this.price = price
        this.image = image
        this.description = description
        this.quantity = quantity
    }

    constructor()
}


