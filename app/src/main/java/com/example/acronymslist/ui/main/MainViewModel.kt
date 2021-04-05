package com.example.acronymslist.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acronymslist.data.models.LongForm
import com.example.acronymslist.data.network.Result
import com.example.acronymslist.domain.GetAcronymsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val getAcronymsUseCase: GetAcronymsUseCase
): ViewModel() {

    val state : MutableLiveData<State> = MutableLiveData()
    val loading : MutableLiveData<Boolean> = MutableLiveData()

    fun loadAcronyms(word: String) {
        loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val result = getAcronymsUseCase.execute(word)
            loading.postValue(false)
            if(result is Result.Success) {
                state.postValue(State.Done(result.result))
            } else if(result is Result.Failure) {
                state.postValue(State.Error(result.throwable))
            }
        }
    }

}

open class State {
    data class Error(val e: Throwable): State()
    data class Done(val acronyms: List<LongForm>): State()
}

