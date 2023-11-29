package com.cobs.instantshopping.model

class Fruit{
    var name:String? = null
    var price: String?=null
    var description: String?=null
    var quantity: String? = null
    var category: String? = null
    var image: String?=null

    constructor(
        name: String?,
        price: String?,
        description: String?,
        quantity: String?,
        category: String?,
        image: String?
    ) {
        this.name = name
        this.price = price
        this.description = description
        this.quantity = quantity
        this.category = category
        this.image = image
    }

    constructor()
}