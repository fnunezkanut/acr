package com.github.fnunezkanut.endpoint.v1

import com.github.fnunezkanut.model.MessageResponse
import com.github.fnunezkanut.service.CoursesService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(
    value = "course operations",
    description = "course operations",
    tags = ["V1 Courses"]
)
class CoursesRegisterStudent(
    private val coursesService: CoursesService
) {

    private val logger = KotlinLogging.logger {}

    @ApiOperation(
        value = "register student to a course",
        response = MessageResponse::class,
        notes = """
## sample request

```
POST /v1/courses/2f09fe95-59e1-4a07-9f86-89b68e5f20eb/students/10035a62e-9285-40a3-8d8b-461f5bbd48b9 HTTP/1.1
```

## sample response

```
HTTP/1.1 201

{
  "message": "student registered to course"
}
```"""
    )
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Registered Student to a Course"),
            ApiResponse(code = 404, message = "No such Student or Course"),
            ApiResponse(code = 500, message = "Failed to register a Student to a Course")
        ]
    )

    @PostMapping("/v1/courses/{courseUid}/students/{studentUid}")
    fun main(
        @PathVariable("courseUid") courseUid: String,
        @PathVariable("studentUid") studentUid: String
    ): ResponseEntity<MessageResponse> {

        logger.info { "event: registering a student $studentUid to a course: $courseUid" }

        coursesService.registerStudent(
            courseUid = courseUid,
            studentUid = studentUid
        )

        return ResponseEntity(
            MessageResponse("student registered to course"),
            HttpStatus.CREATED
        )
    }
}