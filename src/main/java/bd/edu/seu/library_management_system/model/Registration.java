package bd.edu.seu.library_management_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Registration {
  //private String userName;
  @Id
  private String email;
  private String password;
  private String userType;

  public Registration() {

  }

    public Registration(String userType,String password, String email ) {
        this.userType= userType;
        this.password = password;
        this.email = email;

    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
