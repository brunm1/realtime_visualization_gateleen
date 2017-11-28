package ch.bfh.ti.gapa.integration.model;

import java.time.Instant;

public class GapaMessage {
    public enum Method {
        GET, PUT, POST, DELETE, PATCH
    }
    public enum Type {
        inbound, outbound
    }
    private Instant timestamp;
    private Method method;
    private String path;
    private Type type;
    private String peer;
    private String traceId;

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getPeer() {
        return peer;
    }

    public void setPeer(String peer) {
        this.peer = peer;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return "GapaMessage{" +
                "timestamp=" + timestamp +
                ", method=" + method +
                ", path='" + path + '\'' +
                ", type=" + type +
                ", peer='" + peer + '\'' +
                ", traceId='" + traceId + '\'' +
                '}';
    }
}
