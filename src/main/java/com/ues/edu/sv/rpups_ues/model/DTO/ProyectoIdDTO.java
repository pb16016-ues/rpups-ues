package com.ues.edu.sv.rpups_ues.model.DTO;

import lombok.Data;

@Data
public class ProyectoIdDTO {
    private Long id;

    public ProyectoIdDTO(Long id) {
        this.id = id;
    }

}
