package com.carbon.todobackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.carbon.todobackend.dao")
public class TodoBackEndApplicationDatabaseConfiguration {
}
