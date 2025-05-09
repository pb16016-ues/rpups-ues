package com.ues.edu.sv.rpups_ues.model.DTO;

import lombok.Data;

@Data
public class MascotaIdDTO {
    private Long id;

    public MascotaIdDTO(Long id) {
        this.id = id;
    }

}
