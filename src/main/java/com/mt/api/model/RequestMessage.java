package com.mt.api.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotEmpty;

import java.util.Objects;

/**
 * Model class used for manipulation with requests and responses
 * Annotations for required validation of fields added
 */
public class RequestMessage {

    @NotEmpty(message = "The field source_language is required.")
    @JsonAlias("source_language")
    private String sourceLanguage;
    @NotEmpty(message = "The field target_language is required.")
    @JsonAlias("target_language")
    private String targetLanguage;
    @NotEmpty(message = "The field domain is required.")
    private String domain;
    @NotEmpty(message = "The field content is required.")
    private String content;

    public RequestMessage(String sourceLanguage, String targetLanguage, String domain, String content) {
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
        this.domain = domain;
        this.content = content;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "RequestMessage{" +
                "sourceLanguage='" + sourceLanguage + '\'' +
                ", targetLanguage='" + targetLanguage + '\'' +
                ", domain='" + domain + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestMessage that = (RequestMessage) o;
        return Objects.equals(sourceLanguage, that.sourceLanguage) && Objects.equals(targetLanguage, that.targetLanguage) && Objects.equals(domain, that.domain) && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceLanguage, targetLanguage, domain, content);
    }
}
