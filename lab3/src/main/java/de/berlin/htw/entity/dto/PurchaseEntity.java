package de.berlin.htw.entity.dto;

import de.berlin.htw.boundary.dto.Item;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "PURCHASE")
public class PurchaseEntity extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ITEMS")
    private String items;

    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "TOTAL")
    private Float total;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public List<Item> getItems() {
        JsonArray itemsArray = new JsonArray(this.items);
        List<Item> output = new ArrayList<>();

        for (int i = 0; i < itemsArray.size(); i++) {
            JsonObject obj = itemsArray.getJsonObject(i);
            Item item = new Item();
            item.setProductId(obj.getString("id"));
            item.setPrice(obj.getFloat("price"));
            item.setCount(obj.getInteger("count"));
            item.setProductName(obj.getString("name"));

            output.add(item);
        }

        return output;
    }
    public void setItems(List<Item> items) {
        this.items = items.toString();
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other instanceof PurchaseEntity) {
            return Objects.equals(((PurchaseEntity) other).getId(), getId());
        }

        return false;
    }
}
