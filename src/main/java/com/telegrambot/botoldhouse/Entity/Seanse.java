package com.telegrambot.botoldhouse.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Seanse {
    @Id
    @GeneratedValue
    Long id;
    LocalDate date;
    LocalTime time;
    String name;
    @Column(length = 700)
    String description;
    String durattion;
    String payLink;
    String webLink;


    public Seanse() {
    }

    public Seanse(LocalDate date, LocalTime time, String name, String durattion, String payLink, String webLink, String description) {
        this.date = date;
        this.time = time;
        this.name = name;
        this.description = description;
        this.durattion = durattion;
        this.payLink = payLink;
        this.webLink = webLink;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDurattion() {
        return durattion;
    }

    public void setDurattion(String durattion) {
        this.durattion = durattion;
    }

    public String getPayLink() {
        return payLink;
    }

    public void setPayLink(String payLink) {
        this.payLink = payLink;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }
}
