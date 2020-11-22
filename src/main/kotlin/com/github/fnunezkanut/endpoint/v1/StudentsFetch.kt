package com.github.fnunezkanut.endpoint.v1

import com.github.fnunezkanut.model.Student
import com.github.fnunezkanut.service.StudentsService
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class StudentsFetch(
    private val studentsService: StudentsService
) {

    private val logger = KotlinLogging.logger {}

    @GetMapping("/v1/students/{studentUid}")
    fun main(
        @PathVariable("studentUid") studentUid: String
    ): ResponseEntity<Student> {

        logger.info { "event: fetching student: $studentUid" }
        val course = studentsService.fetch(uid = studentUid)

        return ResponseEntity(
            course,
            HttpStatus.OK //200
        )
    }
}