package com.example.Qolzy.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring6.SpringTemplateEngine;

import javax.naming.Context;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendRegistrationEmail(String to, String subject, String templateName, Context thymeleafContext) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
            String html = templateEngine.process(templateName, (IContext) thymeleafContext);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            helper.setFrom("no-reply@yourdomain.com");
            mailSender.send(message);
        } catch (Exception e) {
            // log và xử lý lỗi phù hợp
            throw new RuntimeException("Lỗi khi gửi email", e);
        }
    }
}
