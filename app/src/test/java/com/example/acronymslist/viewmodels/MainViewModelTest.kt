package com.example.acronymslist.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.acronymslist.data.models.Acronym
import com.example.acronymslist.data.models.LongForm
import com.example.acronymslist.data.network.CommonException
import com.example.acronymslist.data.network.Result
import com.example.acronymslist.domain.GetAcronymsUseCase
import com.example.acronymslist.ui.main.MainViewModel
import com.example.acronymslist.ui.main.State
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: MainViewModel
    var getAcronymsUseCase: GetAcronymsUseCase = mockk()

    val observer = mockk<Observer<State>> { every { onChanged(any()) } just Runs }
    val loadObserver = mockk<Observer<Boolean>> { every { onChanged(any()) } just Runs }

    companion object {
        val mockedAcronym = Acronym("ok", arrayListOf(
            LongForm("hey", 39, 2009),
            LongForm("hgdsf", 429, 2000),
            LongForm("fsdf", 35, 20011),
            LongForm("fdafad", 4329, 2009)
        ))
    }

    @Before
    fun setUp() {

        viewModel = MainViewModel(getAcronymsUseCase)

        viewModel.state.observeForever(observer)
        viewModel.loading.observeForever(loadObserver)

    }

    @Test
    fun loadCharacters_Failure_WillShowFailInView() {
        coEvery { getAcronymsUseCase.execute("ok") }  returns Result.Failure()

        viewModel.loadAcronyms("ok")

        verifySequence {
            loadObserver.onChanged(true)
            loadObserver.onChanged(false)
            observer.onChanged(State.Error(CommonException))
        }
    }

    @Test
    fun loadCharacters_Success_WillShowAcronyms() {
        coEvery { getAcronymsUseCase.execute("ok") }  returns Result.Success(mockedAcronym.lfs)

        viewModel.loadAcronyms("ok")

        verifySequence {
            loadObserver.onChanged(true)
            loadObserver.onChanged(false)
            observer.onChanged(State.Done(mockedAcronym.lfs))
        }
    }

    @After
    fun afterTests() {
        unmockkAll()
    }

}