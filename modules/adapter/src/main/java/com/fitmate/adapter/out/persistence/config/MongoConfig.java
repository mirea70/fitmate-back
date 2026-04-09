package com.fitmate.adapter.out.persistence.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.fitmate.adapter.out.persistence.mongo")
@org.springframework.context.annotation.Profile("!test")
public class MongoConfig {
}
