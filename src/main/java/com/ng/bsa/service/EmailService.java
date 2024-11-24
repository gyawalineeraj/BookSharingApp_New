package com.ng.bsa.service;

import com.ng.bsa.entities.ConfirmationCode;
import com.ng.bsa.entities.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    private final ConfirmationCodeService confirmationCodeService;
    @Value("${spring.email.confirmationUrl}")
    private String confirmationUrl;
    @Value("${spring.email.emailSending}")
    private String emailSendingmail;


    public void sendConfirmationEmail(User user,String emailSubject) throws MessagingException {
        System.out.println("user id is " + user.getId());
        String activationCode =
                confirmationCodeService.generateAndSaveConfirmationCode(user);

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", user.getFirstName());
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activation_code", activationCode);
        Context context = new Context();
        context.setVariables(properties);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name());

        String template =springTemplateEngine.process("activate-account",
                context);

        helper.setTo(user.getEmail());
        helper.setFrom(emailSendingmail);
        helper.setSubject(emailSubject);
        helper.setText(template,true);

        javaMailSender.send(mimeMessage);
    }

}
