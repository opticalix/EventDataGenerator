package com.opticalix.data_generator

import java.io.File

import com.opticalix.common.pojo.ManufactureEvent
import com.opticalix.common.utils.JavaUtils
import com.opticalix.data_generator.kafka.KafkaManager
import com.opticalix.data_generator.utils.Utils
import net.sourceforge.argparse4j.ArgumentParsers
import net.sourceforge.argparse4j.inf.ArgumentParserException

import scala.io.Source


object Generator {
  def main(args: Array[String]): Unit = {
    runEventGenerator(args)
  }

  /**
    * usage:
    *  java -jar $jar -s $source -d $delayNano -m $mode
    *
    * 看需要优化哪些（kafka消费速度、控制disorder开关--用MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION可实现）
    * 更改EventType，需要1修改runProducer泛型 2实现并更改config文件中serializer 3修改produceXXRecord方法
    * @param args args
    */
  def runEventGenerator(args: Array[String]) = {
    //Parameters
    /// source: 逐行的事件源文件路径
    /// delay_nano: 每发送一行后延时多少纳秒
    val parser = ArgumentParsers.newFor("GenerateEvent").build()
    parser.addArgument("-s", "--source").help("Source file path")
    parser.addArgument("-d", "--delay_nano").help("Delay nanosecond")
    parser.addArgument("-p", "--producer_config").help("Kafka producer config properties file path")
    parser.addArgument("-c", "--consumer_config").help("Kafka consumer config properties file path")
    parser.addArgument("-t", "--topic").help("Topic name")
    try {
      val ns = parser.parseArgs(args)
      val source = ns.getString("source")
      var delayNano = ns.getString("delay_nano")
      assert(source != null)
      if (delayNano == null)
        delayNano = "0"
      println(s"source=$source(${JavaUtils.formatSize(new File(source).length())}), delayNano=$delayNano")
      //handle kafka
      val producerConf = ns.getString("producer_config")
      val consumerConf = ns.getString("consumer_config")
      val topic = ns.getString("topic")

      KafkaManager.setProducerConfigPath(producerConf)
      KafkaManager.setConsumerConfigPath(consumerConf)
      KafkaManager.setTopic(topic)
      println(s"producerConf=$producerConf, consumerConf=$consumerConf, topic=$topic")
      //call generic method in java
      val producer = KafkaManager.runProducer[ManufactureEvent]()

      //read file
      //check mem cost. Do not read all text in mem
      val startTime = System.currentTimeMillis()
      var cnt = 0
      println(s"startTime=${Utils.formatTimeNewApi(startTime, "yyyy-MM-dd HH:mm:ss.SSS")}")
      for (line <- Source.fromFile(source).getLines()) {
        KafkaManager.produceManufactureRecord(producer, line)

        if (delayNano.toLong > 0L)
          Thread.sleep(delayNano.toLong / 1000, (delayNano.toLong % 1000).toInt)
        cnt += 1
      }
      val endTime = System.currentTimeMillis()
      val costSec = (endTime - startTime) / 1000f
      System.out.println(s"Read stage: delayNano=${delayNano}ns, realTps=${cnt / costSec.toFloat}, totalCostTime=${costSec}s, endTime=${Utils.formatTimeNewApi(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}")

      //consumer需要flink-cep实现
      val runLocalConsumer = false
      if (runLocalConsumer) {
//        val consumer = KafkaManager.runConsumer()
//        consumer.setTotalRecordSentCnt(cnt)
      } else {
        System.exit(0)
      }
    }
    catch {
      case e: ArgumentParserException =>
        parser.handleError(e)
        System.exit(1)
      case _: Exception =>
        System.exit(1)
    }
  }
}
