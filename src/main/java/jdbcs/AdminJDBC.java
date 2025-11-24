package jdbcs;

public class AdminJDBC {
    private ManagerJDBC manager;

    public AdminJDBC(ManagerJDBC manager) {
        this.manager = manager;
    }

    public ManagerJDBC getManagerJDBC() {
        return manager;
    }
}
