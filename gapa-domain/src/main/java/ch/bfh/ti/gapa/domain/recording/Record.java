package ch.bfh.ti.gapa.domain.recording;

import java.time.LocalDateTime;

/**
 * Created by adrian on 18.10.17.
 */
public class Record {
    private String sender;
    private String recipient;
    private String url;
    private LocalDateTime time;
    private String httpMethod;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public CharSequence get(String attribute) {
        switch (attribute) {
            case "sender":
                return getSender();
            case "recipient":
                return getRecipient();
            case "url":
                return getUrl();
            case "httpMethod":
                return getHttpMethod();
            default:
                return null;
        }
    }
}
