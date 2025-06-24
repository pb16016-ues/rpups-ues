package com.ues.edu.sv.rpups_ues.service;

public interface EmailService {
    public void sendPasswordResetEmail(String to, String passwordTemporal);

    public void sendPasswordInitialEmail(String to, String passwordTemporal);

    public void sendNotificationSolicitudProyectoEmail(String to, String estadoSolicitud, String observaciones);

    public String generateTemporaryPassword();
}