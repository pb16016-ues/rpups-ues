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

        if (observaciones == null || observaciones.isBlank() || observaciones.isEmpty()) {
            observaciones = "Sin observaciones en la solicitud";
        }
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
        } else if ("En Revisión".equalsIgnoreCase(estadoSolicitud) || "Pendiente".equalsIgnoreCase(estadoSolicitud)) {
            String[] datos = observaciones.split("\\|");

            String titulo = datos[0];
            String nombreCompleto = datos[1];
            String correoInstitucional = datos[2];
            String observacion = datos[3];
            message.setSubject("Notificación de solicitud de proyecto corregida por el usuario");
            messageText = "Buen día,\n\nLa solicitud de proyecto con el título '" + titulo
                    + "', que se envió con observaciones al usuario " + nombreCompleto + " con el email << "
                    + correoInstitucional
                    + ">>, ha sido corregida con respecto a las observaciones realizadas anteriormente. \n\nLas observaciones especificadas a la solicitud fueron las siguientes: \n"
                    + observacion + "\n\n\n\nSaludos cordiales,\n\nSistema RPUPS UES";
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
