package com.sinosoft.cimp_service.util;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * @author hiYuzu
 * @version V1.0
 * @date 2021/9/23 13:47
 */
public class EmailUtil {
    /**
     * 授权码
     */
    private static final String PWD = "apaugysbfjwsbgda";
    /**
     * 主机地址
     */
    private static final String HOST = "smtp.qq.com";
    /**
     * 发送者
     */
    private static final String SENDER = "88999747@qq.com";
    /**
     * 接收者
     */
    private static final String RECIPIENT = "pomelo.yuzu1997@gmail.com";
    /**
     * 正文编码方式
     */
    private static final String CONTENT_TYPE = "text/html;charset=UTF-8";

    /**
     * 发送邮件
     *
     * @param subject 邮件主题
     * @param content 邮件正文
     * @throws MessagingException 消息异常
     * @throws GeneralSecurityException 通用安全异常
     */
    public static void send(String subject, String content) throws MessagingException, GeneralSecurityException {
        Properties properties = new Properties();
        properties.setProperty("mail.host", HOST);
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER, PWD);
            }
        });

        session.setDebug(true);

        Transport transport = session.getTransport();
        transport.connect(HOST, SENDER, PWD);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SENDER));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(RECIPIENT));
        message.setSubject(subject);
        message.setContent(content, CONTENT_TYPE);

        transport.sendMessage(message, message.getAllRecipients());

        transport.close();
    }
}
