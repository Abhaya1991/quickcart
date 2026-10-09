package com.quickcart.users.exception;

public class InvalidCredentialsException extends RuntimeException{

    public InvalidCredentialsException(String msg){
       super(msg);
    }
}
