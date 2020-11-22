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
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(
    value = "professor operations",
    description = "professor operations",
    tags = ["V1 Professors"]
)
class ProfessorsFetch(
    private val professorsService: ProfessorsService
) {

    private val logger = KotlinLogging.logger {}

    @ApiOperation(
        value = "fetches a professor",
        response = Professor::class,
        notes = """
## sample request

```
GET /v1/professors/2a174df9-bffe-4ac2-931b-48ace3b3cd6a HTTP/1.1
```

## sample response

```
HTTP/1.1 200

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
            ApiResponse(code = 200, message = "OK"),
            ApiResponse(code = 404, message = "No such Professor")
        ]
    )

    @GetMapping("/v1/professors/{professorUid}")
    fun main(
        @PathVariable("professorUid") professorUid: String
    ): ResponseEntity<Professor> {

        logger.info { "event: fetching professor: $professorUid" }
        val course = professorsService.fetch(uid = professorUid)

        return ResponseEntity(
            course,
            HttpStatus.OK //200
        )
    }
}