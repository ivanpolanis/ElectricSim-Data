package dev.str.electricsim.config.kafka;

import dev.str.electricsim.config.YamlPropertySourceFactory;
import dev.str.electricsim.config.kafka.model.TopicProperties;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@PropertySource(value = "classpath:topics.yaml", encoding = "UTF-8", factory = YamlPropertySourceFactory.class)
public class TopicConfig {
    Logger logger = LoggerFactory.getLogger(TopicConfig.class);

    @Bean
    public NewTopic camessaConsumptionTopic(cammesaConsumptionTopicProperties cammesaConsumptionTopicProperties) {
        logger.info("Camessa topic properties: {}", cammesaConsumptionTopicProperties);
        return TopicBuilder
                .name(cammesaConsumptionTopicProperties.getName())
                .partitions(cammesaConsumptionTopicProperties.getPartitions())
                .replicas(cammesaConsumptionTopicProperties.getReplicas()).build();
    }

    @ConfigurationProperties(prefix = "cammesa-consumption-raw")
    public static class cammesaConsumptionTopicProperties extends TopicProperties {
        public cammesaConsumptionTopicProperties(String name, Integer partitions, Integer replicas) {
            super(name, partitions, replicas);
        }
    }
}
