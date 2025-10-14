package dev.str.electricsim.config.kafka.model;

public class TopicProperties {
    private String name;
    private int partitions;
    private int replicas;

    public TopicProperties(String name, Integer partitions, Integer replicas) {
        this.name = name;
        this.partitions = partitions;
        this.replicas = replicas;
    }

    public String getName() {
        return name;
    }

    public Integer getPartitions() {
        return partitions;
    }

    public Integer getReplicas() {
        return replicas;
    }
}
