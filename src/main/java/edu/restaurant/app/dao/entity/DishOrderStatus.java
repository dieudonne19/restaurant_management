package edu.restaurant.app.dao.entity;

import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString

public class DishOrderStatus {
    private OrderProcessStatus orderProcessStatus;
    private LocalDateTime datetime;
}
