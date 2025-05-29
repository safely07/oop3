package org.example;

import org.example.classes.AbstractShape;
import org.example.classes.Picture;
import org.example.classes.Rectangle;
import org.example.classes.Triangle;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.awt.*;
import java.util.List;
import java.util.Scanner;

public class Menu {
    public static void addShape(Scanner scanner, SessionFactory sessionFactory) {
        AbstractShape obj = null;
        System.out.println("1. Треугольник");
        System.out.println("2. Прямоугольник");
        System.out.print("Выберите пункт меню: ");
        int choice = scanner.nextInt();
        System.out.print("Введите название фигуры: ");
        String name = scanner.next();
        System.out.print("Введите описание фигуры: ");
        String description = scanner.next();

        switch (choice) {
            case 1:
                obj = new Triangle();
                System.out.println("Введите сторону A: ");
                ((Triangle)obj).setA(scanner.nextInt());
                System.out.println("Введите сторону B: ");
                ((Triangle)obj).setB(scanner.nextInt());
                System.out.println("Введите сторону C: ");
                ((Triangle)obj).setC(scanner.nextInt());
                break;
            case 2:
                obj = new Rectangle();
                System.out.println("Введите высоту: ");
                ((Rectangle)obj).setH(scanner.nextInt());
                System.out.println("Введите ширину: ");
                ((Rectangle)obj).setW(scanner.nextInt());
                break;
            default: {
                System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
                break;
            }

        }

        obj.setName(name);
        obj.setDescription(description);
        obj.setX(0);
        obj.setY(0);
        obj.setColor(0);

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.persist(obj);

        tx.commit();
        session.close();
        System.out.println("Фигура добавлена.");
    }

    public static void addPicture(Scanner scanner,SessionFactory sessionFactory) {

        Picture obj = new Picture();
        System.out.print("Введите название картинки: ");
        obj.setName(scanner.next());

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(obj);
        tx.commit();
        session.close();
        System.out.println("Картина добавлена.");
    }

    public static void listOfPictures(Scanner scanner,SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        List<Picture> pictures = session.createCriteria(Picture.class).list();
        if (pictures.isEmpty()) {
            System.out.println("Нет доступных картин.");
            return;
        }
        for (Picture picture : pictures) {
            Hibernate.initialize(picture.getShapes());
        }
        session.close();
        System.out.println("Список картин:");
        for (int i = 0; i < pictures.size(); i++) {
            System.out.println((i+1)+". "+pictures.get(i));
        }

        System.out.print("Выберите картинку для взаимодействия: ");
        int pictureChoice = scanner.nextInt();

        Picture selectedPicture = pictures.get(pictureChoice-1);

        interactWithPicture(selectedPicture,scanner,sessionFactory);

    }

    public static void interactWithPicture(Picture picture, Scanner scanner,SessionFactory sessionFactory) {
        System.out.println("Вы выбрали картинку: " + picture);
        System.out.println("Название: " + picture.getName());

        System.out.println("Фигуры: " + picture.getShapes());
        System.out.println("Методы");
        System.out.println("1. Добавить фигуру в картину");
        System.out.println("2. Удалить фигуру из картинки");
        System.out.println("3. Удалить картину");
        System.out.println("4. В главное меню");
        switch (scanner.nextInt()) {
            case 1: {
                addShapeInPicture(picture, scanner,sessionFactory);
                break;
            }
            case 2: {
                deleteShapeFromPicture(picture, scanner,sessionFactory);
                break;
            }
            case 3: {
                deletePicture(picture);
                break;
            }
            case 4: {
                break;
            }
        }
    }

