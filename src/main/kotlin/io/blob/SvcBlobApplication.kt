package io.blob

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SvcBlobApplication

fun main(args: Array<String>) {

    runApplication<SvcBlobApplication>(*args)
}
