package com.example.common.dto;

import com.example.common.enums.NotificationChannel;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NotificationUserMessage {
    private final UserNotification user;
    private final String title;
    private final String message;
    private final boolean displayInHistory;
    private final List<NotificationChannel> deliveryChannels;

    @JsonCreator
    public NotificationUserMessage(
            @JsonProperty("user") UserNotification user,
            @JsonProperty("title") String title,
            @JsonProperty("message") String message,
            @JsonProperty("displayInHistory") boolean displayInHistory,
            @JsonProperty("deliveryChannels") List<NotificationChannel> deliveryChannels) {
        this.user = user;
        this.title = title;
        this.message = message;
        this.displayInHistory = displayInHistory;
        this.deliveryChannels = deliveryChannels;
    }
}
