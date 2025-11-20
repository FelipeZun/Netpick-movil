package com.example.netpick_movil

import io.kotest.core.listeners.AfterSpecListener
import io.kotest.core.listeners.BeforeSpecListener
import io.kotest.core.spec.Spec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutinesTestExtension : BeforeSpecListener, AfterSpecListener {
    private val dispatcher = StandardTestDispatcher()

    override suspend fun beforeSpec(spec: Spec) {
        Dispatchers.setMain(dispatcher)
    }

    override suspend fun afterSpec(spec: Spec) {
        Dispatchers.resetMain()
    }
}