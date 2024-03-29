package com.app.service.services;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;

import java.math.BigDecimal;
import java.util.Scanner;

public class UserDataService {

    private Scanner sc = new Scanner(System.in);

    public int getInt(String message) {
        System.out.println(message);
        var text = sc.nextLine();
        if (!text.matches("\\d+")) {
            throw new MyException(ExceptionCode.USER_DATA_SERVICE, "INVALID NUMBER");
        }
        return Integer.parseInt(text);
    }

    public BigDecimal getBigDecimal(String message){
        System.out.println(message);
        var text = sc.nextLine();
        if (!text.matches("\\d+")){
            throw new MyException(ExceptionCode.USER_DATA_SERVICE, "INVALID BIG DECIMAL PRICE");
        }
        return new BigDecimal(text.replaceAll(",", ""));
    }

    public String getString(String message){
        System.out.println(message);
        var text = sc.nextLine();
        return text;
    }

    public void close() {
        if (sc != null) {
            sc.close();
            sc = null;
        }
    }

}
