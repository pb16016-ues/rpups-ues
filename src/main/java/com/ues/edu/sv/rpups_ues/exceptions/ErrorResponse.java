package com.ues.edu.sv.rpups_ues.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;
    private int code;
    private String status;
    private String message;
    private String path;
    private Map<String, String> validations;

    public ErrorResponse() {

    }

    public ErrorResponse(Date timestamp, int code, String status, String message, String path) {
        this.timestamp = timestamp;
        this.code = code;
        this.status = status;
        this.message = message;
        this.path = path;
    }

    public ErrorResponse(Date timestamp, int code, String status, String message, String path,
            Map<String, String> validations) {
        this.timestamp = timestamp;
        this.code = code;
        this.status = status;
        this.message = message;
        this.path = path;
        this.validations = validations;
    }
}
