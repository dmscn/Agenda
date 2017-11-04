package br.com.damasceno.agenda.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;


@JsonRootName(value = "task")
@JsonIgnoreProperties(ignoreUnknown=true)
@Entity
public class Task implements Serializable {

    @PrimaryKey
    @NonNull
    private String id;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "label")
    private String label;

    @JsonProperty(value = "text")
    private String text;

    @JsonProperty(value = "date")
    private String date;

    public Task() { }

    public Task(@NonNull String id, String title, String label, String text, String date) {
        this.id = id;
        this.title = title;
        this.label = label;
        this.text = text;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
