package structure;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ro_kin
 * @Data:2022/12/5 19:56
 * @Description: TODO
 */
public class SubNet implements Serializable {
    private static final long serialVersionUID = 1L;
    private int no;
    private List<Node> nodeList;

    public SubNet(int no,List<Node> nodeList) {
        this.no = no;
        this.nodeList = nodeList;
    }

    public int getNo() {
        return no;
    }

    // for new node allot no =  current max no + 1
    public int getSubNextNo() {
        Node node = nodeList.get(nodeList.size() - 1);
        return node.getNo() + 1;
    }

    public void addNode(Node node){
        nodeList.add(node);
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public Node getLastNode(){
        return nodeList.get(nodeList.size()-1);
    }

    // remove Node
    public void remove(Integer nodeNo) {
        for (int i = 0; i < nodeList.size(); i++) {
            if (nodeList.get(i).getNo() == nodeNo){
                nodeList.remove(i);
                break;
            }
        }
    }
}
