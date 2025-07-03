package bd.edu.seu.library_management_system.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ManageBook {

    @Id
    private int isbn;
    private String title;
    private String author;
    private int quantity;

    public ManageBook(int isbn, String title, String author, int quantity) {
        this.author = author;
        this.isbn = isbn;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
