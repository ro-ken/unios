package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @Author: ro_kin
 * @Data:2022/12/4 11:41
 * @Description: TODO
 */
public class MyConfig {
    private static Properties config = new Properties();
    static {
        try {
            String filename = "config.properties";
            config.load(new FileInputStream(MyUtils.getResPath(filename)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get properties values
     * @param key
     * @return
     */
    public static String get(String key){
        return config.getProperty(key);
    }
}
