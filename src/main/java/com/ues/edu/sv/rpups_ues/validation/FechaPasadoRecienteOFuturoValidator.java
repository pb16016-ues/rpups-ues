package com.ues.edu.sv.rpups_ues.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

/**
 * Validador que implementa la lógica para la anotación @FechaPasadoRecienteOFuturo.
 * 
 * Permite fechas desde N días antes de la fecha actual hasta cualquier fecha futura,
 * donde N es configurable mediante la anotación (por defecto 15 días).
 */
public class FechaPasadoRecienteOFuturoValidator 
        implements ConstraintValidator<FechaPasadoRecienteOFuturo, LocalDate> {

    private int diasAntes;

    @Override
    public void initialize(FechaPasadoRecienteOFuturo constraintAnnotation) {
        this.diasAntes = constraintAnnotation.diasAntes();
    }

    @Override
    public boolean isValid(LocalDate fecha, ConstraintValidatorContext context) {
        // Si la fecha es null, se considera válida (usar @NotNull por separado si se requiere)
        if (fecha == null) {
            return true;
        }

        // Calcular la fecha mínima permitida (hoy - diasAntes)
        LocalDate fechaMinimaPermitida = LocalDate.now().minusDays(diasAntes);

        // La fecha debe ser igual o posterior a la fecha mínima permitida
        return !fecha.isBefore(fechaMinimaPermitida);
    }
}
