import java.io.IOException;
import java.sql.*;
import java.io.FileReader;
import java.util.List;

import com.opencsv.CSVReader;
import org.hibernate.*;
import org.hibernate.criterion.*;

public class DBLoader extends Connector {

    // file with source data
    private final String sourceFilePath;

    public DBLoader(String sourceFilePath) {
        super();
        connect();
        this.sourceFilePath = sourceFilePath;
    }

    // load data from source file into database
    public void loadFileToDB() {
        String[] dataString;
        try (CSVReader reader = new CSVReader(new FileReader(sourceFilePath), ';')) {
            while ((dataString = reader.readNext()) != null) {
                loadNextLine(dataString);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // load one entity object into database
    private void loadNewObject(Object object) {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        session.saveOrUpdate(object);
        session.flush();
        session.getTransaction().commit();
        session.close();
    }

    // check if measure object with given name exist
    // if exist - return id
    // if not exist - create new measure object with given name and return its id
    private Integer getMeasureObjectId(String name) {
        Session session = getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(MeasureObject.class);
        List<MeasureObject> measureObjects = criteria.add(Restrictions.like("name", name, MatchMode.EXACT)).list();
        session.close();
        if (measureObjects.isEmpty()) {
            MeasureObject measureObject = new MeasureObject();
            measureObject.setName(name);
            loadNewObject(measureObject);
            return measureObject.getId();
        }
        return measureObjects.get(0).getId();
    }

    // load one measure (one line from source file) to database
    private void loadNextLine(String[] line) {
        Integer measureObjectId = getMeasureObjectId(line[0]);
        Measure measure = new Measure();
        measure.setValue(Float.parseFloat(line[1]));
        measure.setDateTime(new Date(System.currentTimeMillis()));
        measure.setObject_id(measureObjectId);
        loadNewObject(measure);
    }
}
