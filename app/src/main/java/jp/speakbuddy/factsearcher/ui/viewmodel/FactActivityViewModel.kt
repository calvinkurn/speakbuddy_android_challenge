package jp.speakbuddy.factsearcher.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.speakbuddy.factsearcher.network.FactServiceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FactActivityViewModel() : ViewModel() {

    private val _factContent = MutableStateFlow("-")
    val factContent get() = _factContent

    private val _errorState = MutableStateFlow("")
    val errorState get() = _errorState

    private val _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading

    fun updateFact() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.tryEmit(true)
            try {
                FactServiceProvider.provide().getFact().let {
                    _factContent.tryEmit(it.fact)
                }
            } catch (e: Throwable) {
                // TODO: Implement error mapping (BE error -> FE error)
                _errorState.tryEmit(e.message ?: "Unknown")
                "something went wrong. error = ${e.message}"
            }.also {
                _isLoading.tryEmit(false)
            }
        }
    }
}
