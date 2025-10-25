package dev.str.electricsim.producers;

import dev.str.electricsim.model.CammesaRecord;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CammesaConsumptionProducer {
    Logger log = LoggerFactory.getLogger(CammesaConsumptionProducer.class);
    private final KafkaTemplate<String, CammesaRecord> template;
    private final NewTopic camessaConsumptionTopic;

    public CammesaConsumptionProducer(KafkaTemplate<String, CammesaRecord> template, NewTopic camessaConsumptionTopic) {
        this.template = template;
        this.camessaConsumptionTopic = camessaConsumptionTopic;
    }

    public void send(CammesaRecord cammesaRecord) {
        log.info("Sending message to Camessa Consumption topic: {}", cammesaRecord);
        template.send(camessaConsumptionTopic.name(), cammesaRecord);
    }

}
