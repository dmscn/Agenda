package br.com.damasceno.agenda.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.orm.SugarRecord;

import java.io.Serializable;

import br.com.damasceno.agenda.constant.Constants;

public class User extends SugarRecord implements Serializable, Constants {
    @JsonProperty("id")
    private String idUser;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("picture")
    private String picture;

    @JsonProperty("token")
    private String token;

    @JsonProperty("createdAt")
    private String createdAt;

    public User() { }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
