package com.huifenqi.hzf_platform.email;

import java.io.*;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import sun.net.smtp.SmtpClient;

/**
 * Created by HFQ-Arison on 2017/4/11.
 */
public class SendEmailUtil {
    public static void impvSendMain(String subject,String content,String toMails,String fileName,FileInputStream fis) throws Exception {
        Properties props = new Properties();
        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.exmail.qq.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        props.setProperty("mail.host.port", "465");
        // 设置环境信息
        Session session = Session.getInstance(props);

        // 创建邮件对象
        Message msg = new MimeMessage(session);
        msg.setSubject(subject);
        // 设置邮件内容
        // msg.setText("这是一封由JavaMail发送的邮件！");
        // 设置发件人
        msg.setFrom(new InternetAddress("issues@huizhaofang.com"));

        msg.addHeader("charset", "UTF-8");

        /*添加正文内容*/
        Multipart multipart = new MimeMultipart();
        BodyPart contentPart = new MimeBodyPart();
        contentPart.setText(content);
        contentPart.setHeader("Content-Type", "text/html; charset=UTF-8");
        multipart.addBodyPart(contentPart);

        /*添加附件*/
        MimeBodyPart fileBody = new MimeBodyPart();
        DataSource source = new ByteArrayDataSource(fis, "application/msexcel");
        fileBody.setDataHandler(new DataHandler(source));
        // 中文乱码问题
        fileBody.setFileName(MimeUtility.encodeText(fileName));
        multipart.addBodyPart(fileBody);

        msg.setContent(multipart);
        msg.setSentDate(new Date());
        msg.saveChanges();

        String[] mailAddresses=toMails.split(",");
        Address[] addresses=new Address[mailAddresses.length];
        for(int i=0;i<mailAddresses.length;i++){
            addresses[i]=new InternetAddress(mailAddresses[i]);
        }

        Transport transport = session.getTransport();
        // 连接邮件服务器
        transport.connect("issues@huizhaofang.com", "IsuHuizhaofang@2017");
        // 发送邮件
        transport.sendMessage(msg, addresses);
        // 关闭连接
        transport.close();
    }


    /**
     * Created by HFQ-Arison on 2017/4/11.
     */
    public static void sendTextMail(String subject,String content,String toMails) throws Exception {
        Properties props = new Properties();
        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.exmail.qq.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        props.setProperty("mail.host.port", "465");
        // 设置环境信息
        Session session = Session.getInstance(props);

        // 创建邮件对象
        MimeMessage msg = new MimeMessage(session);
        msg.setSubject(subject);
        // 设置邮件内容
        // msg.setText("这是一封由JavaMail发送的邮件！");
        // 设置发件人
        msg.setFrom(new InternetAddress("issues@huizhaofang.com"));

        //msg.addHeader("charset", "UTF-8");
        msg.addHeader("Content-Type","text/html; charset=UTF-8");

        /*添加正文内容*/
            /*Multipart multipart = new MimeMultipart();
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setHeader("Content-Type", "text/html; charset=UTF-8");
            contentPart.setText(content);
            multipart.addBodyPart(contentPart);
           */
        /*添加附件*/
           /* MimeBodyPart fileBody = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(fis, "application/msexcel");
            fileBody.setDataHandler(new DataHandler(source));
            // 中文乱码问题
            fileBody.setFileName(MimeUtility.encodeText(fileName));
            multipart.addBodyPart(fileBody);*/

        msg.setContent(content,"text/html;charset = gbk");
        msg.setSentDate(new Date());
        msg.saveChanges();
        msg.writeTo(new FileOutputStream("htmlMail.html"));

        String[] mailAddresses=toMails.split(",");
        Address[] addresses=new Address[mailAddresses.length];
        for(int i=0;i<mailAddresses.length;i++){
            addresses[i]=new InternetAddress(mailAddresses[i]);
        }

        Transport transport = session.getTransport();
        // 连接邮件服务器
        transport.connect("issues@huizhaofang.com", "Issues@huizhaofang2026");
        // 发送邮件
        transport.sendMessage(msg, addresses);
        // 关闭连接
        transport.close();
    }
}
