import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;


public class Connector {
    @Autowired
    private SessionFactory sessionFactory;

    public Connector() {
        // create basic config
        Configuration configuration = new Configuration().configure("/resources/hibernate.cfg.xml");
        configuration.addAnnotatedClass(Measure.class);
        configuration.addAnnotatedClass(MeasureObject.class);
        StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        sessionFactory = configuration.buildSessionFactory(ssrb.build());
    }

    // return session factory to get sessions in inheritors
    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
