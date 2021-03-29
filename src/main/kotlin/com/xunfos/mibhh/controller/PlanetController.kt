package com.xunfos.mibhh.controller

import com.xunfos.mibhh.repository.NamesRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("planet")
class PlanetController(
    private val namesRepository: NamesRepository,
) {

    @Operation(summary = "Get all planets mapped")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "List of available planes", content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = String::class))
                    )
                ]
            ),
        ]
    )
    @GetMapping
    suspend fun list(): List<String> = namesRepository.allPlanets()
}
