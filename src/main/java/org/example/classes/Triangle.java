package org.example.classes;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Triangle extends AbstractShape {

    private int a;
    private int b;
    private int c;

    @Override
    public int perimeter()
    {
        return getA()+getB()+getC();
    }
    @Override
    public double area()
    {
        double p = perimeter()/2.0;
        return Math.sqrt(p * (p - getA()) * (p - getB()) * (p - getC()));
    }
    public boolean isRegular() { return (getA() == getB())&&(getB() == getC()); }
    public boolean isRight()
    {
        int max = Math.max(getA(), Math.max(getB(),getC()));
        int side1 = Math.min(getA(), Math.min(getB(),getC()));
        int side2 = perimeter() - max - side1;
        return (max * max == side1 * side1 + side2 * side2);
    }

    @Override
    public String toString() {
        return
                "Triangle (" +
                "name = " + getName() +
                ", A = " + a +
                ", B = " + b +
                ", C = " + c + ")";
    }

}
