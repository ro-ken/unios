package structure.type;

import structure.Address;

import java.io.*;

/**
 * @Author: ro_kin
 * @Data:2022/12/5 22:00
 * @Description: TODO
 */
public class TransmitPackage implements Serializable {
    private static final long serialVersionUID = 1L;

    private Address src;
    private TransmitType type;

    public void setSrc(Address src) {
        this.src = src;
    }

    private Object body;

    public Address getSrc() {
        return src;
    }

    public TransmitType getType() {
        return type;
    }

    public Object getBody() {
        return body;
    }

    public TransmitPackage(TransmitType type, Object body) {
        this.type = type;
        this.body = body;
    }

    @Override
    public String toString() {
        return "TransmitPackage{" +
                "src=" + src +
                ", type=" + type +
                ", body=" + body +
                '}';
    }

    // make this object Serialize to byte array
    public byte[] getBytes(){
        //对象->对象流->字节数组流->字节数组
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = null;
        byte[] arr = null;
        try {
            objectStream = new ObjectOutputStream(byteArrayStream);
            objectStream.writeObject(this);
            arr = byteArrayStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                byteArrayStream.close();
                if (objectStream != null){
                    objectStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return arr;
    }
}
