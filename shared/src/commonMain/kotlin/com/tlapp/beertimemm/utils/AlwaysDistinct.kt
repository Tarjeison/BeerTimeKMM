package com.tlapp.beertimemm.utils


// Convenience class to be used with StateFlows which should re-emit on the same value
class AlwaysDistinct<T>(val value: T) {
    override fun equals(other: Any?): Boolean {
        return false
    }
    override fun hashCode(): Int {
        return this.hashCode()
    }
}
