package com.xunfos.mibhh

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class MibhhApplication

fun main(args: Array<String>) {
    runApplication<MibhhApplication>(*args)
}
