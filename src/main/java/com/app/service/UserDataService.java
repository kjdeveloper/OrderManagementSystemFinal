package com.app.service;

import com.app.exceptions.MyException;

import java.util.Scanner;

public class UserDataService {

    private Scanner sc = new Scanner(System.in);

    public int getInt(String message) {
        System.out.println(message);
        String text = sc.nextLine();
        if (!text.matches("\\d+")) {
            throw new MyException("INVALID OPTION NUMBER");
        }
        return Integer.parseInt(text);
    }

    public void close() {
        if (sc != null) {
            sc.close();
            sc = null;
        }
    }

}
