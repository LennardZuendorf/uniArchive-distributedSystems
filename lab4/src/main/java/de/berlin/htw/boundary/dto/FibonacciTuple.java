package de.berlin.htw.boundary.dto;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
public class FibonacciTuple {

    private Integer last;
    
    private Integer current;

    public Integer getKey() {
        return getCurrent();
    }

    public Integer getLast() {
        return last;
    }

    public void setLast(Integer last) {
        this.last = last;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    @Override
    public String toString(){
        return last.toString() + " | " + current.toString();
    }
    
}
