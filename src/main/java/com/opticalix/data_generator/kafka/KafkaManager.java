package com.opticalix.data_generator.kafka;

import com.opticalix.common.pojo.ManufactureEvent;
import com.opticalix.common.pojo.SmartHomeRawEvent;
import com.opticalix.common.utils.IOUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author Felix
 * @date 18/02/2019 11:48 AM
 * @email opticalix@gmail.com
 */
public class KafkaManager {

    @Deprecated
    public static void setConfig(String... configs) {
        if (configs.length > 0) {
            KafkaConfig.setKafkaBrokers(configs[0]);
        }
        if (configs.length > 1) {
            KafkaConfig.setTopicName(configs[1]);
        }
    }

    public static void setProducerConfigPath(String path) {
        KafkaConfig.setKafkaProducerConfigPath(path);
    }

    public static void setConsumerConfigPath(String path) {
        KafkaConfig.setKafkaConsumerConfigPath(path);
    }

    public static void setTopic(String topic) {
        KafkaConfig.setTopicName(topic);
    }

    public static <EventType> KafkaProducer<Long, EventType> runProducer() {
        return createProducer();
    }

    public static void produceSmartHomeRecord(KafkaProducer<Long, SmartHomeRawEvent> producer, String line) {
        if (producer == null)
            return;
        ProducerRecord<Long, SmartHomeRawEvent> record = new ProducerRecord<>(KafkaConfig.TOPIC_NAME, SmartHomeRawEvent.convert2SmartHomeEvent(line));
        try {
            RecordMetadata metadata = producer.send(record).get();
            System.out.println("Record sent with key " + record.key() + " to partition " + metadata.partition()
                    + " with offset " + metadata.offset());
        }
        catch (ExecutionException | InterruptedException e) {
            System.out.println("Error in sending record");
            e.printStackTrace();
        }
    }

    public static void produceManufactureRecord(KafkaProducer<Long, ManufactureEvent> producer, String line) {
        if (producer == null)
            return;
        ProducerRecord<Long, ManufactureEvent> record = new ProducerRecord<>(KafkaConfig.TOPIC_NAME, ManufactureEvent.convert2ManufactureEvent(line));
        try {
            RecordMetadata metadata = producer.send(record).get();
            System.out.println("Record sent with key " + record.key() + " to partition " + metadata.partition()
                    + " with offset " + metadata.offset());
        }
        catch (ExecutionException | InterruptedException e) {
            System.out.println("Error in sending record");
            e.printStackTrace();
        }
    }

    public static <EventType> KafkaProducer<Long, EventType> createProducer() {
        Properties props = IOUtils.readProperties(KafkaConfig.KAFKA_PRODUCER_CONFIG_PATH);
        try {
            KafkaProducer<Long, EventType> producer = new KafkaProducer<>(props);
            System.out.println("createProducer success");
            return producer;
        } catch (Exception e) {
            System.out.println("createProducer fail");
            e.printStackTrace();
            return null;
        }
    }

    //problem: consumer config is printed, but 'createConsumer' not print. need try-catch to show exception msg
//    public static EventConsumer runConsumer() {
//        EventConsumer consumer = createConsumer();
//        if (consumer != null) {
//            Executors.newSingleThreadExecutor().submit(consumer);
//        }
//        return consumer;
//    }

//    public static EventConsumer createConsumer() {
//        Properties props = new Properties();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.KAFKA_BROKERS);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConfig.GROUP_ID);
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "com.opticalix.data_generator.kafka.EventDeserializer");
//        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, KafkaConfig.MAX_POLL_RECORDS);
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE);
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KafkaConfig.OFFSET_RESET_LATEST);
//        try {
//            EventConsumer consumer = new EventConsumer(props);
//            consumer.subscribe(Collections.singletonList(KafkaConfig.TOPIC_NAME));
//            System.out.println("createConsumer success");
//            return consumer;
//        } catch (Exception e) {
//            System.out.println("createConsumer fail");
//            e.printStackTrace();
//            return null;
//        }
//    }
}
