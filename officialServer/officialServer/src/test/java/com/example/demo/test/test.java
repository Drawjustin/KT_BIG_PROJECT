package com.example.demo.test;

import com.example.demo.entity.User;
import com.example.demo.repository.TestRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Commit;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Commit
public class test {

    @PersistenceContext
    EntityManager em;

    @Autowired
    TestRepository testRepository;
    @Test
    public void test01(){
        assertThat(testRepository.findUserById("guswhd903").getUserId()).isEqualTo("guswhd903");

    }

}
