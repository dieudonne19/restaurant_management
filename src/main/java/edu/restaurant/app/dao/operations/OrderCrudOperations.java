package edu.restaurant.app.dao.operations;

import edu.restaurant.app.dao.DataSource;
import edu.restaurant.app.dao.entity.Dish;
import edu.restaurant.app.dao.entity.DishOrder;
import edu.restaurant.app.dao.entity.Order;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OrderCrudOperations implements CrudOperations<Order> {
    private final DataSource dataSource = new DataSource();
    private final DishOrderCrudOperations dishOrderCrudOperations = new DishOrderCrudOperations();

    @Override
    public List<Order> getAll(int page, int size) {
        if (page < 1) {
            throw new IllegalArgumentException("page must be higher than " + page);
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select o.id, o.reference, o.destination, o.creation_date_time from order o "
                     + "join dish_order do on do.id_order = o.id limit ? offset ?")) {
            statement.setInt(1, page);
            statement.setInt(2, page * (size - 1));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return null;
                }
            }
            throw new RuntimeException("Not finished");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select o.id, o.reference, o.destination, o.creation_date_time from order o where id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapFromResultSet(resultSet);
                }
            }
            throw new RuntimeException("Order.id=" + id + " not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> saveAll(List<Order> entitiesToSave) {
        List<Order> orders = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("insert into order (id, reference, destination, creation_data_time) values (?, ?, ?, ?)"
                     + "on conflict (id) do update set reference=excluded.reference?"
                     + " returning id, reference, destination, creation_date_time")) {
            entitiesToSave.forEach(entity -> {
                try {
                    statement.setLong(1, entity.getId());
                    statement.setString(2, entity.getReference());
                    statement.setString(3, entity.getDestination());
                    statement.setObject(4, entity.getCreationDatetime());
                    statement.addBatch(); // group by batch so executed as one query in database

                    dishOrderCrudOperations.saveAll(entity.getDishOrders());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    orders.add(mapSaveFromResultSet(resultSet));
                }
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Order save(Order entityToSave) {
        List<Order> Orders = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("insert into \"order\" (id, reference, destination, creation_date_time)"
                     + " values (?, ?, ?, ?)"
                     + "on conflict (id) do update set reference=excluded.reference"
                     + " returning id, reference, destination, creation_date_time")) {
            try {
                statement.setLong(1, entityToSave.getId());
                statement.setString(2, entityToSave.getReference());
                statement.setString(3, entityToSave.getDestination());
                statement.setTimestamp(4, Timestamp.valueOf(entityToSave.getCreationDatetime()));
                statement.addBatch(); // group by batch so executed as one query in database

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                List<DishOrder> dishOrders = dishOrderCrudOperations.saveAll(entityToSave.getDishOrders());
                if (resultSet.next()) {
                    Order orderCreated = mapSaveFromResultSet(resultSet);
                    orderCreated.setDishOrders(dishOrders);
                    Orders.add(orderCreated);
                }
            }
            System.out.println(Orders);
            return Orders.getFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private Order mapSaveFromResultSet(ResultSet resultSet) throws SQLException {
        Order order = new Order();

        order.setId(resultSet.getLong("id"));
        order.setReference(resultSet.getString("reference"));
        order.setDestination(resultSet.getString("destination"));
        order.setCreationDatetime(resultSet.getTimestamp("creation_date_time").toLocalDateTime());

        return order;
    }

    private Order mapFromResultSet(ResultSet resultSet) throws SQLException {
        List<DishOrder> dishOrders = dishOrderCrudOperations.findByOrderId(resultSet.getLong("id"));
        Order order = new Order();
        order.setId(resultSet.getLong("id"));
        order.setDishOrders(dishOrders);
        order.setDestination(resultSet.getString("destination"));
        order.setCreationDatetime(resultSet.getTimestamp("creation_date_time").toLocalDateTime());
        order.setReference(resultSet.getString("reference"));

        return order;
    }

}
