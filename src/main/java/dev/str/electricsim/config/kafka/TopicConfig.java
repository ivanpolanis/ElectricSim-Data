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

    @Bean("cammesaConsumptionTopic")
    public NewTopic camessaConsumptionTopic(CammesaConsumptionTopicProperties cammesaConsumptionTopicProperties) {
        logger.info("Building Cammesa Consumption Topic");
        return TopicBuilder
                .name(cammesaConsumptionTopicProperties.getName())
                .partitions(cammesaConsumptionTopicProperties.getPartitions())
                .replicas(cammesaConsumptionTopicProperties.getReplicas()).build();
    }

    @ConfigurationProperties(prefix = "cammesa-consumption-raw")
    public static class CammesaConsumptionTopicProperties extends TopicProperties {
        public CammesaConsumptionTopicProperties(String name, Integer partitions, Integer replicas) {
            super(name, partitions, replicas);
        }
    }

    @Bean("cammesaGenerationTopic")
    public NewTopic camessaGenerationTopic(CammesaGenerationTopicProperties cammesaGenerationTopicProperties) {
        logger.info("Building Cammesa Generation Topic");
        return TopicBuilder
                .name(cammesaGenerationTopicProperties.getName())
                .partitions(cammesaGenerationTopicProperties.getPartitions())
                .replicas(cammesaGenerationTopicProperties.getReplicas()).build();
    }

    @ConfigurationProperties(prefix = "cammesa-generation-raw")
    public static class CammesaGenerationTopicProperties extends TopicProperties {
        public CammesaGenerationTopicProperties(String name, Integer partitions, Integer replicas) {
            super(name, partitions, replicas);
        }
    }
}
