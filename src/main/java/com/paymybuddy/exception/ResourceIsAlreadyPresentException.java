package com.paymybuddy.exception;

public class ResourceIsAlreadyPresentException extends Exception{

    public ResourceIsAlreadyPresentException(){}

    public ResourceIsAlreadyPresentException(String message){
        super(message);
    }

}
