package bd.edu.seu.library_management_system.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;



@Entity
public class Admin {
    @Id
    private String email;
    private String password;

    public Admin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Admin() {

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
