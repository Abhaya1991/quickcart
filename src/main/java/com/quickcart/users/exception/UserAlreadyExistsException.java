package com.quickcart.users.exception;

public class UserAlreadyExistsException extends  RuntimeException{

    public UserAlreadyExistsException(String msg){
        super(msg);
    }
}
