package com.github.fnunezkanut.endpoint.v1

import com.github.fnunezkanut.model.Professor
import com.github.fnunezkanut.service.ProfessorsService
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProfessorsCreate(
    private val professorsService: ProfessorsService
) {

    private val logger = KotlinLogging.logger {}

    @PostMapping("/v1/professors")
    fun main(
        @RequestBody createRequest: Professor
    ): ResponseEntity<Professor> {

        logger.info { "event: creating new professor: $createRequest" }

        //validate incoming user data and create the professor
        val validatedProfessor = professorsService.validate(
            unvalidated = createRequest
        )
        val createdProfessor = professorsService.create(
            validated = validatedProfessor
        )

        return ResponseEntity(
            createdProfessor,
            HttpStatus.CREATED //201
        )
    }
}