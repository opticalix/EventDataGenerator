package com.opticalix.data_generator.kafka;

public class KafkaConfig {
    public static String KAFKA_BROKERS = "localhost:9092";
    public static String TOPIC_NAME = "test";
    public static String CLIENT_ID = "client_1";
    public static String GROUP_ID = "group_1";
    public static Integer MAX_NO_MESSAGE_FOUND_COUNT = 100;
    public static Integer MAX_POLL_RECORDS = 1;
    public static String OFFSET_RESET_LATEST = "latest";
    public static String OFFSET_RESET_EARLIEST = "earliest";
    public static String KAFKA_PRODUCER_CONFIG_PATH;
    public static String KAFKA_CONSUMER_CONFIG_PATH;

    public static void setKafkaBrokers(String kafkaBrokers) {
        KAFKA_BROKERS = kafkaBrokers;
    }

    public static void setTopicName(String topicName) {
        TOPIC_NAME = topicName;
    }

    public static void setKafkaProducerConfigPath(String kafkaProducerConfigPath) {
        KAFKA_PRODUCER_CONFIG_PATH = kafkaProducerConfigPath;
    }

    public static void setKafkaConsumerConfigPath(String kafkaConsumerConfigPath) {
        KAFKA_CONSUMER_CONFIG_PATH = kafkaConsumerConfigPath;
    }

}