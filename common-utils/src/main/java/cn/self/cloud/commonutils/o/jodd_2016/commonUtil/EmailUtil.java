package cn.self.cloud.commonutils.o.jodd_2016.commonUtil;//package jodd_2016.commonUtil;
//
//import java.io.File;
//import java.io.IOException;
//import org.junit.Test;
//import jodd.io.FileUtil;
//import jodd.mail.Email;
//import jodd.mail.EmailAttachment;
//import jodd.mail.EmailMessage;
//import jodd.mail.SendMailSession;
//import jodd.mail.SmtpSslServer;
//import jodd.mail.att.ByteArrayAttachment;
//import jodd.mail.att.FileAttachment;
//import jodd.util.MimeTypes;
///**
//* JODD操作email类
//*
//* @author DJZHOU
//*
//*/
//public class EmailUtil
//{
//public static void main(String[] args)
//{
//Email email = Email.create();
//email.addMessage(new EmailMessage("消息"));
//email.addText("邮件内容");
//email.embedFile(new File("d:/console.txt"));
//email.from("771842634@qq.com").to("771842634@qq.com");
//email.subject("主题");
//SendMailSession mailSession = new SmtpSslServer("smtp.qq.com//发送端邮箱服务器协议", "发送端QQ邮箱", "发送端QQ邮箱密码").createSession();
//mailSession.open();
//mailSession.sendMail(email);
//mailSession.close();
//System.out.println("发送成功!...");
//}
//@Test
//public void test() throws IOException
//{
//Email email = new Email();
//email.setFrom("infoxxx@jodd.org");
//email.setTo("igorxxxx@gmail.com");
//email.setSubject("test7");
//EmailMessage textMessage = new EmailMessage("Hello!", MimeTypes.MIME_TEXT_PLAIN);
//email.addMessage(textMessage);
//EmailMessage htmlMessage = new EmailMessage(
//"" +
//"
//Hey!
//jodd使用示例
//Hay!
//",
//MimeTypes.MIME_TEXT_HTML);
//email.addMessage(htmlMessage);
//EmailAttachment embeddedAttachment =
//new ByteArrayAttachment(FileUtil.readBytes("d:\\c.png"), "image/png", "c.png", "c.png");
//email.attach(embeddedAttachment);
//EmailAttachment attachment = new FileAttachment(new File("d:\\b.jpg"), "b.jpg", "image/jpeg");
//email.attach(attachment);
//}
//}