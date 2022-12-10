package manager.impl;

import context.PlatformContext;
import manager.ResourceManager;
import structure.Address;
import structure.Node;
import structure.SubNet;
import structure.type.NetsMaintainPackage;
import structure.type.NetsMaintainType;
import structure.type.TransmitPackage;
import structure.type.TransmitType;
import util.MyConfig;
import util.MyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ro_kin
 * @Data:2022/12/4 11:12
 * @Description: TODO
 */
public class CircleResourceManager extends ResourceManager {

    public static final long discoveryWaitTime = 500 ; // (ms)


    private long lastGetHeartBeatTime;


    // heart pkg flow anticlockwise : preNode <- this <- preNode
    private Node preNode;
    private Node nextNode;

    @Override
    protected void networkEstablishment() {
        nodeDiscovery(); // boadCast self to find neighbor
        MyUtils.sleep(discoveryWaitTime);   // take a break wait other discovery ack package
        addToNets();  //  add to network group
    }

    @Override
    // to judge whether the next node is dead or not
    protected void OthersIsAlive() {
        if (System.currentTimeMillis() - lastGetHeartBeatTime > nodeDeadInterval){
            System.out.println(nextNode + " is dead !");
            int no = nextNode.getNo();
            subNet.remove(no);
            NetsMaintainPackage maintainPackage = new NetsMaintainPackage(NetsMaintainType.NodeDel, no);
            TransmitPackage transmitPackage = new TransmitPackage(TransmitType.NetsMaintain,maintainPackage);
            context.sender.boardCast(transmitPackage);  // board cast dead node no
            updatePreNextNode();
        }
    }

    @Override
    // send a heartbeat pkg to preNode indicate I am alive
    protected void SelfIsAlive() {
        if (System.currentTimeMillis() - lastSendHeartBeatTime >= heartbeatInterval){
            Address address = preNode.getAddress();
            NetsMaintainPackage maintainPackage = new NetsMaintainPackage(NetsMaintainType.Heart, MyConfig.myAddress);
            TransmitPackage transmitPackage = new TransmitPackage(TransmitType.NetsMaintain,maintainPackage);
            context.sender.send(address,transmitPackage);  // send to nextNode address heartbeat
            updateSendHeartBeatTime();

            if ("true".equals(MyConfig.get("printHeartBeat"))){
                System.out.println("send heartbeat :" + address);
            }
        }
    }

    @Override
    protected void disposePackage(NetsMaintainPackage pkg) {
        switch (pkg.getType()){
            case Discover:disposeDiscover(pkg.getBody());  break;
//            case DiscoverAck:disposeDiscoverAck(pkg.getBody());  break;//this stage should receive this package
            case Join:disposeJoin(pkg.getBody());   break;
            case JoinAck:disposeJoinAck(pkg.getBody());  break;
            case Heart:disposeHeart(pkg.getBody());   break;
            case NodeAdd:disposeNodeAdd(pkg.getBody());  break;
            case NodeDel:disposeNodeDel(pkg.getBody());  break;
        }
    }


    private void addToNets() {
        Object pkgBody = null;
        while (packageQueue.size() > 0){
            NetsMaintainPackage pkg = poll();
            if (pkg.getType() == NetsMaintainType.DiscoverAck){
                // if has more than node reply，choose first node reply
                if (pkgBody == null)
                    pkgBody = pkg.getBody();
//                disposeDiscoverAck(pkg.getBody());
            }else {
                System.err.println("receiver other package in discovery stage :" + pkg);
            }
        }
        if (pkgBody != null) {
            disposeDiscoverAck(pkgBody);
        } else{
           // didn't find other node,self create a network
           super.createNetworkBySelf();
        }
    }

    private void updateGetHeartBeatTime(){
        lastGetHeartBeatTime = System.currentTimeMillis();
    }




    private void disposeNodeDel(Object body) {
        Integer NodeNo = (Integer) body;
        subNet.remove(NodeNo);
        updatePreNextNode();
    }

    private void disposeNodeAdd(Object body) {
        if (context.selfNode != null){
            Node node = (Node) body;
            // if node is myself ,don't add it
            if (!context.selfNode.getAddress().equals(node.getAddress())){
                subNet.addNode(node);
                updatePreNextNode();
            }
        }
    }

    private void disposeHeart(Object body) {
        Address address = (Address) body;
        // should judge whether address is nextNode
        if (!address.equals(nextNode.getAddress())){
            System.err.println(address + "should not send to me!");
            System.err.println(nextNode.getAddress() + "is my next Node!");
        }
        updateGetHeartBeatTime();
        if ("true".equals(MyConfig.get("printHeartBeat"))){
            System.out.println("get heartbeat :" + address);
        }
    }

    private void disposeJoinAck(Object body) {
        System.out.println(body);
        SubNet subNet = (SubNet) body;
        this.subNet = subNet;
        context.network.put(subNet.getNo(),subNet);
        context.selfNode = subNet.getLastNode();
        updatePreNextNode();
    }

    // group SAP dispose Join pkg to allow the node join
    private void disposeJoin(Object body) {
        int nextNo = subNet.getSubNextNo();
        Node node = new Node(nextNo,1, (Address) body);
        subNet.addNode(node); // add to node list

        // board cast new node to all nodes
        NetsMaintainPackage np = new NetsMaintainPackage(NetsMaintainType.NodeAdd,node);
        TransmitPackage tp = new TransmitPackage(TransmitType.NetsMaintain,np);
        context.sender.boardCast(tp);

        MyUtils.sleep(100);

        // synchronization the subnet to new node
        np = new NetsMaintainPackage(NetsMaintainType.JoinAck,subNet);
        tp = new TransmitPackage(TransmitType.NetsMaintain,np);
        context.sender.send(node.getAddress(),tp);

        updatePreNextNode();
    }

    // node group has changed , update node's pre and next relationship
    private void updatePreNextNode() {
        List<Node> nodeList = subNet.getNodeList();
        int size = nodeList.size();
        if (size == 1){
            preNode = null;
            nextNode = null;
        }else{
            int i = 0;
            for (; i < size; i++) {
                if (nodeList.get(i) == context.selfNode){
                    break;
                }
            }
            assert i < size;
            int preIndex = (size+i-1) % size;
            int nextIndex = (i+1) % size;
            preNode = nodeList.get(preIndex);
            nextNode = nodeList.get(nextIndex);

            updateGetHeartBeatTime();
            updateSendHeartBeatTime();
            MyUtils.printAllNodes(subNet);
        }
    }

    // group candidate dispose SAP discoverACK pkg to join the group
    private void disposeDiscoverAck(Object body) {
        Address address = (Address) body;   // group SAP address
        NetsMaintainPackage maintainPackage = new NetsMaintainPackage(NetsMaintainType.Join, MyConfig.myAddress);
        TransmitPackage transmitPackage = new TransmitPackage(TransmitType.NetsMaintain,maintainPackage);
        context.sender.send(address,transmitPackage);  // send to target address self address
        System.out.println("Join to :"+address);
    }

    // group SAP dispose discover pkg
    private void disposeDiscover(Object body) {
        if (context.selfNode != null){
            Address address = (Address) body;  // the candidate address
            NetsMaintainPackage maintainPackage = new NetsMaintainPackage(NetsMaintainType.DiscoverAck, MyConfig.myAddress);
            TransmitPackage transmitPackage = new TransmitPackage(TransmitType.NetsMaintain,maintainPackage);
            context.sender.send(address,transmitPackage);  // send to target address self address
            System.out.println("disposeDiscover:"+address);
        }
    }

}
