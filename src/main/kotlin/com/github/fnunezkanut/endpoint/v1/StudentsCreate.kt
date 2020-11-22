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
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(
    value = "students operations",
    description = "students operations",
    tags = ["V1 Students"]
)
class StudentsCreate(
    private val studentsService: StudentsService
) {

    private val logger = KotlinLogging.logger {}

    @ApiOperation(
        value = "creates a student",
        response = Student::class,
        notes = """
## sample request

```
POST /v1/students HTTP/1.1

{"firstName":"stu","lastName":"4","code":"00000004"}
```

## sample response

```
HTTP/1.1 201

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
            ApiResponse(code = 201, message = "Created"),
            ApiResponse(code = 400, message = "Invalid input"),
            ApiResponse(code = 409, message = "Student already exists"),
            ApiResponse(code = 500, message = "Failed to create Student")
        ]
    )

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