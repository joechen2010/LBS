package cn.edu.nju.software.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

public class PropertyUtils {

    private static final String        DEFAULT_ENCODING    = "UTF-8";

    //    private static Logger              logger              = LoggerFactory.getLogger(PropertyUtils.class);

    private static PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();

    private static ResourceLoader      resourceLoader      = new DefaultResourceLoader();

    public static Properties loadProperties(String... locations) {
        Properties props = new Properties();
        for (String location : locations) {

            InputStream is = null;
            try {
                Resource resource = resourceLoader.getResource(location);
                is = resource.getInputStream();
                propertiesPersister.load(props, new InputStreamReader(is, DEFAULT_ENCODING));
            } catch (IOException ex) {
                //                logger.info("Could not load properties from classpath:" + location + ": " + ex.getMessage());
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        //                        logger.warn("Error when closing the resource inputStream.", e);
                    }
                }
            }
        }
        return props;
    }

}
