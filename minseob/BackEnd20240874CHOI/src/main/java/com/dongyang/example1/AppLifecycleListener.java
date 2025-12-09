package com.dongyang.example1;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.Date;

@WebListener
public class AppLifecycleListener implements ServletContextListener {

    
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        System.out.println("---------------------------");
        System.out.println("웹 애플리케이션 시작됨 >>>> " + new Date());
        System.out.println("서버정보 : " + context.getServerInfo());
    }


    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("---------------------------");
        System.out.println("웹 애플리케이션 종료됨 >>>> " + new Date());
    }
}
