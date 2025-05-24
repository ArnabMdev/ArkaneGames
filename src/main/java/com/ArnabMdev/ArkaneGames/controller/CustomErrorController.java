package com.ArnabMdev.ArkaneGames.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = "An error occurred";
        String errorDetails = "";

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            model.addAttribute("statusCode", statusCode);

            // Get error details
            Object exceptionObj = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
            if (exceptionObj != null) {
                Throwable exception = (Throwable) exceptionObj;
                errorMessage = exception.getMessage();
                errorDetails = getStackTraceAsString(exception);
            }

            // Add specific messages for common HTTP status codes
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorMessage = "Page not found";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorMessage = "Access denied";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorMessage = "Internal server error";
            }
        }

        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("errorDetails", errorDetails);
        model.addAttribute("timestamp", new java.util.Date());
        model.addAttribute("path", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));

        return "error";
    }

    private String getStackTraceAsString(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (int i = 0; i < Math.min(10, stackTrace.length); i++) {
            sb.append(stackTrace[i].toString()).append("\n");
        }
        return sb.toString();
    }
}
