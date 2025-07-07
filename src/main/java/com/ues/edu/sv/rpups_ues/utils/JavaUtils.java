package com.ues.edu.sv.rpups_ues.utils;

public class JavaUtils {

    public static String validarFormatearCarnet(String carnet) {
        if (carnet == null || carnet.length() != 7) {
            throw new IllegalArgumentException("El carnet debe tener exactamente 7 caracteres.");
        }

        // Validar letras
        String letras = carnet.substring(0, 2).toUpperCase();
        if (!letras.matches("[A-Z]{2}")) {
            throw new IllegalArgumentException("El carnet debe iniciar con dos letras.");
        }

        // Validar números
        String numeros = carnet.substring(2);
        if (!numeros.matches("\\d{5}")) {
            throw new IllegalArgumentException("El carnet debe tener 5 números después de las letras.");
        }

        // Validar año de ingreso
        int anioIngreso = Integer.parseInt(numeros.substring(0, 2));
        int anioActual = java.time.Year.now().getValue() % 100; // últimos dos dígitos del año actual
        if (anioIngreso > anioActual) {
            throw new IllegalArgumentException("El año de ingreso en el carnet no puede ser mayor al año actual.");
        }

        return letras + numeros;
    }

}
