package com.fitmate.domain.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class QueryDslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory((entityManager));
    }

    //초성 검색시 SQL 수기 작성시 필요한 Entity Manager 를 반환한다.
    public EntityManager getEntityManager(){
        return entityManager;
    }

}
