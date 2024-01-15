package com.pm.kafkadance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.DeadLetterPublishingRecovererFactory;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationSupport;
import org.springframework.lang.NonNullApi;

import java.util.function.Consumer;

@EnableKafka
@Configuration
public class KafkaRetryConfig extends RetryTopicConfigurationSupport {

    // this is necessary if the retry topic have fewer partition than the main topic
    // because when we send to the retry or dlt, we send to the same partition tae
    // abit confused by this actually, because of the naming and everything, why is that named configuredltpublishing?
    @Override
    protected Consumer<DeadLetterPublishingRecovererFactory> configureDeadLetterPublishingContainerFactory() {
        return dlprf -> dlprf.setPartitionResolver((cr, nextTopic) -> null);
    }

//    you can also use different number of partitions to be the same as main topic as follow
//    @RetryableTopic(numPartitions = 2, replicationFactor = 3)
//    use that in the listener method i guess.


    /**
     * You can also configure the non-blocking retry support by creating RetryTopicConfiguration beans in a @Configuration annotated class.
     *
     * @Bean public RetryTopicConfiguration myRetryTopic(KafkaTemplate<String, Object> template) {
     * return RetryTopicConfigurationBuilder
     * .newInstance()
     * .create(template);
     * }
     * <p>
     * Copied!
     * This will create retry topics and a dlt, as well as the corresponding consumers, for all topics in methods annotated with @KafkaListener using the default configurations.
     * The KafkaTemplate instance is required for message forwarding.
     */

    @Bean
    public RetryTopicConfiguration myRetryTopic(KafkaTemplate<String, Object> template) {
        return RetryTopicConfigurationBuilder
                .newInstance()
                .create(template);
    }

//    we can use different retrytopicconfig beans with different kafkatemplates, for different topics tae byr.

    @Bean
    public RetryTopicConfiguration myRetryTopic(KafkaTemplate<String, MyPojo> template) {
        return RetryTopicConfigurationBuilder
                .newInstance()
                .fixedBackOff(3000)
                .maxAttempts(5)
                .concurrency(1)
                .includeTopics("my-topic", "my-other-topic")
                .create(template);
    }

    @Bean
    public RetryTopicConfiguration myOtherRetryTopic(KafkaTemplate<String, MyOtherPojo> template) {
        return RetryTopicConfigurationBuilder
                .newInstance()
                .exponentialBackoff(1000, 2, 5000)
                .maxAttempts(4)
                .excludeTopics("my-topic", "my-other-topic")
                .retryOn(MyException.class)
                .create(template);
    }

//    error tok tat ny tl d a tine kuu cha htr tr.

    /**
     *
     The retry topics' and dlt’s consumers will be assigned to a consumer group with a group id that is the combination of the one which you provide in the groupId parameter of the @KafkaListener annotation with the topic’s suffix.
     If you don’t provide any they’ll all belong to the same group, and rebalance on a retry topic will cause an unnecessary rebalance on the main topic.
     */

    /**
     * Multiple @KafkaListener annotations can be used for the same topic with or without manual partition assignment along with non-blocking retries,
     * but only one configuration will be used for a given topic. It’s best to use a single RetryTopicConfiguration bean for configuration of such topics; if multiple @RetryableTopic annotations are being used for the same topic, all of them should have the same values,
     * otherwise one of them will be applied to all of that topic’s listeners and the other annotations' values will be ignored.
     */

//    so, topic takhu si a twet listener twy a myr G shi loh ya pin mae retry config nae logic ka takhu pl ya ml tae.
}
