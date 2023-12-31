package com.cobs.instantshopping.model

class UserModel {
    var id:String?=null
    var image:String? = null
    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
    var password: String? = null
    var phone: String? = null
    var position: String = "Customer"

    constructor(
        id:String?,
        image:String?,
        firstName: String?,
        lastName: String?,
        email: String?,
        password: String?,
        phone: String?,
        position: String = "Customer"
    ) {
        this.id = id
        this.image = image
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.password = password
        this.phone = phone

    }

    constructor()

}
