package com.pm.kafkadance.listener;

import com.pm.kafkadance.dto.TestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestListener {

    @KafkaListener(topics = "demo",
            containerFactory = "kafkaListenerContainerFactory")
    public void listenGroupFoo(TestDto message) {

        try {
            log.info("Received Message : {}", message);

            // Process the message
        } catch (Exception e) {
            log.info("abcdefg");
            // Handle the error, e.g., log it, perform retries, or take other actions
        }

    }

    @KafkaHandler
    public void testHanlder(){
        log.info("handled");
    }



}
