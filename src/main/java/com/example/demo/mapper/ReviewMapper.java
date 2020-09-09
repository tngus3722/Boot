package com.example.demo.mapper;

import com.example.demo.domain.Review;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReviewMapper {

    public List<Review> display(Integer fish_id);
}
