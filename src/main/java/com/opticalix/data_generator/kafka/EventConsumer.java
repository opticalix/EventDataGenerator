package com.opticalix.data_generator.kafka;

import com.opticalix.common.pojo.SmartHomeRawEvent;
import com.opticalix.data_generator.utils.Utils;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Properties;

/**
 * @author Felix
 * @date 19/02/2019 10:30 AM
 * @email opticalix@gmail.com
 */
public class EventConsumer extends KafkaConsumer<Long, SmartHomeRawEvent> implements Runnable {
    private volatile long mTotalRecordSentCnt;
    private long mConsumeRecordCnt;

    public EventConsumer(Properties properties) {
        super(properties);
    }

    public void setTotalRecordSentCnt(long totalRecordSentCnt) {
        mTotalRecordSentCnt = totalRecordSentCnt;
    }

    @Override
    public void run() {
        int noMessageFound = 0;
        while (true) {
            ConsumerRecords<Long, SmartHomeRawEvent> consumerRecords = this.poll(Duration.ofSeconds(1));
            // 1000 is the time in milliseconds consumer will wait if no record is found at broker.
            // 超过阈值次未收到消息 或 消费记录数超过总数 都会停止loop
            if (consumerRecords.count() == 0) {
                if (mTotalRecordSentCnt != 0 && mConsumeRecordCnt >= mTotalRecordSentCnt) {
                    System.out.println("mConsumeRecordCnt=" + mConsumeRecordCnt + ", mTotalRecordSentCnt=" + mTotalRecordSentCnt + ", exit loop");
                    break;
                }
                noMessageFound++;
                System.out.println("noMessageFound++ -> " + noMessageFound);
                if (noMessageFound > KafkaConfig.MAX_NO_MESSAGE_FOUND_COUNT) {
                    // If no message found count is reached to threshold exit loop.
                    System.out.println("noMessageFound reach threshold=" + KafkaConfig.MAX_NO_MESSAGE_FOUND_COUNT + ", exit loop");
                    break;
                } else {
                    continue;
                }
            }
            //print each record.
            mConsumeRecordCnt += consumerRecords.count();
            consumerRecords.forEach(record -> {
                System.out.println("Record Key:" + record.key() + ", value:" + record.value() + ", partition:" + record.partition() + ", offset:" + record.offset());
            });
            // commits the offset of record to broker.
            this.commitAsync();
        }
        this.close();

        System.out.println("Kafka consume end: endTime=" + Utils.formatTimeNewApi(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS"));
        System.exit(0);
    }
}
