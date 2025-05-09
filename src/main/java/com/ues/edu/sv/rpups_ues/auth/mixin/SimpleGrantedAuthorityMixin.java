package com.ues.edu.sv.rpups_ues.auth.mixin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleGrantedAuthorityMixin {

    @JsonCreator
    SimpleGrantedAuthorityMixin(@JsonProperty("authority") String role) {

    }
}