/**
 * @Author: ro_kin
 * @Data:2022/12/7 21:27
 * @Description: TODO
 */
public class ltest {
    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
//        System.out.println(startTime);
//        Thread.sleep(2000);
//        System.out.println(System.currentTimeMillis());
        String ip = "192.168.1.56";
        System.out.println(ip.substring(ip.lastIndexOf('.')));
    }
}
