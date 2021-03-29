package com.xunfos.mibhh.config

import com.xunfos.mibhh.model.Alien
import com.xunfos.mibhh.model.AlienStatus
import com.xunfos.mibhh.model.Location
import com.xunfos.mibhh.repository.AlienRepository
import com.xunfos.mibhh.repository.CityRange
import com.xunfos.mibhh.repository.LocationRepository
import com.xunfos.mibhh.repository.NamesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import java.util.*
import kotlin.random.Random

@Configuration
class SchedulerGeneratorConfig(
    private val alienRepository: AlienRepository,
    private val locationRepository: LocationRepository,
    private val namesRepository: NamesRepository,
) {
    @Bean
    fun alienRepositoryGenerationScheduler(): AlienRepositoryGenerationScheduler = AlienRepositoryGenerationScheduler(
        alienRepository = alienRepository,
        locationRepository = locationRepository,
        namesRepository = namesRepository,
        cities = listOf("Hamburg")
    )
}


class AlienRepositoryGenerationScheduler(
    private val alienRepository: AlienRepository,
    private val locationRepository: LocationRepository,
    private val namesRepository: NamesRepository,
    private val cities: List<String>
) {
    private val rng = Random(System.currentTimeMillis())

    // Execute every minute
    @Scheduled(fixedDelay = 60_000)
    fun fillUpRepository() = runBlocking {
        println("clearing repository")
        alienRepository.clearRepository()

        cities
            .asFlow()
            .flowOn(Dispatchers.IO)
            .collect { city ->
                println("Filling up repository with $city aliens!")
                val amount = rng.nextInt(20, 220)
                println("Topping up with $amount items!")
                locationRepository.getCityByNameOrNull(city = city)?.let { cityRange ->
                    (1..amount).asSequence()
                        .map { generateAlien(cityRange) }
                        .forEach { alienRepository.put(it) }
                } ?: println("City boundaries for $city not found")
            }
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
