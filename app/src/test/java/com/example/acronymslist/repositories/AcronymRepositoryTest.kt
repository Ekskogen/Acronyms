package com.example.acronymslist.repositories

import com.example.acronymslist.data.models.Acronym
import com.example.acronymslist.data.models.LongForm
import com.example.acronymslist.data.network.AcronymService
import com.example.acronymslist.data.network.NoInternetException
import com.example.acronymslist.data.repositories.AcronymRepositoryImpl
import com.example.acronymslist.data.network.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AcronymRepositoryTest {

    lateinit var repository: AcronymRepositoryImpl
    var service: AcronymService = mockk()

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
        repository = AcronymRepositoryImpl(service)
    }

    @Test
    fun getAcronyms_SuccessfulWithBody_WillReturnSuccessWithValue() {
        coEvery { service.getAcronyms("ok") }  returns retrofit2.Response.success(listOf(mockedAcronym))

        runBlocking {
            val response = repository.getAcronyms("ok")
            coVerify(exactly = 1) { service.getAcronyms("ok") }
            assertTrue(response is Result.Success && response.result == mockedAcronym)
        }
    }

    @Test
    fun getAcronyms_SuccessfulWithoutBody_WillReturnSuccessEmptyList() {
        coEvery { service.getAcronyms("ok") }  returns retrofit2.Response.success(null)

        runBlocking {
            val response = repository.getAcronyms("ok")
            coVerify(exactly = 1) { service.getAcronyms("ok") }
            assertTrue(response is Result.Success && response.result.lfs.isEmpty())
        }
    }

    @Test
    fun getAcronyms_NetworkNotSuccessful_WillReturnFailure() {
        coEvery { service.getAcronyms("ok") }  returns retrofit2.Response.error(404, ResponseBody.create(null,""))

        runBlocking {
            val response = repository.getAcronyms("ok")
            coVerify(exactly = 1) { service.getAcronyms("ok") }
            assertTrue(response is Result.Failure)
        }
    }

    @Test
    fun getAcronyms_NetworkNoInternet_WillReturnFailureWithException() {
        coEvery { service.getAcronyms("ok") }  throws UnknownHostException()

        runBlocking {
            val response = repository.getAcronyms("ok")
            coVerify(exactly = 1) { service.getAcronyms("ok") }
            assertTrue(response is Result.Failure && response.throwable == NoInternetException)
        }
    }

    @Test
    fun getAcronyms_NetworkException_WillReturnFailureWithThatException() {
        coEvery { service.getAcronyms("ok") }  throws SocketTimeoutException()

        runBlocking {
            val response = repository.getAcronyms("ok")
            coVerify(exactly = 1) { service.getAcronyms("ok") }
            assertTrue(response is Result.Failure && response.throwable is SocketTimeoutException)
        }
    }

    @After
    fun afterTests() {
        unmockkAll()
    }


}