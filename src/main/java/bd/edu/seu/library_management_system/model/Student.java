package bd.edu.seu.library_management_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Student {
    @Id
    private String email;
    private String name;
    private String Password;

    public Student(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.Password = password;

    }

    public Student() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
