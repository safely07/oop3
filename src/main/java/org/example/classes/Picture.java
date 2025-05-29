package org.example.classes;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @ManyToMany
    @JoinTable(name = "shapes_in_picture",
    joinColumns = @JoinColumn(name = "picture_id"),
    inverseJoinColumns = @JoinColumn(name = "shape_id"))
    private List<AbstractShape> shapes;
    private String name;

    @Override
    public String toString() {
        return
                "Picture ("+
                "id=" + id +
                ", Name = " + name + ")"
                ;
    }

}
