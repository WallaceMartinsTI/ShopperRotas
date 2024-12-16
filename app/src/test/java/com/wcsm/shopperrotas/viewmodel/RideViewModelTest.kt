package com.wcsm.shopperrotas.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.wcsm.shopperrotas.data.remote.dto.Driver
import com.wcsm.shopperrotas.data.remote.dto.Ride
import com.wcsm.shopperrotas.data.model.RideResponse
import com.wcsm.shopperrotas.data.repository.IRideRepository
import kotlinx.coroutines.test.runTest

import app.cash.turbine.test
import com.wcsm.shopperrotas.data.model.RideConfirmRequest
import com.wcsm.shopperrotas.data.model.RideConfirmResponse
import com.wcsm.shopperrotas.data.remote.dto.Location
import com.wcsm.shopperrotas.data.remote.dto.Review
import com.wcsm.shopperrotas.data.model.RideEstimateRequest
import com.wcsm.shopperrotas.data.model.RideRequest
import com.wcsm.shopperrotas.data.model.RideResponseState
import com.wcsm.shopperrotas.data.remote.dto.RideEstimate
import com.wcsm.shopperrotas.data.remote.dto.RideOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class RideViewModelTest {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var rideRepository: IRideRepository

    private lateinit var rideViewModel: RideViewModel


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        Dispatchers.setMain(Dispatchers.Unconfined)
        rideViewModel = RideViewModel(rideRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `fetchRideEstimate should emit success response`() = runTest {
        val estimateRequest = RideEstimateRequest(
            customer_id = "Qualquer1",
            origin = "Av. Pres. Kenedy, 2385 - Remédios, Osasco - SP, 02675-031",
            destination = "Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200"
        )

        val expectedResponse = RideEstimate(
                origin = Location(0.0, 0.0),
                destination = Location(0.0, 0.0),
                distance = 0,
                duration = "",
                options = listOf(
                    RideOption(
                        id = 1,
                        name = "Homer Simpson",
                        description = "Olá! Sou o Homer, seu motorista camarada! Relaxe e aproveite o passeio, com direito a rosquinhas e boas risadas (e talvez alguns desvios).",
                        vehicle = "Plymouth Valiant 1973 rosa e enferrujado",
                        review = Review(
                            rating = 2.0,
                            comment = "Motorista simpático, mas errou o caminho 3 vezes. O carro cheira a donuts."
                        ),
                        value = 50.05
                    ),
                    RideOption(
                        id = 2,
                        name = "Dominic Toretto",
                        description = "Ei, aqui é o Dom. Pode entrar, vou te levar com segurança e rapidez ao seu destino. Só não mexa no rádio, a playlist é sagrada.",
                        vehicle = "Dodge Charger R/T 1970 modificado",
                        review = Review(
                            rating = 4.0,
                            comment = "Que viagem incrível! O carro é um show à parte e o motorista, apesar de ter uma cara de poucos amigos, foi super gente boa. Recomendo!"
                        ),
                        value = 100.09
                    ),
                    RideOption(
                        id = 3,
                        name = "James Bond",
                        description = "Boa noite, sou James Bond. À seu dispor para um passeio suave e discreto. Aperte o cinto e aproveite a viagem.",
                        vehicle = "Aston Martin DB5 clássico",
                        review = Review(
                            rating = 5.0,
                            comment = "Serviço impecável! O motorista é a própria definição de classe e o carro é simplesmente magnífico. Uma experiência digna de um agente secreto."
                        ),
                        value = 200.18
                    )
                ),
                routeResponse = Any()
        )


        Mockito.`when`(rideRepository.estimate(estimateRequest)).thenReturn(
            RideResponseState.Success(
                expectedResponse
            )
        )

        rideViewModel.fetchRideEstimate(
            estimateRequest.customer_id!!,
            estimateRequest.origin!!,
            estimateRequest.destination!!
        )

        advanceUntilIdle()

        rideViewModel.estimateResponse.test {
            assertThat(awaitItem()).isEqualTo(expectedResponse)
        }

        rideViewModel.estimatedWithSuccess.test {
            assertThat(awaitItem()).isTrue()
        }

        verify(rideRepository).estimate(estimateRequest)
    }

    @Test
    fun `sendConfirmRide should emit success response`() = runTest {
        val rideConfirmRequest = RideConfirmRequest(
            customer_id = "Qualquer1",
            origin = "Qualquer2",
            destination = "Qualquer3",
            distance = 20,
            duration = "",
            driver = Driver(2, "Dominic Toretto"),
            value = 100.09
        )
        val expectedResponse = RideConfirmResponse(success = true)

        Mockito.`when`(rideRepository.confirm(rideConfirmRequest)).thenReturn(
            RideResponseState.Success(
                expectedResponse
            )
        )

        rideViewModel.sendConfirmRide(rideConfirmRequest)

        advanceUntilIdle()

        rideViewModel.rideConfirmed.test {
            assertThat(awaitItem()).isTrue()
        }

        verify(rideRepository).confirm(rideConfirmRequest)
    }
    @Test
    fun `fetchRidesHistory should emit success response`() = runTest {
        val customerId = "CT01"
        val driverId: Int? = null

        val rideRequest = RideRequest(
            customerId = customerId,
            driverId = driverId
        )

        val expectedResponse = RideResponse(
            customerId,
            listOf(
                Ride(
                    id = 1,
                    date = "2024-12-07T13:46:09",
                    origin = "75552 Balistreri Knolls, 498, Yadiraside, 07717-7747",
                    destination = "3246 Jed Center, 672, Lavonnemouth, 31304-4836",
                    distance = 58.910952996119356,
                    duration = "39:21",
                    driver = Driver(id = 1, name = "Homer Simpson"),
                    value = 426.0052584026086
                ),
                Ride(
                    id = 7,
                    date = "2024-11-19T01:28:32",
                    origin = "8161 East Road, 966, Port Gianni, 07023-9667",
                    destination = "466 Watery Lane, 676, Eugeneboro, 64971",
                    distance = 30.398578664107557,
                    duration = "54:39",
                    driver = Driver(id = 2, name = "Dominic Toretto"),
                    value = 902.0833718537956
                ),
                Ride(
                    id = 78,
                    date = "2024-12-30T05:35:17",
                    origin = "985 Maia Lake, 41, Spokane, 38679",
                    destination = "28706 Gerson Causeway, 205, East Abeborough, 77915-6867",
                    distance = 82.15460672501351,
                    duration = "4:14",
                    driver = Driver(id = 1, name = "James Bond"),
                    value = 407.80224909794896
                )
            )
        )
        Mockito.`when`(rideRepository.ride(rideRequest)).thenReturn(
            RideResponseState.Success(
                expectedResponse
            )
        )

        rideViewModel.fetchRidesHistory(customerId, driverId)

        advanceUntilIdle()

        rideViewModel.ridesHistory.test {
            assertThat(awaitItem()).isEqualTo(expectedResponse.rides)
        }

        verify(rideRepository).ride(rideRequest)
    }
}