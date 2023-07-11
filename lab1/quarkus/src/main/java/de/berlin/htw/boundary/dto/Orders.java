package de.berlin.htw.boundary.dto;

import java.util.List;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
public class Orders {

    private List<Order> orders;

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

}
