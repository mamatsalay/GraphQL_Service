package uz.uzum.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


public class AuthRequest {

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

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

}
