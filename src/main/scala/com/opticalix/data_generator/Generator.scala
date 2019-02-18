package com.opticalix.data_generator

import java.io.File

import com.opticalix.data_generator.utils.{JavaUtils, Utils}
import net.sourceforge.argparse4j.ArgumentParsers
import net.sourceforge.argparse4j.inf.ArgumentParserException

import scala.io.Source


object Generator {
  def main(args: Array[String]): Unit = {

    runEventGenerator(args)

  }

  /**
    * /Users/admin/Downloads/dota2Dataset/dota2Train.csv  22.4m
    * /Users/admin/Downloads/FiveCitiePMData/BeijingPM20100101_20151231.csv 3.2m
    * /Users/admin/Downloads/train.csv 61k
    * TODO output to kafka
    * @param args args
    */
  def runEventGenerator(args: Array[String]) = {
    //Parameters
    /// source: 逐行的事件源文件路径
    /// tps: 每秒发送多少行
    /// delay_nano: 每发送一行后延时多少纳秒
    /// mode: 数据生成时序类型. 0代表匀速
    val parser = ArgumentParsers.newFor("GenerateEvent").build()
    parser.addArgument("-s", "--source").help("Source file path")
    parser.addArgument("-t", "--tps").help("Transaction per second")
    parser.addArgument("-m", "--mode").choices("0", "1").help("Set mode")
    parser.addArgument("-d", "--delay_nano").help("Delay nanosecond")
    try {
      val ns = parser.parseArgs(args)
      val source = ns.getString("source")
      var tps = ns.getString("tps")
      var mode = ns.getString("mode")
      var delayNano = ns.getString("delay_nano")
      assert(source != null)
      if (tps == null)
        tps = "100"
      if (delayNano == null)
        delayNano = "0"
      if (mode == null)
        mode = "0"
      val startTime = System.currentTimeMillis()
      var cnt = 0
      //check mem cost. Do not read all text in mem
      println(s"source=$source(${JavaUtils.formatSize(new File(source).length())}), tps=$tps, delayNano=$delayNano, mode=$mode, startTime=${Utils.formatTimeNewApi(startTime, "yyyy-MM-dd HH:mm:ss.SSS")}")


      //TODO use tps. correct tps
//      var delay = 0
//      var offset = 0
//      var t1, t2 = 0L
//      for (line <- Source.fromFile(source).getLines()) {
//        SmartHomeEventStreamGenerator.output(line)
//
//        val measureCnt = 500
//        if (cnt <= measureCnt) {
//          if (cnt == 0) {
//            t1 = System.currentTimeMillis()
//          } else if (cnt == measureCnt) {
//            t2 = System.currentTimeMillis()
//            offset = (1000 * (t2 - t1) / measureCnt.toFloat).toInt
//            if (offset > 0) {
//              delay = 1000 * 1000 / tps.toInt
//              delay = Math.max(0, delay - offset)
//            }
//          }
//        } else {
//          Thread.sleep(delay / 1000, delay % 1000)
//        }
//        cnt += 1
//      }
//      val endTime = System.currentTimeMillis()
//      val costSec = (endTime - startTime) / 1000f
//      System.out.println(s"delay=${delay}ns, offset=${offset}ns, realTps=${cnt / costSec.toFloat}, totalCostTime=${costSec}s, endTime=${Utils.formatTimeNewApi(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}")


      for (line <- Source.fromFile(source).getLines()) {
        SmartHomeEventStreamGenerator.output(line)
        Thread.sleep(delayNano.toLong / 1000, (delayNano.toLong % 1000).toInt)
        cnt += 1
      }
      val endTime = System.currentTimeMillis()
      val costSec = (endTime - startTime) / 1000f
      System.out.println(s"delayNano=${delayNano}ns, realTps=${cnt / costSec.toFloat}, totalCostTime=${costSec}s, endTime=${Utils.formatTimeNewApi(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss.SSS")}")

      System.exit(0)
    }
    catch {
      case e: ArgumentParserException =>
        parser.handleError(e)
        System.exit(1)
      case _: Exception =>
        System.exit(1)
    }
  }

  def runIpGenerator(): Unit = {
    //    ip
    //    print(ApacheLogGenerator.provideSingleIP())
    //
    //    time
    //    val currDate = LocalDate.now()
    //    val zoneId = ZoneId.systemDefault()
    //    val millis = Duration.ofSeconds(3).get(ChronoUnit.SECONDS).toInt * 1000
    //    val todayStartTime = LocalDateTime.of(currDate.getYear,currDate.getMonthValue,currDate.getDayOfMonth, 0, 0)
    //    for (v <- 1 to 50)
    //      println(ApacheLogGenerator.provideSingleFormatTime("dd/MMM/yyyy:HH:mm:ss Z", todayStartTime, millis))
    //
    //    http
    //    for (v <- 1 to 50)
    //      println(ApacheLogGenerator.provideHttpInfo())
  }
}
