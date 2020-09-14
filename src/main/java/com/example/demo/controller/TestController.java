package com.example.demo.controller;
import com.example.demo.annotation.XSS;
import com.example.demo.domain.Review;
import com.example.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @Autowired
    private ReviewService reviewService;

    @RequestMapping(value = "/test" , method = RequestMethod.GET)
    public String HelloSpringBoot(String str){
        return "Hello Spring Boot World" + str;
    }

    @RequestMapping(value = "/review", method = RequestMethod.GET)
    public ResponseEntity display ( @RequestParam("fish_id") Integer fish_id ){
        return new ResponseEntity(reviewService.display(fish_id), HttpStatus.OK);
    }

    @XSS
    @RequestMapping(value = "/XssTest", method = RequestMethod.POST)
    public void XssTest(@RequestBody Review review){
        System.out.println("review id : " + review.getId());
        System.out.println("review Title : " + review.getTitle());
        System.out.println("review Content : " + review.getContent());
    }

}
