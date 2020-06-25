package com.testapp.consumer.service.impl;

import com.testapp.consumer.config.KafkaConsumerConfig;
import com.testapp.consumer.config.KafkaProperties;
import com.testapp.consumer.config.SparkConfigProperties;
import com.testapp.consumer.service.SparkConsumerService;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SparkConsumerServiceImpl implements SparkConsumerService {

    private final SparkConf sparkConf;

    private final KafkaConsumerConfig kafkaConsumerConfig;

    private final KafkaProperties kafkaProperties;

    private final SparkConfigProperties sparkConfigProperties;

    @Override
    public void run() {
        log.debug("Run spark consumer service...");

        // Create context with a DurationTime seconds batch interval
        JavaStreamingContext jsc = new JavaStreamingContext(sparkConf,
                Durations.seconds(sparkConfigProperties.getStreamDurationTime().getSeconds()));

        // Create direct kafka stream with brokers and topics
        JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream(
                jsc,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(
                        kafkaProperties.getDefaultTopics()
                        , kafkaConsumerConfig.consumerConfigs()));

        JavaDStream<String> lines = stream.map(ConsumerRecord::value);
        //Count the tweets and print

        Long durationTime = sparkConfigProperties.getStreamDurationTime().getSeconds();
        lines
                .count()
                .map(cnt -> "Messages count in last " + durationTime + " seconds: " + cnt + "\n\r" +
                        "_____________________________________________________")
                .print();

        // подсчет количества слов в payload сообщения
       /*lines
               .flatMap(SparkConsumerServiceImpl::splitBySpace)
               .mapToPair(hashTag -> new Tuple2<>(hashTag, 1))
                .reduceByKey(Integer::sum)
                .mapToPair(Tuple2::swap)
                .foreachRDD(rdd -> {
                    log.info("---------------------------------------------------------------");
                   //Counts
                    rdd.sortByKey(false).collect()
                            .forEach(record -> {
                                log.info("=== === ===");
                               log.info(String.format("payload data word: '%s', count: (%d)", record._2, record._1));
                           });
                });*/


      /* lines
               .flatMap(SparkConsumerServiceImpl::splitBySpace)
               .mapToPair(hashTag -> new Tuple2<>(hashTag, 1))
                .reduceByKey(Integer::sum)
                .mapToPair(Tuple2::swap)
                .foreachRDD(rdd -> {
                    log.info("---------------------------------------------------------------");
                   //Counts
                    rdd.sortByKey(false).collect()
                            .forEach(record -> {
                                log.info("=== === ===");
                               log.info(String.format("payload data word: '%s', count: (%d)", record._2, record._1));
                           });
                });*/


        stream.map(record -> {
            System.out.println("#############");
            return record.value();
        }).count();

        lines.foreachRDD(rdd -> {
            log.info("begin item lines: {}", rdd);
            rdd.collect().forEach(item -> log.info("item lines: {}", item));
        });

        stream.foreachRDD(rdd -> {
            log.info("item stream: {}", rdd);
        });

        log.info("-----------------------");


        // Start the computation
        jsc.start();

        try {
            jsc.awaitTermination();
        } catch (InterruptedException e) {
            log.error("Interrupted: {}", e.getMessage());
            // Restore interrupted state...
        }
    }

    private static Iterator<String> splitBySpace(String text) {
        List<String> lines = Arrays.asList(text.split(" "));
        return lines.iterator();
    }

}
