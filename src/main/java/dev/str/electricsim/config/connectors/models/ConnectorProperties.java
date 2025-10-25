package dev.str.electricsim.config.connectors.models;

public class ConnectorProperties {
    private String url;

    public ConnectorProperties(String url ) {
        this.url = url;
    }

    public String url() {
        return url;
    }
}
