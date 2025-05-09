package com.ues.edu.sv.rpups_ues.model.DTO;

import lombok.Data;

@Data
public class SimpleEmail {
    private String to;
    private String subject;
    private String body;

}