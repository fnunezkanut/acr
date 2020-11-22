package com.github.fnunezkanut.endpoint.v1

import com.github.fnunezkanut.model.Course
import com.github.fnunezkanut.service.CoursesService
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class CoursesFetch(
    private val coursesService: CoursesService
) {

    private val logger = KotlinLogging.logger {}

    @GetMapping("/v1/courses/{courseUid}")
    fun main(
        @PathVariable("courseUid") courseUid: String
    ): ResponseEntity<Course> {

        logger.info { "event: fetching course: $courseUid" }
        val course = coursesService.fetch(uid = courseUid)

        return ResponseEntity(
            course,
            HttpStatus.OK //200
        )
    }
}