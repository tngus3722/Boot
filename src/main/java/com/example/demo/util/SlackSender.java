package com.example.demo.util;

import com.example.demo.domain.SlackAttachment;
import com.example.demo.domain.SlackParameter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;

@Service
public class SlackSender {
    @Value("${slack-url}")
    private String slack_url;

    private RestTemplate restTemplate;
    private SlackParameter slackParameter;

    public ResponseEntity send(){
        try {
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter((Charset.forName("UTF-8"))));
            slackParameter = new SlackParameter();
            slackParameter.setChannel("#tngus");
            slackParameter.setUsername("알람");
            slackParameter.setText("text");

            ArrayList<SlackAttachment> list = new ArrayList<>();
            SlackAttachment attachment = new SlackAttachment();
            attachment.setAuthor_name("정수현");
            attachment.setTitle("Title");
            attachment.setText("spring boot slack noti test");
            attachment.setColor("36a64f");
            list.add(attachment);
            slackParameter.setAttachments(list);

            restTemplate.postForObject(slack_url,slackParameter, String.class);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (Exception e){
            return  new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
