package br.com.damasceno.agenda.model;

/**
 * Created by dmscn on 18/10/17.
 */

public class Task {
    private String title;
    private String label;
    private String text;
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
