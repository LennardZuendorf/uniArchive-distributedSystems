package de.berlin.htw.control;

import de.berlin.htw.boundary.dto.Basket;
import de.berlin.htw.boundary.dto.Orders;
import de.berlin.htw.control.exception.NotEnoughFundsException;
import de.berlin.htw.entity.dao.PurchaseRepository;
import de.berlin.htw.entity.dto.PurchaseEntity;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@Dependent
public class OrderController {

    @Inject
    PurchaseRepository repository;

    @Inject
    UserController user;

    public String addOrder(Basket basket, Integer userId) throws NotEnoughFundsException {
        if(user.validateBalance(basket.getTotal(), userId)) {
            PurchaseEntity newOrder = new PurchaseEntity();
            newOrder.setTotal(basket.getTotal());
            newOrder.setItems(basket.getItems());
            newOrder.setUserId(userId);
            String orderId = String.valueOf(repository.persistOrder(newOrder));
            user.lowerBalance(basket.getTotal(), userId);
            return orderId;
        } else throw new NotEnoughFundsException("Not enough money");
    }

    public Orders getOrders(Integer userId){
        Orders output = new Orders();
        output.setBalance(0.0f);
        List<PurchaseEntity> orderList = repository.findOrdersByUserId(userId);

        for (PurchaseEntity order : orderList) {
            output.setBalance(output.getBalance() + order.getTotal());
        }

        return output;
    }
}
