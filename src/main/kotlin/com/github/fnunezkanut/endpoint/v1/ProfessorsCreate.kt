package com.github.fnunezkanut.endpoint.v1

import com.github.fnunezkanut.model.Professor
import com.github.fnunezkanut.service.ProfessorsService
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
    value = "professor operations",
    description = "professor operations",
    tags = ["V1 Professors"]
)
class ProfessorsCreate(
    private val professorsService: ProfessorsService
) {

    private val logger = KotlinLogging.logger {}

    @ApiOperation(
        value = "creates a professor",
        response = Professor::class,
        notes = """
## sample request

```
POST /v1/professors HTTP/1.1
Content-Type: application/json; charset=utf-8

{"firstName":"prof","lastName":"4","code":"0000004"}
```

## sample response

```
HTTP/1.1 201

{
  "uid": "2a174df9-bffe-4ac2-931b-48ace3b3cd6a",
  "code": "0000004",
  "firstName": "prof",
  "lastName": "4"
}
```"""
    )
    @ApiResponses(
        value = [
            ApiResponse(code = 201, message = "Created"),
            ApiResponse(code = 400, message = "Invalid input"),
            ApiResponse(code = 409, message = "Professor already exists"),
            ApiResponse(code = 500, message = "Failed to create Professor")
        ]
    )

    @PostMapping("/v1/professors")
    fun main(
        @RequestBody createRequest: Professor
    ): ResponseEntity<Professor> {

        logger.info { "event: creating new professor: $createRequest" }

        //validate incoming user data and create the professor
        val validatedProfessor = professorsService.validate(
            unvalidated = createRequest
        )
        val createdProfessor = professorsService.create(
            validated = validatedProfessor
        )

        return ResponseEntity(
            createdProfessor,
            HttpStatus.CREATED //201
        )
    }
}