package com.pm.kafkadance.producer;

import com.pm.kafkadance.dto.TestDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class TestProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public TestProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.onSend("Hello World");
    }

    public String onSend(String message) {
        kafkaTemplate.send("demo", new TestDto("a","b"));
        kafkaTemplate.send("demo", "Test")
                .handle((result, ex) -> {
                    if (ex != null) {
                        // Handle the exception
                        System.err.println("Error sending message: " + ex.getMessage());
                        // You can also throw a custom exception or return a default result here
                    } else {
                        // Handle the successful result
                        System.out.println("Message sent successfully. Topic: " + result.getProducerRecord().topic());
                    }
                    return null;
                });
        return "done";
    }
}
