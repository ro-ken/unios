package structure;

/**
 * @Author: ro_kin
 * @Data:2022/12/5 20:07
 * @Description: TCP/IP addr
 */
public class Address {
    private String ip;

    public Address(String ip) {
        this.ip = ip;

    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
