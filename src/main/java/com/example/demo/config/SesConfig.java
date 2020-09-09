package com.example.demo.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SesConfig {

    @Value("${ses.access-key}")
    String AWS_KEY_ID;

    @Value("${ses.secret-key}")
    String AWS_KEY_SECRET;

    @Bean
    public AmazonSimpleEmailServiceAsync amazonSimpleEmailServiceAsync() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(AWS_KEY_ID, AWS_KEY_SECRET);

        return AmazonSimpleEmailServiceAsyncClient.asyncBuilder()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withRegion(Regions.US_EAST_2)
                .build();
    }
}
