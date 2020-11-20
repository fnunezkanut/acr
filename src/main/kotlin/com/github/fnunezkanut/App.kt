package com.github.fnunezkanut

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.*
import javax.annotation.PostConstruct

//entry point into this springboot2 app
fun main(args: Array<String>) {
    runApplication<App>(*args)
}

@SpringBootApplication
class App {

    //ensure when running locally we are on UTC, so any timestamps in postgres are UTC
    @PostConstruct
    fun started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    /* config beans below */
}