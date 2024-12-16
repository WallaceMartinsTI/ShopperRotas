package com.wcsm.shopperrotas.data.repository

import com.google.common.truth.Truth.assertThat
import com.wcsm.shopperrotas.data.model.RideConfirmRequest
import com.wcsm.shopperrotas.data.model.RideConfirmResponse
import com.wcsm.shopperrotas.data.model.RideEstimateRequest
import com.wcsm.shopperrotas.data.model.RideRequest
import com.wcsm.shopperrotas.data.model.RideResponse
import com.wcsm.shopperrotas.data.model.RideResponseState
import com.wcsm.shopperrotas.data.remote.api.ShopperAPI
import com.wcsm.shopperrotas.data.remote.dto.Driver
import com.wcsm.shopperrotas.data.remote.dto.Location
import com.wcsm.shopperrotas.data.remote.dto.Review
import com.wcsm.shopperrotas.data.remote.dto.Ride
import com.wcsm.shopperrotas.data.remote.dto.RideEstimate
import com.wcsm.shopperrotas.data.remote.dto.RideOption
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.fail
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
            customerId = "Qualquer1",
            origin = "Qualquer2",
            destination = "Qualquer3"
        )

        val expectedResponse = Response.success(
            RideEstimate(
                origin = Location(0.0, 0.0),
                destination = Location(0.0, 0.0),
                distance = 0,
                duration = "",
                options = emptyList(),
                routeResponse = Any()
            )
        )

        Mockito.`when`(shopperAPI.getRideEstimate(estimateRequest)).thenReturn(
            expectedResponse
        )

        var drivers: List<RideOption>? = null

        when(val response = rideRepositoryImpl.estimate(estimateRequest)) {
            is RideResponseState.Success -> {
                drivers = response.data.options
            }
            is RideResponseState.Error -> {
                fail("Response error.")
            }
        }

        assertThat(drivers).isNotNull()
        assertThat(drivers).isEmpty()
        verify(shopperAPI).getRideEstimate(estimateRequest)
    }

    @Test
    fun `estimate should return success with 3 available drivers`() = runTest {
        val estimateRequest = RideEstimateRequest(
            customerId = "Qualquer1",
            origin = "Av. Pres. Kenedy, 2385 - Remédios, Osasco - SP, 02675-031",
            destination = "Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200"
        )

        val expectedResponse = Response.success(
            RideEstimate(
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
        )

        Mockito.`when`(shopperAPI.getRideEstimate(estimateRequest)).thenReturn(
            expectedResponse
        )

        var drivers: List<RideOption>? = null

        when(val response = rideRepositoryImpl.estimate(estimateRequest)) {
            is RideResponseState.Success -> {
                drivers = response.data.options
            }
            is RideResponseState.Error -> {
                fail("Response error.")
            }
        }

        assertThat(drivers?.size).isEqualTo(3)
        verify(shopperAPI).getRideEstimate(estimateRequest)
    }

    @Test
    fun `estimate should return success with 2 available drivers`() = runTest {
        val estimateRequest = RideEstimateRequest(
            customerId = "Qualquer1",
            origin = "Av. Thomas Edison, 365 - Barra Funda, São Paulo - SP, 01140-000",
            destination = "Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200"
        )

        val expectedResponse = Response.success(
            RideEstimate(
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
                    )
                ),
                routeResponse = Any()
            )
        )

        Mockito.`when`(shopperAPI.getRideEstimate(estimateRequest)).thenReturn(
            expectedResponse
        )

        var drivers: List<RideOption>? = null

        when(val response = rideRepositoryImpl.estimate(estimateRequest)) {
            is RideResponseState.Success -> {
                drivers = response.data.options
            }
            is RideResponseState.Error -> {
                fail("Response error.")
            }
        }

        assertThat(drivers?.size).isEqualTo(2)
        verify(shopperAPI).getRideEstimate(estimateRequest)
    }

    @Test
    fun `estimate should return success with 1 available drivers`() = runTest {
        val estimateRequest = RideEstimateRequest(
            customerId = "Qualquer1",
            origin = "Av. Brasil, 2033 - Jardim America, São Paulo - SP, 01431-001",
            destination = "Av. Paulista, 1538 - Bela Vista, São Paulo - SP, 01310-200"
        )

        val expectedResponse = Response.success(
            RideEstimate(
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
                    )
                ),
                routeResponse = Any()
            )
        )

        Mockito.`when`(shopperAPI.getRideEstimate(estimateRequest)).thenReturn(
            expectedResponse
        )

        var drivers: List<RideOption>? = null

        when(val response = rideRepositoryImpl.estimate(estimateRequest)) {
            is RideResponseState.Success -> {
                drivers = response.data.options
            }
            is RideResponseState.Error -> {
                fail("Response error.")
            }
        }

        assertThat(drivers?.size).isEqualTo(1)
        verify(shopperAPI).getRideEstimate(estimateRequest)
    }

    @Test
    fun `estimate should return error for equal addresses`() = runTest {
        val estimateRequest = RideEstimateRequest(
            customerId = "Qualquer1",
            origin = "Endereço Teste",
            destination = "Endereço Teste"
        )

        val errorResponseJson = """
            {
                "error_code": "INVALID_DATA",
                "error_description": "Mesmo endereço de origem e destino"
            }
        """.trimIndent()
        val responseBody = ResponseBody.create(
            MediaType.parse("application/json"),
            errorResponseJson
        )
        val expectedResponse = Response.error<RideEstimate>(400, responseBody)

        Mockito.`when`(shopperAPI.getRideEstimate(estimateRequest)).thenReturn(
            expectedResponse
        )

        when(val response = rideRepositoryImpl.estimate(estimateRequest)) {
            is RideResponseState.Success -> {
                fail("Expected error response, but got success.")
            }
            is RideResponseState.Error -> {
                assertThat(response.errorMessage).isNotNull()
                assertThat(response.errorMessage).isNotEmpty()
                assertThat(response.errorMessage).isEqualTo("Mesmo endereço de origem e destino")
            }
        }

        verify(shopperAPI).getRideEstimate(estimateRequest)
    }

    @Test
    fun `estimate should return error message for empty destination`() = runTest {
        val estimateRequest = RideEstimateRequest(
            customerId = "Qualquer1",
            origin = "Endereço Teste",
            destination = ""
        )

        val errorResponseJson = """
            {
                "error_code": "INVALID_DATA",
                "error_description": "Sem endereço de destino"
            }
        """.trimIndent()
        val responseBody = ResponseBody.create(
            MediaType.parse("application/json"),
            errorResponseJson
        )
        val expectedResponse = Response.error<RideEstimate>(400, responseBody)

        Mockito.`when`(shopperAPI.getRideEstimate(estimateRequest)).thenReturn(
            expectedResponse
        )

        when(val response = rideRepositoryImpl.estimate(estimateRequest)) {
            is RideResponseState.Success -> {
                fail("Expected error response, but got success.")
            }
            is RideResponseState.Error -> {
                assertThat(response.errorMessage).isNotNull()
                assertThat(response.errorMessage).isNotEmpty()
                assertThat(response.errorMessage).isEqualTo("Sem endereço de destino")
            }
        }

        verify(shopperAPI).getRideEstimate(estimateRequest)
    }

    @Test
    fun `estimate should return error message for empty origin`() = runTest {
        val estimateRequest = RideEstimateRequest(
            customerId = "Qualquer1",
            origin = "",
            destination = "Endereço Teste"
        )

        val errorResponseJson = """
            {
                "error_code": "INVALID_DATA",
                "error_description": "Sem endereço de origem"
            }
        """.trimIndent()
        val responseBody = ResponseBody.create(
            MediaType.parse("application/json"),
            errorResponseJson
        )
        val expectedResponse = Response.error<RideEstimate>(400, responseBody)

        Mockito.`when`(shopperAPI.getRideEstimate(estimateRequest)).thenReturn(
            expectedResponse
        )

        when(val response = rideRepositoryImpl.estimate(estimateRequest)) {
            is RideResponseState.Success -> {
                fail("Expected error response, but got success.")
            }
            is RideResponseState.Error -> {
                assertThat(response.errorMessage).isNotNull()
                assertThat(response.errorMessage).isNotEmpty()
                assertThat(response.errorMessage).isEqualTo("Sem endereço de origem")
            }
        }

        verify(shopperAPI).getRideEstimate(estimateRequest)
    }

    @Test
    fun `estimate should return error message for blank client id`() = runTest {
        val estimateRequest = RideEstimateRequest(
            customerId = "",
            origin = "Endereço Teste",
            destination = "Endereço Teste"
        )

        val errorResponseJson = """
            {
                "error_code": "INVALID_DATA",
                "error_description": "sem id de cliente"
            }
        """.trimIndent()
        val responseBody = ResponseBody.create(
            MediaType.parse("application/json"),
            errorResponseJson
        )
        val expectedResponse = Response.error<RideEstimate>(400, responseBody)

        Mockito.`when`(shopperAPI.getRideEstimate(estimateRequest)).thenReturn(
            expectedResponse
        )

        when(val response = rideRepositoryImpl.estimate(estimateRequest)) {
            is RideResponseState.Success -> {
                fail("Expected error response, but got success.")
            }
            is RideResponseState.Error -> {
                assertThat(response.errorMessage).isNotNull()
                assertThat(response.errorMessage).isNotEmpty()
                assertThat(response.errorMessage).isEqualTo("sem id de cliente")
            }
        }

        verify(shopperAPI).getRideEstimate(estimateRequest)
    }

    @Test
    fun `confirm should return success response`() = runTest {
        val rideConfirmRequest = RideConfirmRequest(
            customerId = "Qualquer1",
            origin = "Qualquer2",
            destination = "Qualquer3",
            distance = 20,
            duration = "",
            driver = Driver(2, "Dominic Toretto"),
            value = 100.09
        )

        val expectedResponse = Response.success(RideConfirmResponse(success = true))

        Mockito.`when`(shopperAPI.confirmRide(rideConfirmRequest)).thenReturn(
            expectedResponse
        )

        when(val response = rideRepositoryImpl.confirm(rideConfirmRequest)) {
            is RideResponseState.Success -> {
                assertThat(response.data.success).isNotNull()
                assertThat(response.data.success).isTrue()
            }
            is RideResponseState.Error -> {
                fail("Response error.")
            }
        }

        verify(shopperAPI).confirmRide(rideConfirmRequest)
    }

    @Test
    fun `confirm should return error message for invalid distance for this driver`() = runTest {
        val rideConfirmRequest = RideConfirmRequest(
            customerId = "Qualquer1",
            origin = "Qualquer2",
            destination = "Qualquer3",
            distance = 0,
            duration = "",
            driver = Driver(2, "Dominic Toretto"),
            value = 100.09
        )

        val errorResponseJson = """
            {
                "error_code": "INVALID_DISTANCE",
                "error_description": "quilometragem inválida para o motorista"
            }
        """.trimIndent()
        val responseBody = ResponseBody.create(
            MediaType.parse("application/json"),
            errorResponseJson
        )
        val expectedResponse = Response.error<RideConfirmResponse>(400, responseBody)

        Mockito.`when`(shopperAPI.confirmRide(rideConfirmRequest)).thenReturn(
            expectedResponse
        )

        when(val response = rideRepositoryImpl.confirm(rideConfirmRequest)) {
            is RideResponseState.Success -> {
                fail("Expected error response, but got success.")
            }
            is RideResponseState.Error -> {
                assertThat(response.errorMessage).isNotNull()
                assertThat(response.errorMessage).isNotEmpty()
                assertThat(response.errorMessage).isEqualTo("quilometragem inválida para o motorista")
            }
        }

        verify(shopperAPI).confirmRide(rideConfirmRequest)
    }

    @Test
    fun `confirm should return error message for invalid driver`() = runTest {
        val rideConfirmRequest = RideConfirmRequest(
            customerId = "Qualquer1",
            origin = "Qualquer2",
            destination = "Qualquer3",
            distance = 20,
            duration = "",
            driver = Driver(0, "Dominic Toretto"),
            value = 100.09
        )

        val errorResponseJson = """
            {
                "error_code": "DRIVER_NOT_FOUND",
                "error_description": "Motorista não encontrado"
            }
        """.trimIndent()
        val responseBody = ResponseBody.create(
            MediaType.parse("application/json"),
            errorResponseJson
        )
        val expectedResponse = Response.error<RideConfirmResponse>(400, responseBody)

        Mockito.`when`(shopperAPI.confirmRide(rideConfirmRequest)).thenReturn(
            expectedResponse
        )

        when(val response = rideRepositoryImpl.confirm(rideConfirmRequest)) {
            is RideResponseState.Success -> {
                fail("Expected error response, but got success.")
            }
            is RideResponseState.Error -> {
                assertThat(response.errorMessage).isNotNull()
                assertThat(response.errorMessage).isNotEmpty()
                assertThat(response.errorMessage).isEqualTo("Motorista não encontrado")
            }
        }

        verify(shopperAPI).confirmRide(rideConfirmRequest)
    }

    @Test
    fun `confirm should return error message for driver not informed`() = runTest {
        val rideConfirmRequest = RideConfirmRequest(
            customerId = "Qualquer1",
            origin = "Qualquer2",
            destination = "Qualquer3",
            distance = 20,
            duration = "",
            driver = Driver(null, "Dominic Toretto"),
            value = 100.09
        )

        val errorResponseJson = """
            {
                "error_code": "INVALID_DATA",
                "error_description": "Sem motorista selecionado"
            }
        """.trimIndent()
        val responseBody = ResponseBody.create(
            MediaType.parse("application/json"),
            errorResponseJson
        )
        val expectedResponse = Response.error<RideConfirmResponse>(400, responseBody)

        Mockito.`when`(shopperAPI.confirmRide(rideConfirmRequest)).thenReturn(
            expectedResponse
        )

        when(val response = rideRepositoryImpl.confirm(rideConfirmRequest)) {
            is RideResponseState.Success -> {
                fail("Expected error response, but got success.")
            }
            is RideResponseState.Error -> {
                assertThat(response.errorMessage).isNotNull()
                assertThat(response.errorMessage).isNotEmpty()
                assertThat(response.errorMessage).isEqualTo("Sem motorista selecionado")
            }
        }

        verify(shopperAPI).confirmRide(rideConfirmRequest)
    }

    @Test
    fun `confirm should return error message for equal addresses`() = runTest {
        val rideConfirmRequest = RideConfirmRequest(
            customerId = "Qualquer1",
            origin = "Endereço Teste",
            destination = "Endereço Teste",
            distance = 20,
            duration = "",
            driver = Driver(2, "Dominic Toretto"),
            value = 100.09
        )

        val errorResponseJson = """
            {
                "error_code": "INVALID_DATA",
                "error_description": "Mesmo endereço de origem e destino"
            }
        """.trimIndent()
        val responseBody = ResponseBody.create(
            MediaType.parse("application/json"),
            errorResponseJson
        )
        val expectedResponse = Response.error<RideConfirmResponse>(400, responseBody)

        Mockito.`when`(shopperAPI.confirmRide(rideConfirmRequest)).thenReturn(
            expectedResponse
        )

        when(val response = rideRepositoryImpl.confirm(rideConfirmRequest)) {
            is RideResponseState.Success -> {
                fail("Expected error response, but got success.")
            }
            is RideResponseState.Error -> {
                assertThat(response.errorMessage).isNotNull()
                assertThat(response.errorMessage).isNotEmpty()
                assertThat(response.errorMessage).isEqualTo("Mesmo endereço de origem e destino")
            }
        }

        verify(shopperAPI).confirmRide(rideConfirmRequest)
    }

    @Test
    fun `confirm should return error message for empty destination`() = runTest {
        val rideConfirmRequest = RideConfirmRequest(
            customerId = "Qualquer1",
            origin = "Endereço Teste",
            destination = "",
            distance = 20,
            duration = "",
            driver = Driver(2, "Dominic Toretto"),
            value = 100.09
        )

        val errorResponseJson = """
            {
                "error_code": "INVALID_DATA",
                "error_description": "Sem endereço de destino"
            }
        """.trimIndent()
        val responseBody = ResponseBody.create(
            MediaType.parse("application/json"),
            errorResponseJson
        )
        val expectedResponse = Response.error<RideConfirmResponse>(400, responseBody)

        Mockito.`when`(shopperAPI.confirmRide(rideConfirmRequest)).thenReturn(
            expectedResponse
        )

        when(val response = rideRepositoryImpl.confirm(rideConfirmRequest)) {
            is RideResponseState.Success -> {
                fail("Expected error response, but got success.")
            }
            is RideResponseState.Error -> {
                assertThat(response.errorMessage).isNotNull()
                assertThat(response.errorMessage).isNotEmpty()
                assertThat(response.errorMessage).isEqualTo("Sem endereço de destino")
            }
        }

        verify(shopperAPI).confirmRide(rideConfirmRequest)
    }

    @Test
    fun `confirm should return error message for empty origin`() = runTest {
        val rideConfirmRequest = RideConfirmRequest(
            customerId = "Qualquer1",
            origin = "",
            destination = "Endereço Teste",
            distance = 20,
            duration = "",
            driver = Driver(2, "Dominic Toretto"),
            value = 100.09
        )

        val errorResponseJson = """
            {
                "error_code": "INVALID_DATA",
                "error_description": "Sem endereço de origem"
            }
        """.trimIndent()
        val responseBody = ResponseBody.create(
            MediaType.parse("application/json"),
            errorResponseJson
        )
        val expectedResponse = Response.error<RideConfirmResponse>(400, responseBody)

        Mockito.`when`(shopperAPI.confirmRide(rideConfirmRequest)).thenReturn(
            expectedResponse
        )

        when(val response = rideRepositoryImpl.confirm(rideConfirmRequest)) {
            is RideResponseState.Success -> {
                fail("Expected error response, but got success.")
            }
            is RideResponseState.Error -> {
                assertThat(response.errorMessage).isNotNull()
                assertThat(response.errorMessage).isNotEmpty()
                assertThat(response.errorMessage).isEqualTo("Sem endereço de origem")
            }
        }

        verify(shopperAPI).confirmRide(rideConfirmRequest)
    }

    @Test
    fun `confirm should return error message for blank client id`() = runTest {
        val rideConfirmRequest = RideConfirmRequest(
            customerId = null,
            origin = "Qualquer1",
            destination = "Qualquer2",
            distance = 20,
            duration = "",
            driver = Driver(2, "Dominic Toretto"),
            value = 100.09
        )

        val errorResponseJson = """
            {
                "error_code": "INVALID_DATA",
                "error_description": "Sem id do usuário"
            }
        """.trimIndent()
        val responseBody = ResponseBody.create(
            MediaType.parse("application/json"),
            errorResponseJson
        )
        val expectedResponse = Response.error<RideConfirmResponse>(400, responseBody)

        Mockito.`when`(shopperAPI.confirmRide(rideConfirmRequest)).thenReturn(
            expectedResponse
        )

        when(val response = rideRepositoryImpl.confirm(rideConfirmRequest)) {
            is RideResponseState.Success -> {
                fail("Expected error response, but got success.")
            }
            is RideResponseState.Error -> {
                assertThat(response.errorMessage).isNotNull()
                assertThat(response.errorMessage).isNotEmpty()
                assertThat(response.errorMessage).isEqualTo("Sem id do usuário")
            }
        }

        verify(shopperAPI).confirmRide(rideConfirmRequest)
    }

    @Test
    fun `ride should return success response with rides list`() = runTest {
        val customerId = "CT01"
        val driverId = null

        val rideRequest = RideRequest(
            customerId = customerId,
            driverId = driverId
        )

        val expectedResponse = Response.success(
            RideResponse(
                customerId = customerId,
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

        var rides: List<Ride>? = null

        when(val response = rideRepositoryImpl.ride(rideRequest)) {
            is RideResponseState.Success -> {
                rides = response.data.rides
            }
            is RideResponseState.Error -> {
                fail("Response error.")
            }
        }

        assertThat(rides?.size).isEqualTo(3)
        verify(shopperAPI).getHistoryRides(customerId, driverId)
    }

    @Test
    fun `ride should return erro message for no rides saved`() = runTest {
        val customerId = "Qualquer1"
        val driverId = null

        val rideRequest = RideRequest(
            customerId = customerId,
            driverId = driverId
        )

        val errorResponseJson = """
            {
                "error_code": "NO_RIDES_FOUND",
                "error_description": "sem corridas salvas"
            }
        """.trimIndent()
        val responseBody = ResponseBody.create(
            MediaType.parse("application/json"),
            errorResponseJson
        )
        val expectedResponse = Response.error<RideResponse>(400, responseBody)

        Mockito.`when`(shopperAPI.getHistoryRides(customerId, driverId)).thenReturn(
            expectedResponse
        )

        when(val response = rideRepositoryImpl.ride(rideRequest)) {
            is RideResponseState.Success -> {
                fail("Expected error response, but got success.")
            }
            is RideResponseState.Error -> {
                assertThat(response.errorMessage).isNotNull()
                assertThat(response.errorMessage).isNotEmpty()
                assertThat(response.errorMessage).isEqualTo("sem corridas salvas")
            }
        }

        verify(shopperAPI).getHistoryRides(customerId, driverId)
    }

    @Test
    fun `ride should return erro message for invalid driver`() = runTest {
        val customerId = "Qualquer1"
        val driverId = null

        val rideRequest = RideRequest(
            customerId = customerId,
            driverId = driverId
        )

        val errorResponseJson = """
            {
                "error_code": "INVALID_DRIVER",
                "error_description": "motorista inválido"
            }
        """.trimIndent()
        val responseBody = ResponseBody.create(
            MediaType.parse("application/json"),
            errorResponseJson
        )
        val expectedResponse = Response.error<RideResponse>(400, responseBody)

        Mockito.`when`(shopperAPI.getHistoryRides(customerId, driverId)).thenReturn(
            expectedResponse
        )

        when(val response = rideRepositoryImpl.ride(rideRequest)) {
            is RideResponseState.Success -> {
                fail("Expected error response, but got success.")
            }
            is RideResponseState.Error -> {
                assertThat(response.errorMessage).isNotNull()
                assertThat(response.errorMessage).isNotEmpty()
                assertThat(response.errorMessage).isEqualTo("motorista inválido")
            }
        }

        verify(shopperAPI).getHistoryRides(customerId, driverId)
    }
}