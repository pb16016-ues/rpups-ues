package com.ues.edu.sv.rpups_ues.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validación personalizada que permite fechas desde 15 días antes de la fecha actual
 * hasta cualquier fecha futura.
 * 
 * Esta validación es útil para casos donde se necesita permitir fechas recientes
 * del pasado (hasta 15 días antes) además de fechas presentes y futuras.
 */
@Documented
@Constraint(validatedBy = FechaPasadoRecienteOFuturoValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FechaPasadoRecienteOFuturo {

    String message() default "La fecha debe ser desde 15 días antes de hoy en adelante";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Número de días antes de la fecha actual que se permiten.
     * Por defecto: 15 días.
     */
    int diasAntes() default 15;
}
