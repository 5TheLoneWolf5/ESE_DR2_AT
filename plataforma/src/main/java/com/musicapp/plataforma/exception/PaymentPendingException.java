package com.musicapp.plataforma.exception;

public class PaymentPendingException extends RuntimeException {
    public PaymentPendingException(String message) {
        super(message);
    }
}
