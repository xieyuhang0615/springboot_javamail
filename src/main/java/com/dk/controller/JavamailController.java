package com.dk.controller;

import com.dk.utils.SendComplexJavaMailUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @program: springboot_javamail
 * @description: TODO
 * @author: Mr.XYH
 * @create: 2020-12-08 17:40
 **/
@Controller
@RequestMapping("javamailController")
public class JavamailController {


    @RequestMapping("sendJavaMail")
    @ResponseBody
    public String sendJavaMail(){
        //主题
        SendComplexJavaMailUtils.title = "<测试test>";
        //内容
        SendComplexJavaMailUtils.content = "第一封邮件";
        //收件人地址
        SendComplexJavaMailUtils.recipientNameAddr = "yzh1940579742@163.com";
        //收件人名称
        SendComplexJavaMailUtils.recipientName = "yzh1940579742";
//        //附件
//        SendComplexJavaMailUtils.filepaths = "C:\\Users\\Administrator\\Desktop\\javamail.docx";
        //图片
        SendComplexJavaMailUtils.image = "E:\\imgs\\cs50009.png";
        try {
            SendComplexJavaMailUtils.main();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "发送成功";
    }
}
