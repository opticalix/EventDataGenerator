package com.opticalix.data_generator

import java.io.File

import com.opticalix.data_generator.kafka.{KafkaConfig, KafkaManager}
import com.opticalix.data_generator.utils.{JavaUtils, Utils}
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
    * /Users/admin/Downloads/dota2Dataset/dota2Train.csv  22.4m
    * /Users/admin/Downloads/FiveCitiePMData/BeijingPM20100101_20151231.csv 3.2m
    * /Users/admin/Downloads/train.csv 61k
    * /Users/admin/Documents/Felix/master/dataset/test_20_ascend.csv
    *
    * @param args args
    */
  def runEventGenerator(args: Array[String]) = {
    //Parameters
    /// source: 逐行的事件源文件路径
    /// delay_nano: 每发送一行后延时多少纳秒
    /// mode: 数据生成时序类型. 默认0代表匀速
    val parser = ArgumentParsers.newFor("GenerateEvent").build()
    parser.addArgument("-s", "--source").help("Source file path")
    parser.addArgument("-m", "--mode").help("Set mode")
    parser.addArgument("-d", "--delay_nano").help("Delay nanosecond")
    parser.addArgument("-b", "--bootstrap_servers").help("Bootstrap servers, like: host1:port1,host2:port2,...")
    parser.addArgument("-t", "--topic").help("Topic name")
//    parser.addArgument("-g", "--group_id").help("Group ID for consumer")
    try {
      val ns = parser.parseArgs(args)
      val source = ns.getString("source")
      var mode = ns.getString("mode")
      var delayNano = ns.getString("delay_nano")
      assert(source != null)
      if (delayNano == null)
        delayNano = "0"
      if (mode == null)
        mode = "0"
      //handle kafka
      val bs = ns.getString("bootstrap_servers")
      val topic = ns.getString("topic")
      println(s"source=$source(${JavaUtils.formatSize(new File(source).length())}), delayNano=$delayNano, mode=$mode, bootstrap_servers=$bs, topic=$topic")

      KafkaManager.setConfig(bs, topic)
      val producer = KafkaManager.runProducer()
      val consumer = KafkaManager.runConsumer()

      //read file
      //check mem cost. Do not read all text in mem
      val startTime = System.currentTimeMillis()
      var cnt = 0
      println(s"startTime=${Utils.formatTimeNewApi(startTime, "yyyy-MM-dd HH:mm:ss.SSS")}")
      for (line <- Source.fromFile(source).getLines()) {
        KafkaManager.produceRecord(producer, line)

        if (delayNano.toLong > 0L)
          Thread.sleep(delayNano.toLong / 1000, (delayNano.toLong % 1000).toInt)
        cnt += 1
      }
      val endTime = System.currentTimeMillis()
      val costSec = (endTime - startTime) / 1000f
      System.out.println(s"Read stage: delayNano=${delayNano}ns, realTps=${cnt / costSec.toFloat}, totalCostTime=${costSec}s, endTime=${Utils.formatTimeNewApi(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}")

      consumer.setTotalRecordSentCnt(cnt)
//      System.exit(0)
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
