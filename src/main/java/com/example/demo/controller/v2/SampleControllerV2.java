package com.example.demo.controller.v2;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/v2")
@RestController
public class SampleControllerV2 {

    @RequestMapping(value = "/sample" , method = RequestMethod.GET)
    public String sample(){
        return "sample v2";
    }
}
