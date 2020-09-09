package com.example.demo.controller;

import com.example.demo.util.SlackSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SlackController {

    @Autowired
    SlackSender slackSender;

    @RequestMapping(value = "/slack" , method = RequestMethod.GET)
    public ResponseEntity testSlack(){
        return slackSender.send();
    }
}
