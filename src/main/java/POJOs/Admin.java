package POJOs;

public class Admin {
    private Integer userId;
    private String adminPassword;


    public Admin(Integer userId, String adminPassword) {
        this.userId = userId;
        this.adminPassword = adminPassword;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public int getUserId() {
        return userId;
    }
}