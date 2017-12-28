import com.opencsv.CSVWriter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class FileLoader extends Connector {

    private String username = "sa";
    private String password = "123456";
    private String url = "jdbc:sqlserver://localhost:1433;DatabaseName=Measure";

    private final String targetFilePath;

    private Connection connection = null;
    private Statement statement = null;

    public FileLoader(String targetFilePath) {
        super();
        connect();
        this.targetFilePath = targetFilePath;
    }

    private List<MeasureObject> getMeasureObjects() {
        Session session = getSessionFactory().openSession();
        List<MeasureObject> measureObjects = session.createCriteria(MeasureObject.class).list();
        session.close();
        return measureObjects;
    }

    private double getAverageByObject(List<Measure> measures, Integer id) {
        return measures.stream()
                .mapToDouble(measure -> measure.getValue())
                .average().getAsDouble();
    }

    private void addRecordsOnMeasureObject(Session session, List<String[]> records,
                            MeasureObject measureObject) {
        Criteria criteria = session.createCriteria(Measure.class)
                .add(Restrictions.eq("object_id", measureObject.getId()));
        List<Measure> measures = criteria.list();

        double curAverage = getAverageByObject(measures, measureObject.getId());
        records.add(new String[] {
                measureObject.getId().toString(),
                measureObject.getName(),
                String.valueOf(curAverage),
                ""
        });

        // reverse iterator to write last 10 records if exists
        ListIterator measuresIterator = measures.listIterator(measures.size());
        for (int i = 0; i < 10 && measuresIterator.hasPrevious(); ++i) {
            records.add(new String[] {
                    measureObject.getId().toString(),
                    measureObject.getName(),
                    "",
                    ((Measure)measuresIterator.previous()).getValue().toString()
            });
        }
    }

    public void writeStatistics() {
        List<String[]> dataString = new ArrayList<String[]>();

        List<MeasureObject> measureObjects = getMeasureObjects();

        List<String[]> records = new ArrayList<String[]>();
        records.add(new String[] { "ID", "Name", "Average", "Value" });

        Session session = getSessionFactory().openSession();
        for (int i = 0; i < measureObjects.size(); ++i) {
            addRecordsOnMeasureObject(session, records, measureObjects.get(i));
        }
        session.close();

        try (CSVWriter writer = new CSVWriter(new FileWriter(targetFilePath))) {
            writer.writeAll(records);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
   }
}
