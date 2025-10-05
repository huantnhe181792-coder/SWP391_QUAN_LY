package com.mycompany.swp391.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class SendEmail {

    private static final String DEFAULT_FROM_NAME = "Hệ thống Quản Lý CLB";
    private static final String SMTP_USERNAME = "khoapvhe181066@fpt.edu.vn";
    private static final String SMTP_PASSWORD = "xpewdawsihwpigoa";

    /**
     * Generic email sender using Gmail SMTP..
     * @param toEmail recipient email
     * @param subject email subject
     * @param body email body (text or HTML based on isHtml)
     * @param isHtml whether body is HTML
     * @param fromPersonalName personal name to show in From header (optional, can be null)
     * @return true if sent successfully, otherwise false
     */
    public static boolean sendEmail(String toEmail, String subject, String body, boolean isHtml, String fromPersonalName) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            InternetAddress fromAddress = (fromPersonalName != null && !fromPersonalName.isEmpty())
                    ? new InternetAddress(SMTP_USERNAME, fromPersonalName)
                    : new InternetAddress(SMTP_USERNAME, DEFAULT_FROM_NAME);
            message.setFrom(fromAddress);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject, "UTF-8");
            if (isHtml) {
                message.setContent(body, "text/html; charset=UTF-8");
            } else {
                message.setText(body, "UTF-8");
            }

            Transport.send(message);
            System.out.println("Email đã được gửi thành công tới: " + toEmail);
            return true;
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Backward-compatible helper for sending a newly generated password.
     */
    public static boolean sendNewPassWord(String email, String newPass) {
        String subject = "Mật khẩu mới";
        String body = "Mật khẩu mới của bạn là : " + newPass;
        return sendEmail(email, subject, body, false, DEFAULT_FROM_NAME);
    }
}


