package bd.edu.seu.library_management_system.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Teacher {

    @Id
    private String email;
    private String Password;

    public Teacher(String email, String password) {
        this.email = email;
        Password = password;
    }

    public Teacher() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
