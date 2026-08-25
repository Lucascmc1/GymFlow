package io.github.lucascmc1.gymflow.shared;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String notFound(ResourceNotFoundException exception, HttpServletRequest request, Model model) {
        model.addAttribute("statusCode", 404);
        model.addAttribute("errorTitle", "Página não encontrada");
        model.addAttribute("errorMessage", exception.getMessage());
        model.addAttribute("path", request.getRequestURI());
        return "error/error";
    }

    @ExceptionHandler(BusinessRuleException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    String businessRule(BusinessRuleException exception, HttpServletRequest request, Model model) {
        model.addAttribute("statusCode", 422);
        model.addAttribute("errorTitle", "Não foi possível concluir");
        model.addAttribute("errorMessage", exception.getMessage());
        model.addAttribute("path", request.getRequestURI());
        return "error/error";
    }
}
