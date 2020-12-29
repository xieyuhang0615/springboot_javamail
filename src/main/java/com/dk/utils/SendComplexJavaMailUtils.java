package com.dk.utils;

import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

/**
 * @ProjectName: springboot_javamail
 * @Package: com.dk.utils
 * @ClassName: SendJavaMailUtils
 * @Description: java类作用描述
 * @Author: 张凯
 * @CreateDate: 2019-11-29 09:32
 * @UpdateUser: 更新者
 * @UpdateDate: 2019-11-29 09:32
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
@Component
public class SendComplexJavaMailUtils {

    public static String send = "499165324@qq.com"; //发件人邮箱地址
    public static String authorization = "eynzmciwrxoebjhd"; //授权码
    public static String addresser = "谢雨杭";//发件人名称
    public static String title; //标题
    public static String content; //正文
    public static String filepaths;//附件地址
    public static String image;//图片地址

    //收件人地址
    public static String recipientNameAddr;
    //收件人名称
    public static String recipientName;
    //抄送人地址
    public static String CcPeopleNameAddr;
    //抄送人名称
    public static String CcPeopleName;
    //密送人地址
    public static String secretNameAddr;
    //密送人名称
    public static String secretName;


    public static void main() throws Exception {
        /*Properties 对象 里面配置了一些用于连接邮箱服务器的参数,如:端口号,邮箱协议,地址等*/
        Properties props = new Properties();
        //邮箱协议有三种:smtp,POP3,IMAP   这里我们使用的是smtp
        props.setProperty("mail.transport.protocol", "smtp");
        //因为我们使用QQ邮箱来发邮件,所有要使用QQ的smtp邮箱协议地址
        props.setProperty("mail.smtp.host", "smtp.qq.com");
        //QQ邮箱的协议服务器端口号
        props.setProperty("mail.smtp.port", "465");
        //如果配置163邮箱的话,以上配置就可以了,但如果是QQ的话,需要多配置一个SSL安全认证

        //对SSL安全认证进行配置
        //我们java通过使用 javax.net.ssl.SSLSocketFactory 这个类  来支持SSL认证
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        //让我们选择一些非SSL认证的,我们要不要处理, false的话 没经过SSL认证的我们一概不处理
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        //SSL认证协议端口号为:465   如果是非SSL认证 端口号为:25
        props.setProperty("mail.smtp.socketFactory.port", "465");

    /*Java代码如果想要和邮箱服务器进行交互的话,就必须要把他封装成一个对象,
    这个对象被称为javamail里面的session对象*/
        //将Properties 对象传进来,这样我们session对象就可以和服务器进行交互了
        Session session = Session.getInstance(props);
        session.setDebug(true);

        //与邮箱服务器交互的前提是  我们首先要创建一封邮件
        //判断用户传入的参数是否存在
        MimeMessage message = null;
        if (image == null  && filepaths == null ) {
            message = createMimeMessage(session, send, title, recipientNameAddr);
        } else if (image == null ) {
            message = createMimeMessage(session, send, title, filepaths, recipientNameAddr);
        } else if (filepaths == null ) {
            message = createMimeMessage(session, title, image);
        } else {
            message = createMimeMessage(session, send, title, image, filepaths, recipientNameAddr);
        }

        Transport transport = session.getTransport();//建立连接对象
        //建立连接,其中密码以"授权码"的形式体现
        //发件人  和  授权码
        transport.connect(send, authorization);
        //发送邮件
        //第一个参数为邮件, 第二个message.getAllRecipients()为获取所有收件人地址
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    //MimeMessage:带图片加附件的邮件
    public static MimeMessage createMimeMessage(Session session, String send, String title, String image, String filepaths, String recipientNameAddr) throws Exception {
        //我们在这里就可以new一封邮件,用于存放邮件的一些信息
        MimeMessage message = new MimeMessage(session);
        //邮件:标题 正文 收件人 发件人     {附件  图片  链接}
        //发件人
        Address address = new InternetAddress(send, addresser, "UTF-8");
        message.setFrom(address);
        //主题
        message.setSubject(title, "UTF-8");


        //首先我们创建一个图片节点
        MimeBodyPart imagePart = new MimeBodyPart();
        DataHandler imageDataHandler = new DataHandler(new FileDataSource(image));//存放图片地址
        imagePart.setDataHandler(imageDataHandler);
        //创建Id
        imagePart.setContentID("myImage");

        //创建文本节点,目的是为了加载图片节点   因为图片是通过文本的形式放在网页里面的
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(content + "<img src='cid:myImage' />", "text/html;charset=utf-8");

        //将文本节点,图片节点进行组装    组装成一个复合节点
        MimeMultipart multiparts = new MimeMultipart();
        multiparts.addBodyPart(imagePart);
        multiparts.addBodyPart(textPart);
        //将上面两者关联起来
        multiparts.setSubType("related");

        //注意:正文中 只能出现MimeBodyPart普通节点,而不能出现复合节点MimeMultipart而不能出现复合节点
        // MimeMultipart -→ MimeBodyPart
        MimeBodyPart text_image_bodyPart = new MimeBodyPart();
        text_image_bodyPart.setContent(multiparts);


        //附件
        //创建单节点
        MimeBodyPart attachment = new MimeBodyPart();
        //附件地址
        DataHandler FileDataHandler = new DataHandler(new FileDataSource(filepaths));
        //将附件地址设置到单节点里面
        attachment.setDataHandler(FileDataHandler);
        //给附件设置文件名
        attachment.setFileName(MimeUtility.encodeText(FileDataHandler.getName()));

        //将刚才处理好的"文本+图片"节点 与 附件 设置成一个新的混合节点
        MimeMultipart multipartss = new MimeMultipart();
        multipartss.addBodyPart(text_image_bodyPart);
        multipartss.addBodyPart(attachment);
        //将两者组装成一个混合关系节点
        multipartss.setSubType("mixed");

        //添加正文
        message.setContent(multipartss, "text/html;charset=utf-8");

        // 将组合的MimeMultipart(文本图片+附件)对象设置为整个邮件的内容，一定要调用saveChanges方法进行更新
        message.setContent(multipartss);

        //收件人类型: TO:普通收件人  CC:抄送   BCC:密送
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipientNameAddr, recipientName));
        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(CcPeopleNameAddr, CcPeopleName));
        message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress(secretNameAddr, secretName));

        message.setSentDate(new Date());//发邮件的时间
        message.saveChanges();//保存邮件
        return message;
    }


    //MimeMessage:附件的邮件  不带图片
    public static MimeMessage createMimeMessage(Session session, String send, String title, String filepaths, String recipientNameAddr) throws Exception {
        //我们在这里就可以new一封邮件,用于存放邮件的一些信息
        MimeMessage message = new MimeMessage(session);
        //邮件:标题 正文 收件人 发件人     {附件  图片  链接}
        //发件人
        Address address = new InternetAddress(send, addresser, "UTF-8");
        message.setFrom(address);
        //主题
        message.setSubject(title, "UTF-8");


        //正文
        // MimeMultipart类表示一个复合消息组合成的消息
        MimeMultipart multipart = new MimeMultipart();
        // 设置文本内容
        // MimeBodyPart类表示一个MIME消息
        MimeBodyPart textContent = new MimeBodyPart();
        //文本内容
        textContent.setText(content); //,"text/html;charset=UTF-8"
        multipart.addBodyPart(textContent);

        //附件
        //创建单节点
        MimeBodyPart attachment = new MimeBodyPart();
        //附件地址
        DataHandler FileDataHandler = new DataHandler(new FileDataSource(filepaths));
        //将附件地址设置到单节点里面
        attachment.setDataHandler(FileDataHandler);
        //给附件设置文件名
        attachment.setFileName(MimeUtility.encodeText(FileDataHandler.getName()));
        multipart.addBodyPart(attachment);

        message.setContent(multipart);

        //收件人类型: TO:普通收件人  CC:抄送   BCC:密送
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipientNameAddr, recipientName));
        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(CcPeopleNameAddr, CcPeopleName));
        message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress(secretNameAddr, secretName));

        message.setSentDate(new Date());//发邮件的时间
        message.saveChanges();//保存邮件
        return message;
    }


    //MimeMessage:不带图片,不带附件的邮件
    public static MimeMessage createMimeMessage(Session session, String send, String title, String recipientNameAddr) throws Exception {
        //我们在这里就可以new一封邮件,用于存放邮件的一些信息
        MimeMessage message = new MimeMessage(session);
        //邮件:标题 正文 收件人 发件人     {附件  图片  链接}
        //发件人
        Address address = new InternetAddress(send, addresser, "UTF-8");
        message.setFrom(address);
        //主题
        message.setSubject(title, "UTF-8");


        //正文
        // MimeMultipart类表示一个复合消息组合成的消息
        MimeMultipart multipart = new MimeMultipart();
        // 设置文本内容
        // MimeBodyPart类表示一个MIME消息
        MimeBodyPart textContent = new MimeBodyPart();
        //文本内容
        textContent.setText(content); //,"text/html;charset=UTF-8"
        multipart.addBodyPart(textContent);

        message.setContent(multipart);

        //收件人类型: TO:普通收件人  CC:抄送   BCC:密送
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipientNameAddr, recipientName));
        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(CcPeopleNameAddr, CcPeopleName));
        message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress(secretNameAddr, secretName));

        message.setSentDate(new Date());//发邮件的时间
        message.saveChanges();//保存邮件
        return message;
    }


    //MimeMessage:带图片不带附件的邮件
    public static MimeMessage createMimeMessage(Session session, String title, String image) throws Exception {
        //我们在这里就可以new一封邮件,用于存放邮件的一些信息
        MimeMessage message = new MimeMessage(session);
        //邮件:标题 正文 收件人 发件人     {附件  图片  链接}
        //发件人
        Address address = new InternetAddress(send, addresser, "UTF-8");
        message.setFrom(address);
        //主题
        message.setSubject(title, "UTF-8");

        //正文
        // MimeMultipart类表示一个复合消息组合成的消息
        MimeMultipart multipart = new MimeMultipart();
        // 设置文本内容
        // MimeBodyPart类表示一个MIME消息
        MimeBodyPart textContent = new MimeBodyPart();
        //文本内容
        textContent.setText(content); //,"text/html;charset=UTF-8"
        multipart.addBodyPart(textContent);


        //首先我们创建一个图片节点
        MimeBodyPart imagePart = new MimeBodyPart();
        DataHandler imageDataHandler = new DataHandler(new FileDataSource(image));//存放图片地址
        imagePart.setDataHandler(imageDataHandler);
        //创建Id
        imagePart.setContentID("myImage");

        //创建文本节点,目的是为了加载图片节点   因为图片是通过文本的形式放在网页里面的
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(content + "<img src='cid:myImage' />", "text/html;charset=utf-8");

        //将文本节点,图片节点进行组装    组装成一个复合节点
        MimeMultipart multiparts = new MimeMultipart();
        multiparts.addBodyPart(imagePart);
        multiparts.addBodyPart(textPart);
        //将上面两者关联起来
        multiparts.setSubType("related");

        //注意:正文中 只能出现MimeBodyPart普通节点,而不能出现复合节点MimeMultipart而不能出现复合节点
        // MimeMultipart -→ MimeBodyPart
        MimeBodyPart text_image_bodyPart = new MimeBodyPart();
        text_image_bodyPart.setContent(multiparts);

        // 将组合的MimeMultipart(文本图片+附件)对象设置为整个邮件的内容，一定要调用saveChanges方法进行更新
        message.setContent(multiparts);

        //收件人类型: TO:普通收件人  CC:抄送   BCC:密送
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipientNameAddr, recipientName));
        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(CcPeopleNameAddr, CcPeopleName));
        message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress(secretNameAddr, secretName));

        message.setSentDate(new Date());//发邮件的时间
        message.saveChanges();//保存邮件
        return message;
    }
}
