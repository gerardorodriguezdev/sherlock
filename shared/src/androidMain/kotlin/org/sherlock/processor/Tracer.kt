package org.sherlock.processor

import android.os.Debug

private class AndroidTracer : Tracer {
    override fun startTrace(name: String) {
        Debug.startMethodTracing(name)
    }

    override fun stopTrace() {
        Debug.stopMethodTracing()
    }
}

actual fun tracer(): Tracer = AndroidTracer()