import java.util.Properties
import java.util.concurrent.Future

import Consumer._
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}

object Producer extends App {
  def props(host: String) = {
    val props = new Properties()
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerSerializer")
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host)
    props
  }

  val producer = new KafkaProducer[Integer, String](props(args(0)))
  1 to 100000 foreach { i =>
    print(s"produce $i ...")
    val future: Future[RecordMetadata] = producer.send(new ProducerRecord[Integer, String](args(1), i, Integer.toString(i)))
    val result: RecordMetadata = future.get()
    println(s"offset ${result.offset} / partition ${result.partition} / topic ${result.topic}")
    Thread.sleep(100)
  }

}
