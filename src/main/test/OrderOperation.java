import edu.restaurant.app.dao.entity.*;
import edu.restaurant.app.dao.operations.DishCrudOperations;
import edu.restaurant.app.dao.operations.OrderCrudOperations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderOperation {
    private final DishCrudOperations dishCrudOperations = new DishCrudOperations();
    private final OrderCrudOperations subject = new OrderCrudOperations();

    @Test
    void create_order() {
        Dish dishToAssociateWithOrder = dishCrudOperations.findById(1L);
        List<DishOrder> dishOrdersBelongToOrder = DishOrders(dishToAssociateWithOrder);

        Order expectedOrderCreated = order();
        // I associate dishOrders au order
        expectedOrderCreated.setDishOrders(dishOrdersBelongToOrder);

        System.out.println(expectedOrderCreated);

        Order actualCreatedOrder = subject.save(expectedOrderCreated);

        Assertions.assertEquals(expectedOrderCreated, actualCreatedOrder);

    }

    public List<DishOrder> DishOrders(Dish dish) {
        List<DishOrder> dishOrders = new ArrayList<>();

        DishOrder newDishOrder = new DishOrder();
        newDishOrder.setId(4L);
        newDishOrder.setOrderId(3L);
        newDishOrder.setDishQuantity(2L);
        newDishOrder.setDish(dish);
        newDishOrder.setOrderDatetime(LocalDateTime.parse("2025-03-22T10:37:30.00"));
        newDishOrder.setDishOrderStatuses(defaultDishOrderStatus());
        dishOrders.add(newDishOrder);

        return dishOrders;
    }

    public Order order() {
        var newOrder = new Order();

        newOrder.setId(3L);
        newOrder.setReference("o-1");
        newOrder.setDestination("Analamahitsy");
        newOrder.setCreationDatetime(LocalDateTime.parse("2025-03-22T10:37:30.00"));

        return newOrder;
    }

    private List<DishOrderStatus> defaultDishOrderStatus() {
        List<DishOrderStatus> dishOrderStatuses = new ArrayList<>();

        DishOrderStatus dishOrderStatus = new DishOrderStatus();
        dishOrderStatus.setOrderProcessStatus(OrderProcessStatus.CREATED);
        dishOrderStatus.setDatetime(LocalDateTime.parse("2025-03-22T10:37:30.00"));

        return dishOrderStatuses;
    }
}
