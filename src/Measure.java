import javax.persistence.*;
import java.util.Date;

// the entity class match table measures in database
// there are getters and setters for each field
@Entity
@Table(name = "measures", schema = "dbo")
public class Measure {
    private Integer id;
    private Integer object_id;
    private Date dateTime;
    private Float value;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "object_id")
    public Integer getObject_id() {
        return object_id;
    }

    public void setObject_id(Integer object_id) {
        this.object_id = object_id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dtime")
    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Column(name = "value")
    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }
}
