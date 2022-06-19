package com.telegrambot.botoldhouse.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class UserBot {

    @Id
    @GeneratedValue
    Long id;

    @Column(name = "chat_id")
    String chatId;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    public UserBot() {
    }

    public UserBot(String chatId, String firstName, String lastName) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
