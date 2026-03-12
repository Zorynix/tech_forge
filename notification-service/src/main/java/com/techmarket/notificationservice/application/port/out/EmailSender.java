package com.techmarket.notificationservice.application.port.out;

public interface EmailSender {

    boolean sendOrderNotification(String to, String subject, String htmlBody);
}
