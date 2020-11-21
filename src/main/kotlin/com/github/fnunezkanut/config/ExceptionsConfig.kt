package com.github.fnunezkanut.config

import com.fasterxml.jackson.annotation.JsonProperty
import mu.KotlinLogging
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.*
import javax.servlet.http.HttpServletRequest


//deals with exceptions floating up from controllers
@ControllerAdvice
@Order(-2)
class ApiErrorControllerAdvice {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(ApiError::class)
    fun handleApiError(req: HttpServletRequest, ex: ApiError): ResponseEntity<JsonError> {
        logger.error(ex) { "ApiError: reason=${ex.reason}, status=${ex.status}" }
        return ResponseEntity.status(ex.status)
            .body(
                JsonError(
                    timestamp = Date(),
                    status = ex.status.value(),
                    error = ex.status.reasonPhrase,
                    message = ex.message,
                    path = req.servletPath
                )
            )
    }
}

//custom exception
class ApiError(
    val status: HttpStatus,
    val reason: String? = null
) : RuntimeException(reason) {
    constructor(statusCode: Int, reason: String? = null) : this(
        status = HttpStatus.valueOf(statusCode),
        reason = reason
    )
}

//json error structure to mimic spring boot error model
data class JsonError(
    @JsonProperty("timestamp") val timestamp: Date,
    @JsonProperty("status") val status: Int,
    @JsonProperty("error") val error: String?,
    @JsonProperty("message") val message: String?,
    @JsonProperty("path") val path: String
)