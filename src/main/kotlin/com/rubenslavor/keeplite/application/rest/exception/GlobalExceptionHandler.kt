package com.rubenslavor.keeplite.application.rest.exception

import com.rubenslavor.keeplite.domain.exceptions.BusinessException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.message ?: "Erro na requisição")
        problemDetail.title = "Regra de Negócio Violada"
        log.warn("BusinessException: {}", ex.message)
        return problemDetail
    }

    @ExceptionHandler(NullPointerException::class)
    fun handleNullPointerException(ex: NullPointerException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        problemDetail.title = "Erro Interno do Servidor"
        problemDetail.detail = "Ocorreu um erro inesperado. Referência do erro: ${ex.message}"
        log.error("NullPointerException: ", ex)
        return problemDetail
    }

    // Captura qualquer outra exceção não tratada e retorna 500
    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ProblemDetail {
        val problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        problemDetail.title = "Erro Inesperado"
        problemDetail.detail = "Ocorreu um erro inesperado no processamento da sua requisição."
        log.error("Exception não tratada: ", ex)
        return problemDetail
    }
}