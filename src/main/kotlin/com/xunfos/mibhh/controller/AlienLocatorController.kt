package com.xunfos.mibhh.controller

import com.xunfos.mibhh.model.Alien
import com.xunfos.mibhh.repository.AlienRepository
import com.xunfos.mibhh.repository.LocationRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("alien")
class AlienLocatorController(
    private val alienRepository: AlienRepository,
    private val locationRepository: LocationRepository
) {

    @Operation(summary = "Get all aliens by City")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found the city and its aliens", content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = Alien::class))
                    )
                ]
            ),
            ApiResponse(responseCode = "404", description = "City not found", content = [Content()])
        ]
    )
    @GetMapping("/city/{city}")
    suspend fun byCity(
        @Parameter(description = "city name in which to list the aliens (e.g: Hamburg)")
        @PathVariable city: String
    ): List<Alien> {
        val location =
            locationRepository.getCityByNameOrNull(city) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

        return alienRepository
            .getAsSequence()
            .map { it.value }
            .filter { it.currentLocation.city == location.id }
            .toList()
    }

    @Operation(summary = "Get all aliens by origin planet")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found the aliens by planet", content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = Alien::class))
                    )
                ]
            )
        ]
    )
    @GetMapping("/planet/{planet}")
    suspend fun byPlanet(
        @Parameter(description = "Planet to find aliens from")
        @PathVariable planet: String
    ): List<Alien> = alienRepository
        .getAsSequence()
        .map { it.value }
        .filter { it.planetOfOrigin.equals(planet, ignoreCase = true) }
        .toList()

}
