package bd.edu.seu.library_management_system.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Defaulter {
    private String email;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "defaulter_id")
    private Long defaulterId; // Unique ID
    private int isbn;
    private String title;
    private LocalDate issueDate;
    private LocalDate returnDate;
    private Long fineAmount;

    // Constructors
    public Defaulter() {
    }

    public Defaulter(LocalDate issueDate, String email, Long fineAmount, int isbn, LocalDate returnDate, String title) {
        this.issueDate = issueDate;
        this.email = email;
        this.fineAmount = fineAmount;
        this.isbn = isbn;
        this.returnDate = returnDate;
        this.title = title;

    }

    // Getters and Setters
    public Long getDefaulterId() {
        return defaulterId;
    }

    public void setDefaulterId(Long defaulterId) {
        this.defaulterId = defaulterId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Long getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(Long fineAmount) {
        this.fineAmount = fineAmount;
    }

    // Alias for templates expecting 'fine'
    public Long getFine() {
        return fineAmount;
    }
}
