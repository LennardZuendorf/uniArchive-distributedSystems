package de.berlin.htw.control.exception;

public class ProductAlreadyExistException extends Exception{

    public ProductAlreadyExistException(String errorMessage) {
        super(errorMessage);
    }
}
