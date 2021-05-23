package com.tlapp.beertimemm

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}