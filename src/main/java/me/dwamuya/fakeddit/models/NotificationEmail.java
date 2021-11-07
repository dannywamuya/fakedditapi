package me.dwamuya.fakeddit.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationEmail {

    private final String subject;
    private final String recipient;
    private final String body;

}