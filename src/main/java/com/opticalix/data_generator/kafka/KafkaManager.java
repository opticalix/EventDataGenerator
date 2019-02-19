package com.opticalix.data_generator.kafka;

import com.opticalix.data_generator.pojo.SmartHomeRawEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * @author Felix
 * @date 18/02/2019 11:48 AM
 * @email opticalix@gmail.com
 */
public class KafkaManager {

    public static void setConfig(String... configs) {
        if (configs.length > 0) {
            KafkaConfig.setKafkaBrokers(configs[0]);
        }
        if (configs.length > 1) {
            KafkaConfig.setTopicName(configs[1]);
        }
        KafkaConfig.printConfigInfo();
    }

    public static KafkaProducer<Long, SmartHomeRawEvent> runProducer() {
        return createProducer();
    }

    public static void produceRecord(KafkaProducer<Long, SmartHomeRawEvent> producer, String line) {
        if (producer == null)
            return;
        ProducerRecord<Long, SmartHomeRawEvent> record = new ProducerRecord<Long, SmartHomeRawEvent>(KafkaConfig.TOPIC_NAME, convert2Event(line));
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

    //problem: consumer config is printed, but 'createConsumer' not print. need try-catch to show exception msg
    public static EventConsumer runConsumer() {
        EventConsumer consumer = createConsumer();
        if (consumer != null) {
            Executors.newSingleThreadExecutor().submit(consumer);
        }
        return consumer;
    }

    public static SmartHomeRawEvent convert2Event(String rawLine) {
        String comma = ",";
        String[] split = rawLine.split(comma);
        long id = Long.parseLong(split[0]);
        long timestamp = Long.parseLong(split[1]);
        float value = Float.parseFloat(split[2]);
        byte property = Byte.parseByte(split[3]);
        int plugId = Integer.parseInt(split[4]);
        int householdId = Integer.parseInt(split[5]);
        int houseId = Integer.parseInt(split[6]);
        return new SmartHomeRawEvent(id, timestamp, value, property, plugId, householdId, houseId);
    }

    public static EventConsumer createConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.KAFKA_BROKERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConfig.GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "com.opticalix.data_generator.kafka.EventDeserializer");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, KafkaConfig.MAX_POLL_RECORDS);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KafkaConfig.OFFSET_RESET_LATEST);
        try {
            EventConsumer consumer = new EventConsumer(props);
            consumer.subscribe(Collections.singletonList(KafkaConfig.TOPIC_NAME));
            System.out.println("createConsumer success");
            return consumer;
        } catch (Exception e) {
            System.out.println("createConsumer fail");
            e.printStackTrace();
            return null;
        }
    }

    public static KafkaProducer<Long, SmartHomeRawEvent> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.KAFKA_BROKERS);
//        props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaConfig.CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "com.opticalix.data_generator.kafka.EventSerializer");
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);//>1 result in disorder
//        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class.getName());
        try {
            KafkaProducer<Long, SmartHomeRawEvent> producer = new KafkaProducer<>(props);
            System.out.println("createProducer success");
            return producer;
        } catch (Exception e) {
            System.out.println("createProducer fail");
            e.printStackTrace();
            return null;
        }
    }
}
