package bd.edu.seu.library_management_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;
@Entity
public class IssuedBook {
    @Id
    private String email;
    private int isbn;
    private String title;
    private LocalDate issueDate;
    private LocalDate returnDate;


    public IssuedBook(String email, int isbn, LocalDate issueDate, LocalDate returnDate, String title) {
        this.email = email;
        this.isbn = isbn;
        this.issueDate = issueDate;

        this.returnDate = returnDate;
        this.title = title;
    }
    public IssuedBook() {

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

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
