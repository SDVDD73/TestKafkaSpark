package com.testapp.consumer.config;

import lombok.RequiredArgsConstructor;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SparkConfig {
    private final SparkConfigProperties sparkConfigProperties;

    @Bean
    public SparkConf sparkConf() {
        return new SparkConf()
                .setAppName(sparkConfigProperties.getSparkAppName())
                .setMaster(sparkConfigProperties.getSparkMaster())
                .set("spark.executor.memory", sparkConfigProperties.getSparkExecutorMemory());
    }
}



