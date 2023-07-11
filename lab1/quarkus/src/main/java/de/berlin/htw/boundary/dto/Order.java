package de.berlin.htw.boundary.dto;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
public class Order {

    private String title;
    
    private Integer count;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(final Integer count) {
        this.count = count;
    }
    
}
