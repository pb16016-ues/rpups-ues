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
        String messageText = "Su contraseña ha sido restablecida.\nSu nueva contraseña temporal es: " + passwordTemporal
                + " \n\nPor favor, cambiar la contraseña temporal lo antes posible.";
        message.setTo(to);
        message.setSubject("Restablecimiento de contraseña");
        message.setText(messageText);
        javaMailSender.send(message);

    }

    @Override
    public String generateTemporaryPassword() {
        return PasswordGenerator.generateTemporaryPassword(10);
    }
}
