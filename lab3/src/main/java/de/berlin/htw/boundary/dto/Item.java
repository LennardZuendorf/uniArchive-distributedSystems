package de.berlin.htw.boundary.dto;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.util.List;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
public class Item {

	@NotEmpty
	@Size(max = 255, message = "Item name too long")
    private String productName;

	@NotBlank
	@NotEmpty
	@Pattern(regexp = "[1-9]+-[1-9]+-[1-9]+-[1-9]+-[1-9]+-[1-9]", message = "Wrong format of Product ID")
    private String productId;

	@Max(10)
	@Min(1)
    private Integer count;

	@Min(10)
	@Max(100)
    private Float price;

    public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId=productId;
	}

	public Integer getCount() {
        return count;
    }

    public void setCount(final Integer count) {
        this.count = count;
    }

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

}
