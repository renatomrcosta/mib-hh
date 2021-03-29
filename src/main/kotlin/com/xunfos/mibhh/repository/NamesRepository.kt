package com.xunfos.mibhh.repository

import org.springframework.stereotype.Repository
import org.springframework.util.ResourceUtils

@Repository
class NamesRepository {
    private val alienNames by lazy {
        ResourceUtils.getFile("classpath:alien_names.txt")
            .readText()
            .parse()
    }

    private val planetNames by lazy {
        ResourceUtils.getFile("classpath:planet_names.txt")
            .readText()
            .parse()
    }

    fun randomAlienName() = alienNames.random()
    fun randomPlanetName() = planetNames.random()

    private fun String.parse() =
        this.split("\n")
            .filter { it.isNotBlank() }

    fun allPlanets(): List<String> = planetNames
}
