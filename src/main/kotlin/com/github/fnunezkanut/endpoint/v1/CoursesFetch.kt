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
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(
    value = "course operations",
    description = "course operations",
    tags = ["V1 Courses"]
)
class CoursesFetch(
    private val coursesService: CoursesService
) {

    private val logger = KotlinLogging.logger {}

    @ApiOperation(
        value = "fetches a course",
        response = Course::class,
        notes = """
## sample request

```
GET /v1/courses/2f09fe95-59e1-4a07-9f86-89b68e5f20eb HTTP/1.1
```

## sample response

```
HTTP/1.1 200

{
  "uid": "2f09fe95-59e1-4a07-9f86-89b68e5f20eb",
  "code": "CT-101",
  "name": "Programming 1"
}
```"""
    )
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "OK"),
            ApiResponse(code = 404, message = "No such Course")
        ]
    )


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