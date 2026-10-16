package com.fapa.back.shared.exceptions;

import java.util.ArrayList;
import java.util.List;

import com.fapa.back.shared.dtos.FieldMessage;

import lombok.Getter;

@Getter 
public class BadRequestException extends RuntimeException {

    private List<FieldMessage> fieldMessages = new ArrayList<>();

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(List<FieldMessage> fieldMessages) {
        super("Validation failed");
        this.fieldMessages = fieldMessages;
    }

}
