package com.telegrambot.botoldhouse.Entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Seanse {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_gen")
//    @SequenceGenerator(name="id_gen", sequenceName="article_seq")
    Long id;

    LocalDate date;
    LocalTime time;
    String name;
    @Column(length = 700)
    String description;
    String durattion;
    String payLink;
    String webLink;
    String adress;


    public Seanse() {
    }

    public Seanse(Long id, LocalDate date, LocalTime time, String name, String durattion, String payLink, String webLink, String description, String adress) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.name = name;
        this.description = description;
        this.durattion = durattion;
        this.payLink = payLink;
        this.webLink = webLink;
        this.adress = adress;
    }

    public Seanse(LocalDate date, LocalTime time, String name, String durattion, String payLink, String webLink, String description, String adress) {
        this.date = date;
        this.time = time;
        this.name = name;
        this.description = description;
        this.durattion = durattion;
        this.payLink = payLink;
        this.webLink = webLink;
        this.adress = adress;
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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Seanse{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", name='" + name + '\'' +

                '}';
    }
}
