package edu.restaurant.app.dao.entity;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@ToString

public class OrderStatus {
    private OrderProcessStatus orderProcessStatus;
    private Instant datetime;
}
