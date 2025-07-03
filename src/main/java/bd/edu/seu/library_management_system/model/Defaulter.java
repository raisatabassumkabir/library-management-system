package bd.edu.seu.library_management_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;
@Entity
public class Defaulter {
    @Id
    private String email;
    private int isbn;
    private String title;
    private LocalDate issueDate;
    private LocalDate returnDate;
    private Long fineAmount;

    public Defaulter(LocalDate issueDate, String email, Long fineAmount, int isbn, LocalDate returnDate, String title) {
        this.issueDate = issueDate;
        this.email = email;
        this.fineAmount = fineAmount;
        this.isbn = isbn;
        this.returnDate = returnDate;
        this.title = title;
    }
    public Defaulter() {

    }
    public LocalDate getIssueDate() {
        return issueDate;
    }


    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(Long fineAmount) {
        this.fineAmount = fineAmount;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
