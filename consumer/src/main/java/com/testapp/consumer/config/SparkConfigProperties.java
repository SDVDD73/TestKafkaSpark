package com.testapp.consumer.config;

import java.time.Duration;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SparkConfigProperties {
    @Value("${spring.application.name}")
    private String sparkAppName;

    @Value("${spark.master}")
    private String sparkMaster;

    @Value("${spark.stream.kafka.durations}")
    private Duration streamDurationTime;

    @Value("${spark.driver.memory}")
    private String sparkDriverMemory;

    @Value("${spark.worker.memory}")
    private String sparkWorkerMemory;

    @Value("${spark.executor.memory}")
    private String sparkExecutorMemory;

    @Value("${spark.rpc.message.maxSize}")
    private String sparkRpcMessageMaxSize;
}
