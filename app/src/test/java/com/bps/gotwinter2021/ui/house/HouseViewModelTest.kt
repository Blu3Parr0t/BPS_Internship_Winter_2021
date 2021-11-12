package com.bps.gotwinter2021.ui.house

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bps.gotwinter2021.data.model.GOTResponse
import com.bps.gotwinter2021.data.network.networkmodel.ServiceResult
import com.bps.gotwinter2021.data.network.repoimpl.GOTRepoImpl
import com.bps.gotwinter2021.favorites.database.FavoriteDatabaseDao
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HouseViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    //Dependencies and testing subject
    private lateinit var viewModelTest: HouseViewModel
    private val testApp = mockk<Application>(relaxed = true)
    private val testRepo = mockk<GOTRepoImpl>(relaxed = true)
    private val testDataSource = mockk<FavoriteDatabaseDao>(relaxed = true)
    private val testDispatchers = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModelTest =
            HouseViewModel(
                app = testApp,
                GOTRepo = testRepo,
                GOTDBDao = testDataSource,
                dispatchers = testDispatchers
            )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `fetchCharacterByHouse should return SUCCESSFUL Stark House`() = runBlockingTest {
        //Set Up
        coEvery { testRepo.fetchCharactersByHouse(any()) } returns createSuccessfulStarkCall()

        //Test
        viewModelTest.fetchCharactersByHouse("House Stark")

        //Assert
        assertEquals(
            23, viewModelTest.characterFromHouse.value?.size
        )
        assertEquals(
            createSuccessfulStarkCall().data[11].name, viewModelTest.characterFromHouse.value?.get(11)?.name
        )
//        assertEquals(
//            createSuccessfulStarkCall().data[11].id, viewModelTest.characterFromHouse.value?.get(11)?.id
//        )
//        assertEquals(
//            createSuccessfulStarkCall().data[11].image, viewModelTest.characterFromHouse.value?.get(11)?.image
//        )
//        assertEquals(
//            createSuccessfulStarkCall().data[11].father, viewModelTest.characterFromHouse.value?.get(11)?.father
//        )
    }

    private fun createSuccessfulStarkCall(): ServiceResult.Succes<List<GOTResponse>> {
        return ServiceResult.Succes(
            listOf(
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>() {
                    every { id } returns "1234"
                    every { name } returns "I AM THE GREATEST"
                    every { image } returns "Yo mama"
                    every { house } returns "f*** yo couch"
                    every { titles } returns arrayListOf("tiddies", "no title")
                    every { father } returns "Darth Vader"
                    every { mother } returns "Gaia"
                },
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>(relaxed = true),
                mockk<GOTResponse>(relaxed = true)
            )
        )
    }
}
