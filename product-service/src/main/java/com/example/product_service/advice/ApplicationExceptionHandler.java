package com.example.product_service.advice;

import com.example.product_service.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    private ProblemDetail handleException(Exception ex, HttpStatus status, String problemTitle) {
        var problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problemDetail.setTitle(problemTitle);
        return problemDetail;
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleProductNotFound(ProductNotFoundException ex) {
        return handleException(ex, HttpStatus.NOT_FOUND, "Product Not Found");
    }

    @ExceptionHandler(NotEnoughInventoryException.class)
    public ProblemDetail handleNotEnoughInventory(NotEnoughInventoryException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST, "Not Enough Inventory");
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ProblemDetail handleProductAlreadyExists(ProductAlreadyExistsException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST, "Product Already Exists");
    }

    @ExceptionHandler(InvalidProductRequestException.class)
    public ProblemDetail handleInvalidProductRequest(InvalidProductRequestException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST, "Invalid Product Request");
    }

    @ExceptionHandler(InvalidPurchaseRequestException.class)
    public ProblemDetail handleInvalidProductPurchaseRequest(InvalidPurchaseRequestException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST, "Invalid Product Purchase Request");
    }

    @ExceptionHandler(InvalidPurchaseProcessRequestException.class)
    public ProblemDetail handleInvalidProductPurchaseProcessRequest(InvalidPurchaseProcessRequestException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST, "Invalid Product Purchase Process Request");
    }

    @ExceptionHandler(InvalidCancelProcessRequestException.class)
    public ProblemDetail handleInvalidProductCancelProcessRequest(InvalidCancelProcessRequestException ex) {
        return handleException(ex, HttpStatus.BAD_REQUEST, "Invalid Product Cancel Process Request");
    }



}
