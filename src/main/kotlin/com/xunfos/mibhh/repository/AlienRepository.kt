package com.xunfos.mibhh.repository

import com.xunfos.mibhh.model.Alien
import com.xunfos.mibhh.model.AlienStatus
import com.xunfos.mibhh.model.Location
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.util.ResourceUtils
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

@Repository
class AlienRepository {
    private val repository = ConcurrentHashMap<UUID, Alien>()

    fun clearRepository() = repository.clear()

    fun put(alien: Alien) {
        repository[alien.id] = alien
    }

    fun list() = repository.elements().toList()
    fun getAsSequence() = repository.asSequence()
}

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
}


@Component
class AlienRepositoryGenerationScheduler(
    private val alienRepository: AlienRepository,
    private val locationRepository: LocationRepository,
    private val namesRepository: NamesRepository
) {
    private val rng = Random(System.currentTimeMillis())
    private val hamburgCityRange =
        locationRepository.getCityByNameOrNull("HAMBURG") ?: error("Hamburg somehow not found")

    // Execute every minute
    @Scheduled(fixedDelay = 60_000)
    fun fillUpRepository() {
        println("clearing repository")
        alienRepository.clearRepository()

        println("Filling up repository with Hamburg aliens!")
        val amount = rng.nextInt(20, 220)

        println("Topping up with $amount items!")

        (1..amount).asSequence()
            .map { generateAlien(hamburgCityRange) }
            .forEach { alienRepository.put(it) }

        println("Finished filling up repository successfully!")
    }

    private fun generateAlien(cityRange: CityRange): Alien {
        val (latitude, longitude) = cityRange.getRandomPointInRange()
        return Alien(
            id = UUID.randomUUID(),
            name = namesRepository.randomAlienName(),
            planetOfOrigin = namesRepository.randomPlanetName(),
            status = AlienStatus.values().random(),
            currentLocation = Location(city = cityRange.id, latitude = latitude, longitude = longitude)
        )
    }
}
