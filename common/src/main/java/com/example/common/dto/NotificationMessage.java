package com.example.common.dto;

import com.example.common.enums.NotificationChannel;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class NotificationMessage {
    private final UUID userId;
    private final String title;
    private final String message;
    private final boolean displayInHistory;
    private final List<NotificationChannel> deliveryChannels;

    @JsonCreator
    public NotificationMessage(
            @JsonProperty("userId") UUID userId,
            @JsonProperty("title") String title,
            @JsonProperty("message") String message,
            @JsonProperty("displayInHistory") boolean displayInHistory,
            @JsonProperty("deliveryChannels") List<NotificationChannel> deliveryChannels) {
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.displayInHistory = displayInHistory;
        this.deliveryChannels = deliveryChannels;
    }
}
