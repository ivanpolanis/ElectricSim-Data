package dev.str.electricsim.producers;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CammesaProducer {
    Logger log = LoggerFactory.getLogger(CammesaProducer.class);
    private final KafkaTemplate<String, String> template;
    private final NewTopic camessaTopic;

    public CammesaProducer(KafkaTemplate<String, String> template, NewTopic camessaTopic) {
        this.template = template;
        this.camessaTopic = camessaTopic;
    }

    public void send(String data) {
        log.info("Sending message to Camessa topic: {}", data);
        template.send(camessaTopic.name(), data);
    }

}
