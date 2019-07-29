package com.app;

import com.app.mainmenu.menu.MenuService;

public class App {
    public static void main(String[] args) {

        MenuService menuService = new MenuService();
        menuService.service();
    }
}
