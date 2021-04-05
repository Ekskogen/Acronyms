package com.example.acronymslist.usecases

import com.example.acronymslist.data.models.Acronym
import com.example.acronymslist.data.models.LongForm
import com.example.acronymslist.data.network.Result
import com.example.acronymslist.data.repositories.AcronymRepository
import com.example.acronymslist.domain.GetAcronymsUseCase
import com.example.acronymslist.domain.GetAcronymsUseCaseImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetAcronymsUsecaseTest {

    lateinit var usecase: GetAcronymsUseCase
    var repository: AcronymRepository = mockk()

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
        usecase = GetAcronymsUseCaseImpl(repository)
    }

    @Test
    fun execute_FailureFromNetwork_WillReturnFailure() {
        coEvery { repository.getAcronyms("ok") }  returns Result.Failure()

        runBlocking {
            val response = usecase.execute("ok")
            coVerify(exactly = 1) { repository.getAcronyms("ok") }
            assertTrue(response is Result.Failure)
        }
    }

    @Test
    fun execute_Success_WillReturnOnlyList() {
        coEvery { repository.getAcronyms("ok") }  returns Result.Success(mockedAcronym)

        runBlocking {
            val response = usecase.execute("ok")
            coVerify(exactly = 1) { repository.getAcronyms("ok") }
            assertTrue(response is Result.Success && response.result == mockedAcronym.lfs)
        }
    }

    @After
    fun afterTests() {
        unmockkAll()
    }

}