package com.ues.edu.sv.rpups_ues.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ues.edu.sv.rpups_ues.service.EmailService;

import freemarker.template.Configuration;
import com.ues.edu.sv.rpups_ues.utils.PasswordGenerator;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final Configuration configuration;

    public EmailServiceImpl(JavaMailSender javaMailSender, Configuration configuration) {
        this.javaMailSender = javaMailSender;
        this.configuration = configuration;
    }

    @Override
    public void sendPasswordResetEmail(String to, String passwordTemporal) {

        SimpleMailMessage message = new SimpleMailMessage();
        String messageText = "Buen día,\n\nSu contraseña ha sido restablecida.\n\nSu nueva contraseña temporal es: "
                + passwordTemporal
                + " \n\nPor favor, cambiar la contraseña temporal lo antes posible, si así lo desea. \n\n\n\nSaludos cordiales,\n\nSistema RPUPS UES";
        message.setTo(to);
        message.setSubject("Restablecimiento de contraseña");
        message.setText(messageText);
        javaMailSender.send(message);

    }

    @Override
    public void sendNotificationSolicitudProyectoEmail(String to, String estadoSolicitud, String observaciones) {

        SimpleMailMessage message = new SimpleMailMessage();
        String messageText = "";

        if ("Aprobado".equalsIgnoreCase(estadoSolicitud)) {
            message.setSubject("Notificación de aprobación de solicitud de proyecto propuesto");
            messageText = "Buen día,\n\nSu solicitud de proyecto ha sido APROBADA. \nObservaciones: "
                    + observaciones
                    + "\n\n\n\nSaludos cordiales,\n\nSistema RPUPS UES";
        } else if ("Rechazado".equalsIgnoreCase(estadoSolicitud)) {
            message.setSubject("Notificación de rechazo de solicitud de proyecto propuesto");
            messageText = "Buen día,\n\nSu solicitud de proyecto ha sido RECHAZADA. \nLas observaciones realizadas a su solicitud son las siguientes: \n\n"
                    + observaciones + "\n\n\n\nSaludos cordiales,\n\nSistema RPUPS UES";
        } else if ("En Observación".equalsIgnoreCase(estadoSolicitud)) {
            message.setSubject("Notificación de observaciones a solicitud de proyecto propuesto");
            messageText = "Buen día,\n\nSu solicitud de proyecto tiene OBSERVACIONES. \nLas observaciones realizadas a su solicitud son las siguientes: \n\n"
                    + observaciones + "\n\n\n\nSaludos cordiales,\n\nSistema RPUPS UES";
        }

        message.setTo(to);
        message.setText(messageText);
        javaMailSender.send(message);
    }

    @Override
    public String generateTemporaryPassword() {
        return PasswordGenerator.generateTemporaryPassword(10);
    }
}
