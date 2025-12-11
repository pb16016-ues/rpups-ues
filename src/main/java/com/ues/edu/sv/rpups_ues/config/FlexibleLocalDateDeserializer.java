package com.ues.edu.sv.rpups_ues.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Deserializador flexible para LocalDate que acepta múltiples formatos:
 * - "yyyy-MM-dd" (formato estándar)
 * - "yyyy-MM-ddTHH:mm:ss.SSSZ" (formato ISO 8601 con hora - usado por Angular)
 * - "yyyy-MM-ddTHH:mm:ssZ" (formato ISO 8601 sin milisegundos)
 */
public class FlexibleLocalDateDeserializer extends JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter DATE_ONLY = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateStr = p.getText();
        
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        
        dateStr = dateStr.trim();
        
        // Intentar primero formato simple yyyy-MM-dd
        if (dateStr.length() == 10) {
            try {
                return LocalDate.parse(dateStr, DATE_ONLY);
            } catch (DateTimeParseException e) {
                // Continuar con otros formatos
            }
        }
        
        // Si contiene 'T', es formato ISO con hora
        if (dateStr.contains("T")) {
            try {
                // Intentar como OffsetDateTime (tiene zona horaria Z o +00:00)
                OffsetDateTime odt = OffsetDateTime.parse(dateStr);
                return odt.toLocalDate();
            } catch (DateTimeParseException e) {
                // Intentar extraer solo la parte de fecha
                try {
                    String datePart = dateStr.substring(0, 10);
                    return LocalDate.parse(datePart, DATE_ONLY);
                } catch (Exception ex) {
                    throw new IOException("No se pudo parsear la fecha: " + dateStr, ex);
                }
            }
        }
        
        throw new IOException("Formato de fecha no reconocido: " + dateStr);
    }
}
