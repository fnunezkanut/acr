package com.github.fnunezkanut.endpoint.v1

import com.github.fnunezkanut.model.Student
import com.github.fnunezkanut.service.StudentsService
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class StudentsCreate(
    private val studentsService: StudentsService
) {

    private val logger = KotlinLogging.logger {}

    @PostMapping("/v1/students")
    fun main(
        @RequestBody createRequest: Student
    ): ResponseEntity<Student> {

        logger.info { "event: creating new student: $createRequest" }

        //validate incoming user data and create the professor
        val validatedProfessor = studentsService.validate(
            unvalidated = createRequest
        )
        val createdStudent = studentsService.create(
            validated = validatedProfessor
        )

        return ResponseEntity(
            createdStudent,
            HttpStatus.CREATED //201
        )
    }
}