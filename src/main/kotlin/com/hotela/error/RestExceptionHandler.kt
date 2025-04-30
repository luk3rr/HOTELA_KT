package com.hotela.error

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import kotlin.reflect.full.findAnnotation

@ControllerAdvice
class RestExceptionHandler {
    @ExceptionHandler(HotelaException::class)
    fun handleHotelaException(e: HotelaException): ResponseEntity<ErrorResponse> {
        e::class.findAnnotation<ResponseStatus>()?.let {
            return ResponseEntity(ErrorResponse(e.code, e.message), it.value)
        }

        return ResponseEntity(
            ErrorResponse(e.code, e.message),
            HttpStatus.INTERNAL_SERVER_ERROR,
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> =
        ResponseEntity(
            ErrorResponse(HotelaException.INVALID_DATA, e.message ?: "Invalid data"),
            HttpStatus.BAD_REQUEST,
        )
}
