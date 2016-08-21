import java.util
import java.util.Properties
import java.util.concurrent.Executors

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.clients.producer.ProducerConfig

import scala.collection.JavaConverters._

object Consumer extends App {
  def props(host: String) = {
    val props = new Properties()
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "elo-group")
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer")
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host)
    props
  }

  // Let's simulate several consumers in parallel
  val N = 5
  val tpool = Executors.newFixedThreadPool(N)

  def createConsumerThread(id: String) = new Runnable {
    override def run(): Unit = {
      val consumer = new KafkaConsumer[Integer, String](props(args(0)))
      val topics = consumer.listTopics.asScala
      println(topics.keys)

      consumer.subscribe(util.Arrays.asList(args(1)))
      var i = 0
      while (true) {
        val records: ConsumerRecords[Integer, String] = consumer.poll(1000)
        records.asScala.foreach(x => println(s"${id}: ${i} : $x"))
        i += 1
      }
    }
  }

  for { i <- 0 until N } {
    tpool.submit(createConsumerThread(s"consumer ${i}"))
  }

}
