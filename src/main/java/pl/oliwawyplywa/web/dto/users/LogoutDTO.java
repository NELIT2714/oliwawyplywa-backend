package pl.oliwawyplywa.web.dto.users;

public class LogoutDTO {

    private String token;

    public LogoutDTO() {
    }

    public LogoutDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
