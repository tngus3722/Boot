package com.example.demo.service;

import com.example.demo.domain.Review;
import com.example.demo.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    public List<Review> display(Integer fish_id) {
        return reviewMapper.display(fish_id);
    }
}
