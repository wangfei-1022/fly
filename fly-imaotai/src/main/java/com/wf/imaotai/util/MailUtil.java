package com.wf.imaotai.util;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.nio.file.Files;
import java.util.Properties;

public class MailUtil {

    public static String username = "927230462@qq.com";
    public static String password = "Wang133655fei";
    public static String host = "smtp.gmail.com";
    public static String port = "587";
    public static Session session = null;

    public static void createSession() {
        if (session != null) {
            return;
        }

        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.prot", port);

        session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                })
    }

    /*
     * 发送纯文本邮件
     * */
    public static void postMessage(String title, String content, String toMail) {
        try {
            createSession();
            // 构造邮件主体
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setSubject(title);
            mimeMessage.setText(content);
            mimeMessage.setFrom(new InternetAddress(username));
            mimeMessage.setRecipient(MimeMessage.RecipientType.TO, new InernetAddress(toMail));
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * 发送附件邮件
     * */
    public static void postMessageWithFile(String title, String content, String fileName, String filePath, String toMail) {
        try {
            createSession();
            // 构造邮件主体
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setSubject(title);

            // 邮件主体
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(content, "text/html;charset=UTF-8");

            //邮件附件
            BodyPart filePart = new MimeBodyPart();
            filePart.setFileName(fileName);
            filePart.setDataHandler(new DataHandler(new ByteArrayDataSource(Files.readAllBytes(Paths.get(filePath)))));

            Multipart multipart = new Multipart();
            multipart.addBodyPart(bodyPart);
            multipart.addBodyPart(filePart);
            mimeMessage.setContent(content);

            mimeMessage.setFrom(new InternetAddress(username));
            mimeMessage.setRecipient(MimeMessage.RecipientType.TO, new InernetAddress(toMail));

            // 发送
            Transport.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}