package com.github.fnunezkanut.endpoint.v1

import com.github.fnunezkanut.model.Student
import com.github.fnunezkanut.service.StudentsService
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
    value = "students operations",
    description = "students operations",
    tags = ["V1 Students"]
)
class StudentsFetch(
    private val studentsService: StudentsService
) {

    private val logger = KotlinLogging.logger {}

    @ApiOperation(
        value = "fetches a student",
        response = Student::class,
        notes = """
## sample request

```
GET /v1/students/ec683dd6-34ff-4151-b003-574102ea4b10 HTTP/1.1
```

## sample response

```
HTTP/1.1 200

{
  "uid": "ec683dd6-34ff-4151-b003-574102ea4b10",
  "code": "00000004",
  "firstName": "stu",
  "lastName": "4"
}
```"""
    )
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "OK"),
            ApiResponse(code = 404, message = "No such Student")
        ]
    )

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