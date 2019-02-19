package com.opticalix.data_generator.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

/**
 * @author Felix
 * @date 18/02/2019 12:15 PM
 * @email opticalix@gmail.com
 */
public class BaseJsonDeserializer<T> implements Deserializer<T> {
    private Class<T> clazz;

    public BaseJsonDeserializer(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public T deserialize(String topic, byte[] data) {
        T retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.readValue(new String(data), clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public void close() {

    }
}
