package com.automation.professional.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.automation.professional.domain.MostOfContent} entity.
 */
public class MostOfContentDTO implements Serializable {

    private Long id;

    private String urlOriginal;

    private String contentText;

    private Instant postTime;

    private Integer numberLike;

    private Integer numberComment;

    private FacebookDTO facebook;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlOriginal() {
        return urlOriginal;
    }

    public void setUrlOriginal(String urlOriginal) {
        this.urlOriginal = urlOriginal;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public Instant getPostTime() {
        return postTime;
    }

    public void setPostTime(Instant postTime) {
        this.postTime = postTime;
    }

    public Integer getNumberLike() {
        return numberLike;
    }

    public void setNumberLike(Integer numberLike) {
        this.numberLike = numberLike;
    }

    public Integer getNumberComment() {
        return numberComment;
    }

    public void setNumberComment(Integer numberComment) {
        this.numberComment = numberComment;
    }

    public FacebookDTO getFacebook() {
        return facebook;
    }

    public void setFacebook(FacebookDTO facebook) {
        this.facebook = facebook;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MostOfContentDTO)) {
            return false;
        }

        MostOfContentDTO mostOfContentDTO = (MostOfContentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mostOfContentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MostOfContentDTO{" +
            "id=" + getId() +
            ", urlOriginal='" + getUrlOriginal() + "'" +
            ", contentText='" + getContentText() + "'" +
            ", postTime='" + getPostTime() + "'" +
            ", numberLike=" + getNumberLike() +
            ", numberComment=" + getNumberComment() +
            ", facebook=" + getFacebook() +
            "}";
    }
}
