package com.cobs.instantshopping.model
class Meat{
    var id:String?=null
    var name:String?=null
    var image:String?=null
    var description:String?=null
    var price:String?=null
    var quantity:String?=null

    constructor(
        id: String?,
        name: String?,
        image: String?,
        description: String?,
        price: String?,
        quantity: String?
    ) {
        this.id = id
        this.name = name
        this.image = image
        this.description = description
        this.price = price
        this.quantity = quantity
    }
}


