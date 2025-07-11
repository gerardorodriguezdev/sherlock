package org.sherlock.processor

interface Tracer {
    fun startTrace(name: String)
    fun stopTrace()
}

class EmptyTracer : Tracer {
    override fun startTrace(name: String) {}
    override fun stopTrace() {}
}