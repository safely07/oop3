package org.example.classes;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Rectangle extends AbstractShape {

    private int h;
    private int w;

    @Override
    public int perimeter()
    {
        return (getH()+getW())*2;
    }
    @Override
    public double area()
    {
        return getW()*getH();
    }
    public double getDiagonales()
    {
        return Math.sqrt(getH()*getH()+getW()*getW());
    }
    public boolean isSquare()
    {
        return (getH() == getW());
    }

    @Override
    public String toString() {
        return
                "Rectangle (" +
                "name = " + getName() +
                ", H = " + h +
                ", W = " + w + ")";
    }
}
