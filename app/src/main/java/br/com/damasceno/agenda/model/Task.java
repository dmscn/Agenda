package br.com.damasceno.agenda.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Created by dmscn on 18/10/17.
 */

@JsonRootName(value = "task")
public class Task {

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "label")
    private String label;

    @JsonProperty(value = "text")
    private String text;

    @JsonProperty(value = "date")
    private String date;


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