    public static void deletePicture(Picture picture) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.delete(picture);
        tx.commit();
        session.close();
    }

    public static void deleteShapeFromPicture(Picture picture, Scanner scanner,SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        List<AbstractShape> shapes = session.createCriteria(AbstractShape.class).list();
        session.close();
        if (shapes.isEmpty()) {
            System.out.println("Нет доступных фигур.");
            return;
        }
        System.out.println("Список фигур:");
        for (int i = 0; i < shapes.size(); i++) {
            System.out.println((i+1)+". "+shapes.get(i));
        }
        List<AbstractShape> shapesList = picture.getShapes();
        System.out.print("Выберите фигуру для удаления из картинки: ");
        int shapeChoice = scanner.nextInt();
        AbstractShape selectedShape = shapes.get(shapeChoice-1);
        if (shapesList.stream().noneMatch(s -> s.getId().equals(selectedShape.getId()))) {
            System.out.println("Фигура отсутствует в выбранной картине!");
        } else {
            shapesList.removeIf(s -> s.getId().equals(selectedShape.getId()));
            picture.setShapes(shapesList);
            updatePicture(picture,sessionFactory);
            System.out.println("Фигура успешно удалена из картины.");
        }
    }

    public static void addShapeInPicture(Picture picture, Scanner scanner,SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        List<AbstractShape> shapes = session.createCriteria(AbstractShape.class).list();

        if (shapes.isEmpty()) {
            System.out.println("Нет доступных фигур.");
            return;
        }
        session.close();
        System.out.println("Список фигур:");
        for (int i = 0; i < shapes.size(); i++) {
            System.out.println((i+1)+". "+shapes.get(i));
        }

        System.out.print("Выберите фигуру для добавления: ");
        int shapeChoice = scanner.nextInt();
        AbstractShape selectedShape = shapes.get(shapeChoice-1);
        if (picture.getShapes().stream().anyMatch(s -> s.getId().equals(selectedShape.getId()))) {
            System.out.println("Эта фигура уже присутствует в картине!");
        } else {
            picture.getShapes().add(selectedShape);
            updatePicture(picture,sessionFactory);
            System.out.println("Фигура успешно добавлена в картину.");
        }
    }

    public static void updatePicture(Picture picture,SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.update(picture);
        tx.commit();
        session.close();
    }

    public static void listAndInteractWithShapes(Scanner scanner,SessionFactory sessionFactory) {

        Session session = sessionFactory.openSession();
        List<AbstractShape> shapes = session.createCriteria(AbstractShape.class).list();
        session.close();

        if (shapes.isEmpty()) {
            System.out.println("Нет доступных фигур.");
            return;
        }

        System.out.println("Список фигур:");
        for (int i = 0; i < shapes.size(); i++) {
            System.out.println((i+1)+". "+shapes.get(i));
        }

        System.out.print("Выберите фигуру для взаимодействия: ");
        int shapeChoice = scanner.nextInt();

        interactWithShape(shapes.get(shapeChoice-1), scanner,sessionFactory);
    }

    public static void interactWithShape(AbstractShape shape, Scanner scanner,SessionFactory sessionFactory) {
        System.out.println("Вы выбрали фигуру: " + shape);
        System.out.println("Свойства");
        System.out.println("Название: "+shape.getName());
        System.out.println("Описание: "+shape.getDescription());
        System.out.println("Координаты: ("+shape.getX()+"; "+shape.getY()+")");
        Color color = new Color(shape.getColor());
        System.out.println("Цвет: "+ color);
        if (shape instanceof Triangle){
            System.out.println("Стороны: "+((Triangle) shape).getA()+", "+((Triangle) shape).getB()+", "+((Triangle) shape).getC());
        }
        else if (shape instanceof Rectangle){
            System.out.println("Стороны: "+((Rectangle) shape).getW()+", "+ ((Rectangle) shape).getH());
        }

        System.out.println("Методы");
        System.out.println("1. Передвинуть");
        System.out.println("2. Установить рандомный цвет");
        System.out.println("3. Посчитать периметр");
        System.out.println("4. Посчитать площадь");

        if (shape instanceof Triangle) {
            System.out.println("5. Является ли правильным");
            System.out.println("6. Является ли прямоугольгым");
        }
        else {
            System.out.println("5. Является ли квадратом");
            System.out.println("6. Длина диагоналей");
        }

        System.out.println("7. Изменить свойства");
        System.out.println("8. Удалить");
        System.out.println("9. В главное меню");

        System.out.print("Введите номер пункта: ");

        switch (scanner.nextInt()){
            case 1: {
                moving(shape,scanner,sessionFactory);
                break;
            }
            case 2: {
                shape.setRandomColor();
                updateShape(shape,sessionFactory);
                break;
            }
            case 3: {
                System.out.println("Периметр = "+shape.perimeter());
                interactWithShape(shape,scanner,sessionFactory);
                break;
            }
            case 4: {
                System.out.println("Площадь = "+shape.area());
                interactWithShape(shape,scanner,sessionFactory);
                break;
            }
            case 5: {
                if (shape instanceof Triangle) System.out.println(((Triangle) shape).isRegular());
                else System.out.println(((Rectangle) shape).isSquare());
                interactWithShape(shape,scanner,sessionFactory);
                break;
            }
            case 6: {
                if (shape instanceof Triangle) System.out.println(((Triangle) shape).isRight());
                else System.out.println(((Rectangle) shape).getDiagonales());
                interactWithShape(shape,scanner,sessionFactory);
                break;
            }
            case 7: {
                updateShapeMenu(shape,scanner,sessionFactory);
                break;
            }
            case 8: {
                deleteShape(shape,sessionFactory);
                break;
            }
            case 9: {
                break;
            }
        }
    }

    public static void updateShapeMenu(AbstractShape shape, Scanner scanner,SessionFactory sessionFactory) {
        System.out.println("Вы выбрали фигуру: " + shape);
        System.out.println("Свойства");
        System.out.println("1. Название: "+shape.getName());
        System.out.println("2. Описание: "+shape.getDescription());

        if (shape instanceof Triangle){
            System.out.println("3. Стороны: "+((Triangle) shape).getA()+", "+((Triangle) shape).getB()+", "+((Triangle) shape).getC());
        }
        else if (shape instanceof Rectangle){
            System.out.println("3. Стороны: "+((Rectangle) shape).getW()+", "+ ((Rectangle) shape).getH());
        }

        System.out.println("Введите номер пункта: ");
        switch (scanner.nextInt()){
            case 1: {
                System.out.println("Введите новое название: ");
                shape.setName(scanner.next());
                break;
            }
            case 2: {
                System.out.println("Введите новое описание: ");
                shape.setDescription(scanner.next());
                break;
            }
            case 3: {
                System.out.println("Введите новые стороны: ");
                if (shape instanceof Triangle) {
                    System.out.print("A: ");
                    ((Triangle) shape).setA(scanner.nextInt());
                    System.out.print("B: ");
                    ((Triangle) shape).setB(scanner.nextInt());
                    System.out.print("C: ");
                    ((Triangle) shape).setC(scanner.nextInt());
                } else {
                    System.out.print("H: ");
                    ((Rectangle) shape).setH(scanner.nextInt());
                    System.out.print("W: ");
                    ((Rectangle) shape).setW(scanner.nextInt());
                }
            }
            default: System.out.print("Неправильный ввод!");
        }
        updateShape(shape,sessionFactory);
        interactWithShape(shape,scanner,sessionFactory);
    }

    public static void updateShape(AbstractShape shape,SessionFactory sessionFactory){
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.update(shape);
        tx.commit();
        session.close();
    }

    public static void deleteShape(AbstractShape shape,SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.delete(shape);
        tx.commit();
        session.close();
    }

    public static void moving(AbstractShape shape, Scanner scanner,SessionFactory sessionFactory) {
        System.out.println("{"+shape.getX()+", "+shape.getY()+"}");
        System.out.println("1. Вверх");
        System.out.println("2. Вниз");
        System.out.println("3. Влево");
        System.out.println("4. Вправо");
        System.out.println("5. Назад");
        System.out.print("Выберите пункт: ");
        switch (scanner.nextInt()){
            case 1: {
                shape.move("up");
                updateShape(shape,sessionFactory);
                break;
            }
            case 2: {
                shape.move("down");
                updateShape(shape,sessionFactory);
                break;
            }
            case 3: {
                shape.move("left");
                updateShape(shape,sessionFactory);
                break;
            }
            case 4: {
                shape.move("right");
                updateShape(shape,sessionFactory);
                break;
            }
            case 5: {
                interactWithShape(shape,scanner,sessionFactory);
                break;
            }
            default: System.out.println("Введено неверное значение!");
        }
        moving(shape,scanner,sessionFactory);
    }
}
