package edu.restaurant.app.dao.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@ToString

public class DishOrder {
    private Long id;
    private Long orderId;
    private Dish dish;
    private Long dishQuantity;
    private List<DishOrderStatus> dishOrderStatuses;
    private LocalDateTime orderDatetime;

    public DishOrderStatus getActualStatus() {
        return this.dishOrderStatuses.stream().max(Comparator.comparing(DishOrderStatus::getDatetime)).orElse(new DishOrderStatus());
    }
}
