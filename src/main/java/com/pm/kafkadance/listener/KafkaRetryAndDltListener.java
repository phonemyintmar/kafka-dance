package com.pm.kafkadance.listener;

import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;

public class KafkaRetryAndDltListener {


    // It is said that
    // The default back off policy is FixedBackOffPolicy with a maximum of 3 attempts and 1000ms intervals.
    // so if we dont do anything, maybe this will kick in if we just say we want retry and dlt

//    @RetryableTopic(kafkaTemplate = "myRetryableTopicKafkaTemplate")
    @RetryableTopic(attempts = "5",
            backoff = @Backoff(delay = 1000, multiplier = 2, maxDelay = 5000))
    @KafkaListener(topics = "my-annotated-topic", groupId = "myGroupId")
    public void processMessage(String message) {
        // ... message processing
    }

// You can specify the method used to process the DLT for the topic, as well as the behavior if that processing fails.
// To do that you can use the @DltHandler annotation in a method of the class with the @RetryableTopic annotation(s). \
// Note that the same method will be used for all the @RetryableTopic annotated methods within that class.
// So ae tok, d class htl mhr shi tae retryable class a twet a kone lone ko d dlthandler ka pl handle mhr.
// more at here (https://docs.spring.io/spring-kafka/reference/retrytopic/dlt-strategies.html). Version ka tok lat shi 3.1.1 lr ma thi wo.

    @DltHandler
    public void processDltMessage(String message) {
        // ... message processing, persistence, etc
    }



    /**
     * Should the DLT processing fail, there are two possible behaviors available: ALWAYS_RETRY_ON_ERROR and FAIL_ON_ERROR.
     *
     * In the former the record is forwarded back to the DLT topic so it doesn’t block other DLT records' processing. In the latter the consumer ends the execution without forwarding the message.
     *
     * @RetryableTopic(dltProcessingFailureStrategy =
     *             DltStrategy.FAIL_ON_ERROR)
     * @KafkaListener(topics = "my-annotated-topic")
     * public void processMessage(MyPojo message) {
     *     // ... message processing
     * }
     *
     * Copied!
     * @Bean
     * public RetryTopicConfiguration myRetryTopic(KafkaTemplate<Integer, MyPojo> template) {
     *     return RetryTopicConfigurationBuilder
     *             .newInstance()
     *             .dltHandlerMethod("myCustomDltProcessor", "processDltMessage")
     *             .doNotRetryOnDltFailure()
     *             .create(template);
     * }
     */

    /**
     * The default behavior is to ALWAYS_RETRY_ON_ERROR.
     * Starting with version 2.8.3, ALWAYS_RETRY_ON_ERROR will NOT route a record back to the DLT if the record causes a fatal exception to be thrown,
     * such as a DeserializationException, because, generally, such exceptions will always be thrown.
     * Exceptions that are considered fatal are:
     *
     * DeserializationException
     *
     * MessageConversionException
     *
     * ConversionException
     *
     * MethodArgumentResolutionException
     *
     * NoSuchMethodException
     *
     * ClassCastException
     */

//    so maybe it is a good practice to just send strings? or what? if we send a pojo class and cannot deserialize can we even log it?
//    we also needs to test it.
//    anyway, the point is we just config the retry and dlt topics with a good logic and we are good to go. LETS GOOOO!
}
