package structure;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * @Author: ro_kin
 * @Data:2022/12/5 20:07
 * @Description: TCP/IP addr
 */
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;
    private String ip;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(ip, address.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip);
    }

    @Override
    public String toString() {
        return "Address[" + ip  + ']';
    }

    public Address(){

    }

    public Address(String ip) {
        this.ip = ip;
    }

    public InetAddress toInetAddress(){
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(this.ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return inetAddress;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
