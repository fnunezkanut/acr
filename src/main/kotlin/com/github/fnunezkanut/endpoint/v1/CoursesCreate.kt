package com.github.fnunezkanut.endpoint.v1

import com.github.fnunezkanut.model.Course
import com.github.fnunezkanut.service.CoursesService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(
    value = "course operations",
    description = "course operations",
    tags = ["V1 Courses"]
)
class CoursesCreate(
    private val coursesService: CoursesService
) {

    private val logger = KotlinLogging.logger {}

    @ApiOperation(
        value = "creates a course",
        response = Course::class,
        notes = """
## sample request

```
POST /v1/courses HTTP/1.1
Content-Type: application/json; charset=utf-8

{"name":"Programming 4","code":"CT-401"}
```

## sample response

```
HTTP/1.1 201

{
  "uid": "67c2c77d-dd3c-4459-a873-2ff2e6215c7d",
  "code": "CT-401",
  "name": "Programming 4"
}
```"""
    )
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Created"),
            ApiResponse(code = 400, message = "Invalid input"),
            ApiResponse(code = 409, message = "Course already exists"),
            ApiResponse(code = 500, message = "Failed to create Course")
        ]
    )

    @PostMapping("/v1/courses")
    fun main(
        @RequestBody createRequest: Course
    ): ResponseEntity<Course> {

        logger.info { "event: creating new course: $createRequest" }

        //validate incoming user data and create the course
        val validatedCourse = coursesService.validate(
            unvalidated = createRequest
        )
        val createdCourse = coursesService.create(
            validated = validatedCourse
        )

        return ResponseEntity(
            createdCourse,
            HttpStatus.CREATED //201
        )
    }
}