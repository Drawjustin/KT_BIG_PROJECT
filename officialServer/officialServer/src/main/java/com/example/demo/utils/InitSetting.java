package com.example.demo.utils;

import com.example.demo.entity.User;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitSetting {
    private final InitService initService;

    @PostConstruct
    public void init(){initService.init();}

    @Component
    static class InitService{
        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init(){
            em.persist(new User("guswhd903","1234","현종","010-8823-2591","guswhd903@naver.com"));

        }
    }
}
