package structure.type;

/**
 * @Author: ro_kin
 * @Data:2022/12/5 22:00
 * @Description: TODO
 */
public class TransmitPackage {
    TransmitType type;
    String body;

    public TransmitPackage(TransmitType type, String body) {
        this.type = type;
        this.body = body;
    }

    @Override
    public String toString() {
        return "TransmitPackage{" +
                "type=" + type +
                ", body='" + body + '\'' +
                '}';
    }
}
