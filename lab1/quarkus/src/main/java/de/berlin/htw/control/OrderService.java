package de.berlin.htw.control;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;

import de.berlin.htw.boundary.dto.Order;
import de.berlin.htw.boundary.dto.Orders;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@RequestScoped
public class OrderService {

    public Orders getOrdersByName(final String userId, final String name) {
        // example orders
        List<Order> orders = new ArrayList<>();
        // first
        Order o = new Order();
        o.setTitle("First product");
        o.setCount(3);
        orders.add(o);
        // second
        o = new Order();
        o.setTitle("Second product");
        o.setCount(8);
        orders.add(o);

        return new Orders(orders);
    }

}
