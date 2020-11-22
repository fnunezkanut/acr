package com.github.fnunezkanut.endpoint.v1

import com.github.fnunezkanut.model.Professor
import com.github.fnunezkanut.service.ProfessorsService
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ProfessorsFetch(
    private val professorsService: ProfessorsService
) {

    private val logger = KotlinLogging.logger {}

    @GetMapping("/v1/professors/{professorUid}")
    fun main(
        @PathVariable("professorUid") professorUid: String
    ): ResponseEntity<Professor> {

        logger.info { "event: fetching professor: $professorUid" }
        val course = professorsService.fetch(uid = professorUid)

        return ResponseEntity(
            course,
            HttpStatus.OK //200
        )
    }
}