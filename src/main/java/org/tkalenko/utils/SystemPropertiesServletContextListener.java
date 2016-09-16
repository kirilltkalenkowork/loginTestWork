package org.tkalenko.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.StringTokenizer;

/**
 * Загрузка системных свойст
 */
public class SystemPropertiesServletContextListener implements ServletContextListener {
    private final static Logger log = LoggerFactory.getLogger(SystemPropertiesServletContextListener.class);
    private final static String PARAM_NAME = "system-property";

    @Override
    public void contextInitialized(final ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        if (servletContext != null) {
            String initParameter = servletContext.getInitParameter(PARAM_NAME);
            if (!StringUtils.isBlank(initParameter)) {
                log.debug(String.format("start init system parameters=[%s]", initParameter));
                StringTokenizer stringTokenizer = new StringTokenizer(initParameter);
                while (stringTokenizer.hasMoreTokens()) {
                    String systemPropertyString = stringTokenizer.nextToken();
                    log.debug(String.format("start parse system-property=[%s]", systemPropertyString));
                    StringTokenizer stringTokenizer1 = new StringTokenizer(systemPropertyString, "=", false);
                    if (stringTokenizer1.countTokens() == 2) {
                        String key = stringTokenizer1.nextToken();
                        String value = stringTokenizer1.nextToken();
                        log.debug(String.format("set system-property [%s]=[%s]", key, value));
                        System.setProperty(key, value);
                    }
                }
            }
        }
    }

    @Override
    public void contextDestroyed(final ServletContextEvent servletContextEvent) {
        log.debug(String.format("system properties context listener finish"));
    }
}
