package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.TestRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TestService {
    private final TestRepository testRepository;
    public User test01(String id){
        return testRepository.findUserById(id);
    }

}
