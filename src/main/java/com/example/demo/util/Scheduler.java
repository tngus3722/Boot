package com.example.demo.util;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

   // @Scheduled(fixedDelay = 500)
    public void myScheduler(){
        System.out.println("Scheduled test : thread no = " + Thread.currentThread().getName());
    }
}
