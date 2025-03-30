package pl.oliwawyplywa.web.dto.users;

public class Logout {

    private String token;

    public Logout() {
    }

    public Logout(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
