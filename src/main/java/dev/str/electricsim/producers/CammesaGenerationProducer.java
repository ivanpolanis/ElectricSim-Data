package dev.str.electricsim.producers;

import dev.str.electricsim.model.CammesaRecord;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CammesaGenerationProducer {
    private final Logger log = LoggerFactory.getLogger(CammesaGenerationProducer.class);
    private final KafkaTemplate<String, CammesaRecord> template;
    private final NewTopic cammesaGenerationTopic;

    public CammesaGenerationProducer(KafkaTemplate<String, CammesaRecord> template, NewTopic cammesaGenerationTopic) {
        this.template = template;
        this.cammesaGenerationTopic = cammesaGenerationTopic;
    }

    public void send(CammesaRecord cammesaRecord) {
        log.info("Sending message to Camessa Generation topic: {}", cammesaRecord);
        template.send(cammesaGenerationTopic.name(), cammesaRecord);
    }

}
