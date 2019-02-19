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

    public static void setKafkaBrokers(String kafkaBrokers) {
        KAFKA_BROKERS = kafkaBrokers;
    }

    public static void setTopicName(String topicName) {
        TOPIC_NAME = topicName;
    }

    public static void setClientId(String clientId) {
        CLIENT_ID = clientId;
    }

    public static void setGroupId(String groupId) {
        GROUP_ID = groupId;
    }

    public static void printConfigInfo() {
        System.out.println(
                "printConfigInfo:\n" +
                "KAFKA_BROKERS:" + KAFKA_BROKERS + ", " +
                "TOPIC_NAME:" + TOPIC_NAME + ", " +
                "GROUP_ID:" + GROUP_ID + ", " +
                "CLIENT_ID:" + CLIENT_ID
        );
    }
}