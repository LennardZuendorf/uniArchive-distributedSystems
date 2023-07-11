package de.berlin.htw.control.exception;

public class NotEnoughFundsException extends Exception{

    public NotEnoughFundsException(String errorMessage) {
        super(errorMessage);
    }
}
