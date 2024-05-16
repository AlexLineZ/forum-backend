package com.example.common.dto;

import lombok.Data;

@Data
public class NotificationMessage {
    private final String id;
    private final String userId;
    private final String title;
    private final String message;
    private final boolean displayInHistory;
    private final String[] deliveryChannels;
}
