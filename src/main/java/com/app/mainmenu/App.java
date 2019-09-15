package com.app.mainmenu;

import com.app.exceptions.MyException;
import com.app.mainmenu.menu.MenuService;
import com.app.model.Stock;
import com.app.repository.StockRepository;
import com.app.repository.impl.StockRepositoryImpl;

import java.util.Comparator;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {

        MenuService menuService = new MenuService();
        menuService.service();
    }
}
