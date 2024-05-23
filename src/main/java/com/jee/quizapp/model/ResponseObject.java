package com.jee.quizapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseObject {
    private Integer code;
    private String message;
    private Object data;
}
