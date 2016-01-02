package com.utils;

import com.job.BirthdayJob;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class JobExecutor {

    @Bean
    public BirthdayJob bean() {
        return new BirthdayJob();
    }

    public static void main(String args[]) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(JobExecutor.class);
    }
}
