package util;

import java.util.Objects;

/**
 * @Author: ro_kin
 * @Data:2022/12/4 11:50
 * @Description: TODO
 */
public class MyUtils {
    public static String root;
    static {
        root = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
    }
    public static String getResPath(String res){
        return root + res;
    }

//    public static get
}
