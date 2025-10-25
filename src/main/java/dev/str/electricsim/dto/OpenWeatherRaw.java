package dev.str.electricsim.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherRaw {
    public Main main;
    public Wind wind;
    public Clouds clouds;
    public Sys sys;
    public Long dt;
    public Rain rain;
    public Snow snow;
    public String name;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        public Double temp;
        public Integer humidity;
        public Double pressure;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Wind {
        public Double speed;
        public Integer deg;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Clouds {
        public Integer all;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sys {
        public Long sunrise;
        public Long sunset;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rain {
        // keys can be "1h" or "3h". Jackson can't map "1h" to a field, so use @JsonProperty
        @JsonProperty("1h")
        public Double oneH;
        @JsonProperty("3h")
        public Double threeH;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Snow {
        @JsonProperty("1h")
        public Double oneH;
        @JsonProperty("3h")
        public Double threeH;
    }
}
