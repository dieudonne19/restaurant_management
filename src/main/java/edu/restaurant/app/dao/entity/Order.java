package edu.restaurant.app.dao.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@ToString

public class Order {
    private Long id;
    private String reference;
    private List<DishOrder> dishOrders;
    private List<OrderStatus> orderStatuses;
    private String destination;
    private LocalDateTime creationDatetime;

    public DishOrderStatus getActualStatus() {
        /*
        List<DishOrderStatus> dishesActualStatuses = this.dishOrders.stream()
                .map(dishOrder -> {
                    List<DishOrderStatus> dishStatuses = dishOrder.getDishOrderStatuses();
                    return dishStatuses.stream().max(); // error here
                });
        OrderStatus actualOrderStatus = new OrderStatus();

        boolean isAllStatusEven = dishesActualStatuses.stream().allMatch(orderStatus -> {
            switch (orderStatus.getOrderProcessStatus()) {
                case CREATED, CONFIRMED, PREPARING, FINISHED, DELIVERED -> {
                    actualOrderStatus.setDatetime(orderStatus.getDatetime());
                    actualOrderStatus.setOrderProcessStatus(orderStatus.getOrderProcessStatus());
                    return true;
                }
                default -> {
                    return false;
                }
            }
        });
        return actualOrderStatus;
         */
        throw  new RuntimeException("Error needs to be fixed");
    }

    public Double getTotalAmount() {
        return this.dishOrders.stream()
                .map(dishOrder -> {
                    Double actualDishPrice = dishOrder.getDish().getPrice();
                    Long requiredDishQuantity = dishOrder.getDishQuantity();
                    return actualDishPrice * requiredDishQuantity;
                })
                .reduce(0.0, Double::sum);
    }
}
