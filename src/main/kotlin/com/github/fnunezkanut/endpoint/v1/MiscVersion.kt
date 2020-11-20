package com.github.fnunezkanut.endpoint.v1

import com.github.fnunezkanut.model.V1MiscVersionResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MiscVersion {

    @GetMapping("/v1/version")
    fun main(): V1MiscVersionResponse {

        val version = "TODO"

        return V1MiscVersionResponse(
            version = version
        )
    }
}