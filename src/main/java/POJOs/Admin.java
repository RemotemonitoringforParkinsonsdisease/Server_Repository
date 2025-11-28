package POJOs;

public class Admin {
    private Integer adminId;
    private Integer userId;
    private String adminPassword;
    
    public Admin(Integer adminId, Integer userId, String adminPassword) {
        this.adminId = adminId;
        this.userId = userId;
        this.adminPassword = adminPassword;
    }

    public Admin(Integer userId, String adminPassword) {
        this.userId = userId;
        this.adminPassword = adminPassword;
    }



    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public int getUserId() {
        return userId;
    }
}
