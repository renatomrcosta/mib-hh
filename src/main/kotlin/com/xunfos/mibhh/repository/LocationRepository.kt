package com.xunfos.mibhh.repository

import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

@Repository
class LocationRepository {
    private val repository = ConcurrentHashMap<String, CityRange>()

    init {
        repository["HAMBURG"] = CityRange(
            id = "HAMBURG",
            latitudeRange = 53.4169 .. 53.7165,
            longitudeRange = 9.7040 .. 10.2179,
        )
    }

    fun getCityByNameOrNull(city: String) = repository[city.toUpperCase()]
}

data class CityRange(
    val id: String,
    val latitudeRange: ClosedFloatingPointRange<Double>,
    val longitudeRange: ClosedFloatingPointRange<Double>,
) {
    private val rng = Random(System.currentTimeMillis())

    fun getRandomPointInRange(): Pair<Double, Double> =
        rng.nextDouble(latitudeRange.start, latitudeRange.endInclusive) to
        rng.nextDouble(longitudeRange.start, longitudeRange.endInclusive)
}
