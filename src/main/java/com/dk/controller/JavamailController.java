package com.dk.controller;

import com.dk.utils.SendComplexJavaMailUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String sendJavaMail(){
        //主题
        SendComplexJavaMailUtils.title = "<东科教育,万薪就业>";
        //内容
        SendComplexJavaMailUtils.content = "springboot都有哪些注解?   详情见附件";
        //收件人地址
        SendComplexJavaMailUtils.recipientNameAddr = "1094723477@qq.com";
        //收件人名称
        SendComplexJavaMailUtils.recipientName = "任畅";
        //附件
        SendComplexJavaMailUtils.filepaths = "C:\\Users\\Administrator\\Desktop\\springboot都有哪些注解.pdf";
        //图片
        SendComplexJavaMailUtils.image = "E:\\imgs\\东科教育.bmp";
        try {
            SendComplexJavaMailUtils.main();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "show";
    }
}
