package com.ues.edu.sv.rpups_ues.exceptions;

public class DuplicateEntityException extends RuntimeException {
    private String mensaje;

    public DuplicateEntityException(String mensaje) {
        super(mensaje);
        this.mensaje = mensaje;
    }

    public String getField() {
        return mensaje;
    }
}
