package bd.edu.seu.library_management_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ManageBook {

    @Id
    private int isbn;
    private String title;
    private String author;
    private int remainingQuantity;
    private int totalQuantity;

    public ManageBook(int isbn, String title, String author, int remainingQuantity, int totalQuantity) {
        this.author = author;
        this.isbn = isbn;
        this.remainingQuantity = remainingQuantity;
        this.totalQuantity = totalQuantity;
        this.title = title;
    }

    public ManageBook() {

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public int getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(int remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
