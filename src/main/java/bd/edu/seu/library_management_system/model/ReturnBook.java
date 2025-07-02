package bd.edu.seu.library_management_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class ReturnBook {


    @Id
    private String email;
    private int isbn;
    private LocalDate returnDate;

    public ReturnBook(String email, int isbn, LocalDate returnDate) {
        this.email = email;
        this.isbn = isbn;
        this.returnDate = returnDate;
    }

    public ReturnBook() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
