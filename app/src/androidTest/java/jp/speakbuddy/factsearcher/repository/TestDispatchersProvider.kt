package jp.speakbuddy.factsearcher.repository

import jp.speakbuddy.factsearcher.di.DispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatchersProvider : DispatchersProvider {
    override val default: CoroutineDispatcher = Dispatchers.Main
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.Main
}