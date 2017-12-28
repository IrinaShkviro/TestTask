import javax.persistence.*;

// the entity class match table measure_objects in database
// there are getters and setters for each field
@Entity
@Table(name = "measure_objects", schema = "dbo")
public class MeasureObject {
    private Integer id;
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
