package com.xunfos.mibhh.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("alien")
class AlienLocatorController {

    @Operation(summary = "Get all aliens by City")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Found the city and its aliens", content = [
            Content(mediaType = "application/json", array = ArraySchema(schema = Schema(implementation = Alien::class)))
        ]),
        ApiResponse(responseCode = "404", description = "City not found", content = [Content()])
    ])
    @GetMapping("/{city}")
    suspend fun byCity(
        @Parameter(description = "city name in which to list the aliens (e.g: Hamburg)")
        @PathVariable city: String
    ): List<Alien> {
        return listOf()
    }

    @Operation(summary = "Get all aliens by origin planet")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Found the aliens by planet", content = [
            Content(mediaType = "application/json", array = ArraySchema(schema = Schema(implementation = Alien::class)))
        ]),
        ApiResponse(responseCode = "404", description = "Planet not found", content = [Content()])
    ])
    @GetMapping("/{planetId}")
    suspend fun byPlanet(
        @Parameter(description = "Id of the planet to find aliens from")
        @PathVariable city: String
    ): List<Alien> {
        return listOf()
    }
}

data class Alien(
    val id: UUID,
    val name: String,
    val planetOfOrigin: Planet,
    val status: AlienStatus,
    val currentLocation: Location
)

data class Location(val latitude: Double, val longitude: Double)

data class Planet(
    val id: UUID,
    val name: String,
)
enum class AlienStatus { REGISTERED, ALIEN_OF_INTEREST, FUGITIVE }
