package jp.speakbuddy.factsearcher.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.speakbuddy.factsearcher.domain.model.CatFactUiModel
import jp.speakbuddy.factsearcher.domain.model.DefaultCatFactUiModel
import jp.speakbuddy.factsearcher.domain.usecase.FactUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FactActivityViewModel @Inject constructor(
    private val factUseCase: FactUseCase
) : ViewModel() {

    private val _factContent = MutableStateFlow(DefaultCatFactUiModel)
    val factContent get() = _factContent

    private val _errorState = MutableStateFlow("")
    val errorState get() = _errorState

    private val _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading

    fun updateFact() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.tryEmit(true)
            try {
                factUseCase.getRandomCatFact().let {
                    _factContent.tryEmit(it)
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
