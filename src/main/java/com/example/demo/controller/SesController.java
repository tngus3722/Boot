package com.example.demo.controller;

import com.example.demo.util.SesSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class SesController {
    @Autowired
    private SesSender awsMailSender;

    @ResponseBody
    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public ResponseEntity<String> send() {
        awsMailSender.sendMail("boot@www.tngus3722.com", "tngus3722@koreatech.ac.kr", "aws mail test", "<h1>TEst</h1>");
        return new ResponseEntity<String>("Success", HttpStatus.OK);
    }
}
