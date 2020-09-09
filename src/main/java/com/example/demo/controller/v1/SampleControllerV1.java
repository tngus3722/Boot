package com.example.demo.controller.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/v1")
@RestController
public class SampleControllerV1 {

    @RequestMapping(value = "/sample" , method = RequestMethod.GET)
    public String sample(){
        return "sample v1";
    }
}
