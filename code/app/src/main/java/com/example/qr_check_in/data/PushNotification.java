package com.example.qr_check_in.data;
import com.example.qr_check_in.data.NotificationData;

import java.util.Objects;

/**
 * Push notification to send notification
 */

public class PushNotification {
    private final NotificationData data;
    private final String to;

    public PushNotification(NotificationData data, String to) {
        this.data = data;
        this.to = to;
    }

    public NotificationData getData() {
        return data;
    }

    public String getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PushNotification)) return false;
        PushNotification that = (PushNotification) o;
        return Objects.equals(data, that.data) &&
                Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, to);
    }

    @Override
    public String toString() {
        return "PushNotification{" +
                "data=" + data +
                ", to='" + to + '\'' +
                '}';
    }
}
