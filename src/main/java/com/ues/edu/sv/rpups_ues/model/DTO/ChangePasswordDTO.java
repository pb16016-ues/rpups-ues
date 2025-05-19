package com.ues.edu.sv.rpups_ues.model.DTO;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDTO {

    @Size(min = 8, max = 20, message = "El password debe tener entre 8 y 20 caracteres")
    String oldPassword;
    @Size(min = 8, max = 20, message = "El nuevo password debe tener entre 8 y 20 caracteres")
    String newPassword;
}
