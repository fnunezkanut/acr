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
class CoursesAssignProfessor(
    private val coursesService: CoursesService
) {

    private val logger = KotlinLogging.logger {}

    @ApiOperation(
        value = "assign professor to a course",
        response = MessageResponse::class,
        notes = """
## sample request

```
POST /v1/courses/2f09fe95-59e1-4a07-9f86-89b68e5f20eb/professors/19c9165d-4945-465a-b644-bde0aa22f29a HTTP/1.1
```

## sample response

```
HTTP/1.1 201

{
  "message": "professor assigned to course"
}
```"""
    )
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Assigned Professor to a Course"),
            ApiResponse(code = 404, message = "No such Professor or Course"),
            ApiResponse(code = 500, message = "Failed to assign a Professor to a Course")
        ]
    )

    @PostMapping("/v1/courses/{courseUid}/professors/{professorUid}")
    fun main(
        @PathVariable("courseUid") courseUid: String,
        @PathVariable("professorUid") professorUid: String
    ): ResponseEntity<MessageResponse> {

        logger.info { "event: assigning a professor $professorUid to a course: $courseUid" }

        coursesService.assignProfessor(
            courseUid = courseUid,
            professorUid = professorUid
        )

        return ResponseEntity(
            MessageResponse("professor assigned to course"),
            HttpStatus.CREATED
        )
    }
}