package structure.type;

import java.io.Serializable;

/**
 * @Author: ro_kin
 * @Data:2022/12/7 10:38
 * @Description: TODO
 */
public class NetsMaintainPackage implements Serializable {
    private static final long serialVersionUID = 1L;
    private NetsMaintainType type;
    private Object body;

    @Override
    public String toString() {
        return "NetsMaintainPackage{" +
                "type=" + type +
                ", body=" + body +
                '}';
    }

    public NetsMaintainPackage(NetsMaintainType type){

    }

    public NetsMaintainPackage(NetsMaintainType type, Object body) {
        this.type = type;
        this.body = body;
    }

    public NetsMaintainType getType() {
        return type;
    }

    public Object getBody() {
        return body;
    }
}
