package dev.str.electricsim.config.connectors;

import dev.str.electricsim.config.YamlPropertySourceFactory;
import dev.str.electricsim.config.connectors.models.ConnectorProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestClient;

@Configuration
@PropertySource(value = "classpath:connectors.yaml", encoding = "UTF-8", factory = YamlPropertySourceFactory.class)
public class ConnectorConfig {

    @Bean("consumptionConnector")
    public RestClient consumptionConnector(CammesaConsumptionProperties cammesaConsumptionProperties) {
        return RestClient.builder()
                .baseUrl(cammesaConsumptionProperties.url())
                .build();
    }

    @ConfigurationProperties(prefix="cammesa-consumption")
    public static class CammesaConsumptionProperties extends ConnectorProperties {
        public CammesaConsumptionProperties(String url) {
            super(url);
        }
    }

    @Bean("generationConnector")
    public RestClient generationConnector(CammesaGenerationProperties cammesaGenerationProperties) {
        return RestClient.builder()
                .baseUrl(cammesaGenerationProperties.url())
                .build();
    }

    @ConfigurationProperties(prefix="cammesa-generation")
    public static class CammesaGenerationProperties extends ConnectorProperties {
        public CammesaGenerationProperties(String url) {
            super(url);
        }
    }

    @Bean("weatherHistoryConnector")
    public RestClient weatherHistoryConnector(OpenWeatherProperties openWeatherProperties) {
        return RestClient.builder()
                .baseUrl(openWeatherProperties.url())
                .build();
    }

    @ConfigurationProperties(prefix="openweather-history")
    public static class OpenWeatherProperties extends ConnectorProperties {
        public OpenWeatherProperties(String url) {
            super(url);
        }
    }
}

