package com.br.artur.produtoApi.resources.exceptions;

import com.br.artur.produtoApi.service.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandartError> resourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        String errorMessage = "Recurso não encontrado";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandartError err = new StandartError(Instant.now(), status.value(), errorMessage, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MultipartException.class)
    public String multipartException(MultipartException e, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
        return "redirect:/uploadStatus";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity maxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<StandartError> jsonProcessingException(JsonProcessingException e, HttpServletRequest request) {
        String errorMessage = "Falha ao processar o corpo da requisição: ";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandartError err = new StandartError(Instant.now(), status.value(), errorMessage, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}
