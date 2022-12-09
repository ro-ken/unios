package structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ro_kin
 * @Data:2022/12/5 19:38
 * @Description: TODO
 */
public class Node implements Serializable {
    private static final long serialVersionUID = 1L;
    private int no;     // 节点编号
    private List<Integer> netsList;     // 节点属于子网络集合
    private Address address;

    public Node(int no, List<Integer> netsList, Address address) {
        this.no = no;
        this.netsList = netsList;
        this.address = address;
    }

    public Node(int no, int subNo, Address address) {
        this.no = no;
        this.netsList = new ArrayList<>();
        this.netsList.add(subNo);
        this.address = address;
    }

    public int getNo() {
        return no;
    }

    public List<Integer> getNetsList() {
        return netsList;
    }

    @Override
    public String toString() {
        return "Node[" +
                "no=" + no +
                ", addr=" + address.getLastAddr() +
                ']';
    }

    public Address getAddress() {
        return address;
    }
}
