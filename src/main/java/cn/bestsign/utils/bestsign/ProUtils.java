package cn.bestsign.utils.bestsign;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by dai on 2020/1/20.
 */
public class ProUtils {

    public static String getValue(String key,String path) {

        try {
            InputStream in = ClassLoader.class.getResourceAsStream(path);

            Properties prop = new Properties();

            prop.load(in);

            String value = prop.getProperty(key);

            return value;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    public static String getDefaultPathValue(String key){

        String configPath = "/developer.properties";


       return  getValue(key,configPath);
    }
}
