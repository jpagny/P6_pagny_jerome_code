package com.paymybuddy.exception;

public class ResourceIsAlreadyPresentInOurDatabaseException extends Exception{

    public ResourceIsAlreadyPresentInOurDatabaseException(){}

    public ResourceIsAlreadyPresentInOurDatabaseException(String message){
        super(message);
    }

}
