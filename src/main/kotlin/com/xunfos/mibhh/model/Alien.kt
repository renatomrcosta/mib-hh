package com.xunfos.mibhh.model

import java.util.*

data class Alien(
    val id: UUID,
    val name: String,
    val planetOfOrigin: String,
    val status: AlienStatus,
    val currentLocation: Location
)
