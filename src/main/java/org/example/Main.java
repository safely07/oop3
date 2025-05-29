package org.example;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import java.util.Scanner;

import static org.example.Menu.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        while (true) {
            System.out.println("Главное меню:");
            System.out.println("1. Добавить объект");
            System.out.println("2. Показать список фигур");
            System.out.println("3. Показать список картинок");
            System.out.println("4. Добавить картинку");
            System.out.println("5. Выйти");
            System.out.print("Выберите пункт меню: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addShape(scanner,sessionFactory);
                    break;
                case 2:
                    listAndInteractWithShapes(scanner, sessionFactory);
                    break;
                case 3:
                    listOfPictures(scanner,sessionFactory);
                    break;
                case 4:
                    addPicture(scanner,sessionFactory);
                    break;
                case 5:
                    sessionFactory.close();
                    System.exit(0);
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    }