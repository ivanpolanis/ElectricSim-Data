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
    public NewTopic camessaTopic(cammesaTopicProperties cammesaTopicProperties) {
        logger.info("Camessa topic properties: {}", cammesaTopicProperties);
        return TopicBuilder
                .name(cammesaTopicProperties.getName())
                .partitions(cammesaTopicProperties.getPartitions())
                .replicas(cammesaTopicProperties.getReplicas()).build();
    }

    @ConfigurationProperties(prefix = "cammesa-raw")
    public static class cammesaTopicProperties extends TopicProperties {
        public cammesaTopicProperties(String name, Integer partitions, Integer replicas) {
            super(name, partitions, replicas);
        }
    }
}
