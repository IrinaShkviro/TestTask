import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;


public class Connector {

    // some constants to connect
    // optional: move outside the class
    private final String username = "sa";
    private final String password = "123456";
    private final String url = "jdbc:sqlserver://localhost:1433;DatabaseName=Measure";

    @Autowired
    private static SessionFactory sessionFactory;

    public Connector() {
        // create basic config
        Configuration configuration = new Configuration().configure("/resources/hibernate.cfg.xml");
        configuration.addAnnotatedClass(Measure.class);
        configuration.addAnnotatedClass(MeasureObject.class);
        StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(ssrb.build());
    }

    private void loadDrivers() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public void connect() {
        loadDrivers();
    }

    // return session factory to get sessions in inheritors
    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
