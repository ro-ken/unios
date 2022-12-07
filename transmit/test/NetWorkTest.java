package transmit.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author: ro_kin
 * @Data:2022/12/5 22:12
 * @Description: TODO
 */
public class NetWorkTest {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();

//        String hostAddress = InetAddress.getLocalHost().getHostAddress();
//        System.out.println(hostAddress);
        String hostName = address.getHostName();
        System.out.println("主机名为" + hostName);

        String ip = address.getHostAddress();
        System.out.println("IP为" + ip);
    }
}
