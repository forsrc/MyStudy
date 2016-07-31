package com.forsrc.listener;

import com.forsrc.utils.ExportDb;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * The type Init db listener.
 */
public class InitDbListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        ExportDb.init();
    }

}
