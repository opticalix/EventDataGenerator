package com.opticalix.data_generator.kafka;

import com.opticalix.data_generator.pojo.SmartHomeRawEvent;

/**
 * @author Felix
 * @date 18/02/2019 12:20 PM
 * @email opticalix@gmail.com
 */
public class EventDeserializer extends BaseJsonDeserializer<SmartHomeRawEvent> {

    public EventDeserializer() {
        super(SmartHomeRawEvent.class);
    }
}
