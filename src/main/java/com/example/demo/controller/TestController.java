package com.example.demo.controller;
import com.example.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


// branch test another location

@RestController
public class TestController {

    @Autowired
    private ReviewService reviewService;

    @RequestMapping(value = "/test" , method = RequestMethod.GET) // branch test
    public String HelloSpringBoot(String str){
        System.out.println(str);
        return "Hello Spring Boot World" + str;
    }
// test2 branch
    @RequestMapping(value = "/review", method = RequestMethod.GET)
    public ResponseEntity display ( @RequestParam("fish_id") Integer fish_id ){
        return new ResponseEntity(reviewService.display(fish_id), HttpStatus.OK);
    }
}
// after merge branch test


// test1 branch