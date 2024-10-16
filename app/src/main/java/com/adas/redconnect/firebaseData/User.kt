package com.adas.redconnect.firebaseData

class User {
    var name: String? = null
    var uid: String? = null

    constructor(){}

    constructor(name: String?, uid: String?){
        this.name = name
        this.uid = uid
    }
}