package de.berlin.htw.boundary.dto;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
public class Tweet {

    private String author;
    
    private String message;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
    	return "\"" + message + "\"; from " + author;
    }
}
