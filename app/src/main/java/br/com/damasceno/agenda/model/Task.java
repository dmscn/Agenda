package br.com.damasceno.agenda.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;

/**
 * Created by dmscn on 18/10/17.
 */

@JsonRootName(value = "task")
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
