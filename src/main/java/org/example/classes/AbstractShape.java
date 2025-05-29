package org.example.classes;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

@Data
@Entity
@Table(name = "shapes")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractShape {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @ManyToMany(mappedBy = "shapes")
    private List<Picture> pictures;
    private String name;
    private String description;
    private int x;
    private int y;
    private int color ;

    public void setRandomColor()
    {
        Random random = new Random();
        Color col = new Color(random.nextInt(0,255),random.nextInt(0,255),random.nextInt(0,255));
        setColor(col.getRGB());
    }
    public void move(String way)
    {
        switch (way){
            case "up": {
                setY(getY()+10);
                break;
            }
            case "down": {
                setY(getY()-10);
                break;
            }
            case "left": {
                setX(getX()-10);
                break;
            }
            case "right": {
                setX(getX()+10);
                break;
            }
        }
    }
    public abstract int perimeter();
    public abstract double area();

    @Override
    public String toString() {
        return
                "Shape (" +
                "name = " + name +
                ", description = " + description +
                ", color = " + color +
                ", x , y = " + x + ", " + y + ")";
    }
}
