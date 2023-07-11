package de.berlin.htw.control;

import de.berlin.htw.boundary.dto.Basket;
import de.berlin.htw.boundary.dto.Item;
import de.berlin.htw.control.exception.NotEnoughFundsException;
import de.berlin.htw.control.exception.ProductAlreadyExistException;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.hash.HashCommands;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@Dependent
public class BasketController {

    @Inject
    protected RedisDataSource redisDS;

    @Inject
    OrderController order;

    @Inject
    UserController user;

    protected HashCommands<String, String, Item> commands;
    
    @PostConstruct
    protected void init() {
        commands = redisDS.hash(String.class, String.class, Item.class);
    }
    
    public Basket getBasket(String userId) {
        List<Item> items;

        if(redisDS.key().exists(userId)){
            Basket output = new Basket();
            items = new ArrayList<>(commands.hgetall(userId).values());
            output.setItems(items);
            output.setTotal((float) items.stream().mapToDouble(item -> item.getCount() * item.getPrice()).sum());
            return output;

        }else throw new NotFoundException("Basket does not exist");

    }

    public String addItem(Item item, String userId) throws NotEnoughFundsException, ProductAlreadyExistException {
        //check if item already exists, if yes throw exception
    	if (commands.hexists(userId, item.getProductId())) {
            throw new ProductAlreadyExistException("Product already exists in basket");
    	} else {
            List<Item> items= new ArrayList<>(commands.hgetall(userId).values());

            float totalPrice = (float) items.stream().mapToDouble(listItem -> listItem.getCount() * listItem.getPrice()).sum();
            if(!user.validateBalance(Float.sum(totalPrice,item.getCount() * item.getPrice()), Integer.parseInt(userId))) {
                throw new NotEnoughFundsException("No sufficient funds");
            }

            redisDS.key().expire(userId, 120);
    		commands.hset(userId, item.getProductId(), item);
            return item.getProductId();
    	}
    }

    public String updateItem(Item item, String userId) throws NotEnoughFundsException{
        if(commands.hexists(userId, item.getProductId())) {
            List<Item> items= new ArrayList<>(commands.hgetall(userId).values());
            Item updateItem = commands.hget(userId, item.getProductId());
            updateItem.setCount(item.getCount());

            float totalPrice = (float) items.stream().mapToDouble(listItem -> listItem.getCount() * listItem.getPrice()).sum();
            if(!user.validateBalance(Float.sum(totalPrice,updateItem.getCount() * updateItem.getPrice()), Integer.parseInt(userId))) {
                throw new NotEnoughFundsException("No sufficient funds");
            }

            commands.hset(userId, item.getProductId(), updateItem);
            redisDS.key().expire(userId, 120);
            return item.getProductId();
    	} else {
    		throw new NotFoundException("Item does not exist in basket");
    	}
    }

    public void removeItem(String productId, String userId) {
        if(commands.hexists(userId, productId)) {
            commands.hdel(userId, productId);
            redisDS.key().expire(userId, 120);
        } else {
            throw new NotFoundException("Item does not exist in basket");
        }
    }

    public void clearBasket(String userId) {
        try{
            redisDS.key().del(userId);
        } catch (Exception e) {
            throw new NotFoundException("Basket does not exist");
        }
    }

    public String checkout(String userId) throws NotEnoughFundsException {
        Basket basket = new Basket();
        Map<String, Item> output;

        if(redisDS.key().exists(userId)){
            output = commands.hgetall(userId);
            basket.setItems(new ArrayList<>(output.values()));
            basket.setTotal((float) output.values().stream().mapToDouble(item -> item.getCount() * item.getPrice()).sum());

            try{
                String id = order.addOrder(basket, Integer.parseInt(userId));
                redisDS.key().del(userId);
                return id;
            } catch (NotEnoughFundsException e) {
                throw new NotEnoughFundsException("Not enough funds to checkout");
            }

        }else throw new NotFoundException("Basket does not exist");
    }
}
