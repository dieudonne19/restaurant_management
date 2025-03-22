package edu.restaurant.app.dao.operations;

import edu.restaurant.app.dao.DataSource;
import edu.restaurant.app.dao.entity.*;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DishOrderCrudOperations implements CrudOperations<DishOrder> {
    private final DataSource dataSource = new DataSource();
    private final DishCrudOperations dishCrudOperations = new DishCrudOperations();

    @Override
    public List<DishOrder> getAll(int page, int size) {
        return List.of();
    }

    @Override
    public DishOrder findById(Long id) {
        return null;
    }

    public List<DishOrder> findByOrderId(Long id) {
        List<DishOrder> dishOrders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select do.id, do.id_order, do.id_dish, o.dish_quantity, do.order_date_time from dish_order do "
                     + "where id_order = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    DishOrder dishOrder = mapDishOrder(resultSet);
                    dishOrders.add(dishOrder);
                }
                return dishOrders;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DishOrder> saveAll(List<DishOrder> entities) {
        List<DishOrder> dishOrders = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement =
                         connection.prepareStatement("insert into dish_order (id, id_dish, id_order, dish_quantity, order_date_time) values (?, ?, ?, ?, ?)"
                                 + " on conflict (id) do update set dish_quantity=excluded.dish_quantity"
                                 + " returning id, id_dish, id_order, dish_quantity, order_date_time")) {
                entities.forEach(entityToSave -> {
                    try {
                        statement.setLong(1, entityToSave.getId());
                        statement.setLong(2, entityToSave.getDish().getId());
                        statement.setLong(3, entityToSave.getOrderId());
                        statement.setLong(4, entityToSave.getDishQuantity());
                        statement.setTimestamp(5, Timestamp.valueOf(entityToSave.getOrderDatetime()));
                        statement.addBatch(); // group by batch so executed as one query in da
                        try (ResultSet resultSet = statement.executeQuery()) {
                            while (resultSet.next()) {
                                dishOrders.add(mapFromResultSet(resultSet));
                            }
                        }// tabase
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                return dishOrders;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private DishOrder mapFromResultSet(ResultSet resultSet) throws SQLException {
        System.out.println(resultSet.getLong("id_dish"));
        Dish dish = dishCrudOperations.findById(resultSet.getLong("id_dish"));
        DishOrder dishOrder = new DishOrder();
        List<DishOrderStatus> defaultDishOrderStatus = defaultDishOrderStatus();

        dishOrder.setId(resultSet.getLong("id"));
        dishOrder.setOrderId(resultSet.getLong("id_order"));
        dishOrder.setDish(dish);
        dishOrder.setDishQuantity(resultSet.getLong("dish_quantity"));
        dishOrder.setOrderDatetime(resultSet.getTimestamp("order_date_time").toLocalDateTime());

        dishOrder.setDishOrderStatuses(defaultDishOrderStatus);
        return dishOrder;
    }

    private DishOrder mapDishOrder(ResultSet resultSet) throws SQLException {
        Long dish_id = resultSet.getLong("id_dish");
        Dish dish = dishCrudOperations.findById(dish_id);

        DishOrder dishOrder = new DishOrder();
        dishOrder.setId(resultSet.getLong("id"));
        dishOrder.setOrderId(dish_id);
        dishOrder.setDishQuantity(resultSet.getLong("dish_quantity"));
        dishOrder.setDish(dish);

        return dishOrder;
    }

    private List<DishOrderStatus> defaultDishOrderStatus() {
        List<DishOrderStatus> dishOrderStatuses = new ArrayList<>();

        DishOrderStatus dishOrderStatus = new DishOrderStatus();
        dishOrderStatus.setOrderProcessStatus(OrderProcessStatus.CREATED);
        dishOrderStatus.setDatetime(LocalDateTime.parse("2025-03-22T10:37:30.00"));

        return dishOrderStatuses;
    }
}
