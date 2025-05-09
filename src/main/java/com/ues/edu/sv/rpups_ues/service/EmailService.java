package com.ues.edu.sv.rpups_ues.service;

public interface EmailService {
    public void sendPasswordResetEmail(String to, String passwordTemporal);

    public String generateTemporaryPassword();
}