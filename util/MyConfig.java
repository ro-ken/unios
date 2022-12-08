package util;

import structure.Address;

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
    public static Address myAddress ;
    static {
        try {
            String filename = "config.properties";
            config.load(new FileInputStream(MyUtils.getResPath(filename)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // set localhost IP
        if ("auto".equals(config.getProperty("getAddressMode"))){
            myAddress = MyUtils.getHostIp();
        }else {
            myAddress= new Address(config.getProperty("address"));
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
