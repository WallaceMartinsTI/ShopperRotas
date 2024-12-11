package com.wcsm.shopperrotas.data.repository

import com.google.common.truth.Truth.assertThat
import com.wcsm.shopperrotas.data.api.ShopperAPI
import com.wcsm.shopperrotas.data.dto.ConfirmRideRequest
import com.wcsm.shopperrotas.data.dto.ConfirmRideResponse
import com.wcsm.shopperrotas.data.dto.Driver
import com.wcsm.shopperrotas.data.dto.Location
import com.wcsm.shopperrotas.data.dto.Review
import com.wcsm.shopperrotas.data.dto.Ride
import com.wcsm.shopperrotas.data.dto.RideEstimateRequest
import com.wcsm.shopperrotas.data.dto.RideEstimateResponse
import com.wcsm.shopperrotas.data.dto.RideOption
import com.wcsm.shopperrotas.data.dto.RideResponse
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class RideRepositoryImplTest {

    @Mock
    private lateinit var shopperAPI: ShopperAPI

    private lateinit var rideRepositoryImpl: IRideRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        rideRepositoryImpl = RideRepositoryImpl(shopperAPI)
    }

    @Test
    fun `estimate should return success with no available drivers`() = runTest {
        val estimateRequest = RideEstimateRequest(
            customer_id = "Qualquer1",
            origin = "Qualquer2",
            destination = "Qualquer3"
        )

        val expectedResponse = RideEstimateResponse(
            origin = Location(0.0, 0.0),
            destination = Location(0.0, 0.0),
            distance = 0.0,
            duration = "",
            options = emptyList(),
            routeResponse = Any()
        )

        Mockito.`when`(shopperAPI.getRideEstimate(estimateRequest)).thenReturn(
            expectedResponse
        )

        val response = rideRepositoryImpl.estimate(estimateRequest)
        val drivers = response.options

        assertThat(drivers).isEmpty()

        verify(shopperAPI).getRideEstimate(estimateRequest)
    }

    @Test
    fun `estimate should return success with 3 available drivers`() = runTest {
        val estimateRequest = RideEstimateRequest(
            customer_id = "Qualquer1",
            origin = "Av. Pres. Kenedy, 2385 - Remédios, Osasco - SP, 02675-031",
            destination = "Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200"
        )

        val expectedResponse = RideEstimateResponse(
            origin = Location(0.0, 0.0),
            destination = Location(0.0, 0.0),
            distance = 0.0,
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

        Mockito.`when`(shopperAPI.getRideEstimate(estimateRequest)).thenReturn(
            expectedResponse
        )

        val response = rideRepositoryImpl.estimate(estimateRequest)
        val drivers = response.options

        assertThat(drivers.size).isEqualTo(3)

        verify(shopperAPI).getRideEstimate(estimateRequest)
    }

    @Test
    fun `confirm should return success response`() = runTest {
        val confirmRideRequest = ConfirmRideRequest(
            customer_id = "Qualquer1",
            origin = "Qualquer2",
            destination = "Qualquer3",
            distance = 20.0,
            duration = "",
            driver = Driver(2, "Dominic Toretto"),
            value = 100.09
        )

        val expectedResponse = Response.success(ConfirmRideResponse(success = true))

        Mockito.`when`(shopperAPI.confirmRide(confirmRideRequest)).thenReturn(
            expectedResponse
        )

        val response = rideRepositoryImpl.confirm(confirmRideRequest)

        assertThat(response).isEqualTo(expectedResponse)

        verify(shopperAPI).confirmRide(confirmRideRequest)
    }

    @Test
    fun `confirm should return error response`() = runTest {
        val confirmRideRequest = ConfirmRideRequest(
            customer_id = "Qualquer1",
            origin = "Qualquer2",
            destination = "Qualquer3",
            distance = 20.0,
            duration = "",
            driver = Driver(2, "Dominic Toretto"),
            value = 100.09
        )
        val expectedResponse = Response.error<ConfirmRideResponse>(
            400, okhttp3.ResponseBody.create(null, "Error")
        )

        Mockito.`when`(shopperAPI.confirmRide(confirmRideRequest)).thenReturn(
            expectedResponse
        )

        val response = rideRepositoryImpl.confirm(confirmRideRequest)

        assertThat(response).isEqualTo(expectedResponse)

        verify(shopperAPI).confirmRide(confirmRideRequest)
    }

    @Test
    fun `ride should return success response with rides list`() = runTest {
        val customerId = "CT01"
        val driverId = 1

        val expectedResponse = Response.success(
            RideResponse(
                customer_id = customerId,
                rides = listOf(
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
        )

        Mockito.`when`(shopperAPI.getHistoryRides(customerId, driverId)).thenReturn(
            expectedResponse
        )

        val response = rideRepositoryImpl.ride(customerId, driverId)

        assertThat(response).isEqualTo(expectedResponse)
        assertThat(response.body()?.rides).isNotNull()
        assertThat(response.body()?.rides?.size).isEqualTo(3)
        assertThat(response.body()?.rides?.get(1)?.driver?.name).isEqualTo("Dominic Toretto")

        verify(shopperAPI).getHistoryRides(customerId, driverId)
    }

    @Test
    fun `ride should return empty list`() = runTest {
        val customerId = "CT01"
        val driverId = 1
        val expectedResponse = Response.success(
            RideResponse(customer_id = customerId, rides = emptyList())
        )

        Mockito.`when`(shopperAPI.getHistoryRides(customerId, driverId)).thenReturn(
            expectedResponse
        )

        val response = rideRepositoryImpl.ride(customerId, driverId)

        assertThat(response).isEqualTo(expectedResponse)
        assertThat(response.body()?.rides).isEmpty()

        verify(shopperAPI).getHistoryRides(customerId, driverId)
    }

    @Test
    fun `ride should return error response`() = runTest {
        val customerId = "Qualquer1"
        val driverId = null
        val expectedResponse = Response.error<RideResponse>(
            400, okhttp3.ResponseBody.create(null, "Error")
        )

        Mockito.`when`(shopperAPI.getHistoryRides(customerId, driverId)).thenReturn(
            expectedResponse
        )

        val response = rideRepositoryImpl.ride(customerId, driverId)

        assertThat(response.code()).isEqualTo(400)
        assertThat(response.errorBody()?.string()).isEqualTo("Error")

        verify(shopperAPI).getHistoryRides(customerId, driverId)
    }
}