package com.xunfos.mibhh.repository

import com.xunfos.mibhh.model.Alien
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.ConcurrentHashMap

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


