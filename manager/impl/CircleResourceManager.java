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

import javax.rmi.CORBA.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

/**
 * @Author: ro_kin
 * @Data:2022/12/4 11:12
 * @Description: TODO
 */
public class CircleResourceManager extends ResourceManager {
    private final PlatformContext context;
    public static final long discoveryWaitTime = 500 ; // (ms)
    public static final long heartbeatInterval = 1000 ; // (ms)
    public static final long nodeDeadInterval = 3*heartbeatInterval ; // (ms)
    public static final long threadSleepTime = 200 ; // (ms)
    private long lastGetHeartBeatTime;
    private long lastSendHeartBeatTime;

    private SubNet subNet;

    // heart pkg flow anticlockwise : nextNode -> this -> preNode
    private Node preNode;
    private Node nextNode;


    public CircleResourceManager() {
        context = PlatformContext.getInstance();
    }

    @Override
    public void run() {
        System.out.println("CircleResourceManager running!");

        nodeDiscovery(); // boadCast self to find neighbor
        MyUtils.sleep(discoveryWaitTime);   // take a break wait other discovery ack package
        addToNets();  //  add to network group
        netsMaintenance();  // make the nets keepAlive
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
           createNetworkBySelf();
        }
    }

    private void updateGetHeartBeatTime(){
        lastGetHeartBeatTime = System.currentTimeMillis();
    }

    private void updateSendHeartBeatTime(){
        lastSendHeartBeatTime = System.currentTimeMillis();
    }


    private void createNetworkBySelf() {
        int netNo = 1;  // sub net number
        List<Integer> sublist= new ArrayList<>(netNo);
        context.selfNode = new Node(1,sublist,MyConfig.myAddress);

        List<Node> lists = new ArrayList<>();
        lists.add(context.selfNode);
        SubNet subNet = new SubNet(netNo,lists);
        this.subNet = subNet;
        context.network.put(netNo,subNet);

        System.out.println("create a new network");
        MyUtils.printAllNodes(subNet);
    }

    private void netsMaintenance() {
        while (true){
            while (packageQueue.size() > 0){
                NetsMaintainPackage netsMaintainPackage = poll();
                disposePackage(netsMaintainPackage);
            }
            if (preNode != null && nextNode !=null){
                whetherNextNodeDead();
                SelfIsAlive();
            }
            // thread take a break
            MyUtils.sleep(threadSleepTime);
        }
    }

    // send a heartbeat pkg to preNode indicate I am alive
    private void SelfIsAlive() {
        if (System.currentTimeMillis() - lastSendHeartBeatTime >= heartbeatInterval){
            Address address = nextNode.getAddress();
            NetsMaintainPackage maintainPackage = new NetsMaintainPackage(NetsMaintainType.Heart, MyConfig.myAddress);
            TransmitPackage transmitPackage = new TransmitPackage(TransmitType.NetsMaintain,maintainPackage);
            context.sender.send(address,transmitPackage);  // send to nextNode address heartbeat
            updateSendHeartBeatTime();

            if ("true".equals(MyConfig.get("printHeartBeat"))){
                System.out.println("send heartbeat :" + address);
            }
        }
    }

    private void whetherNextNodeDead() {
        if (System.currentTimeMillis() - lastGetHeartBeatTime > nodeDeadInterval){

            subNet.remove(nextNode.getNo());
            NetsMaintainPackage maintainPackage = new NetsMaintainPackage(NetsMaintainType.NodeDel, nextNode.getNo());
            TransmitPackage transmitPackage = new TransmitPackage(TransmitType.NetsMaintain,maintainPackage);
            context.sender.boardCast(transmitPackage);  // board cast self address
            updatePreNextNode();
        }
    }

    private void disposePackage(NetsMaintainPackage pkg) {
        switch (pkg.getType()){
            case Discover:disposeDiscover(pkg.getBody());
            case DiscoverAck:disposeDiscoverAck(pkg.getBody());
            case Join:disposeJoin(pkg.getBody());
            case JoinAck:disposeJoinAck(pkg.getBody());
            case Heart:disposeHeart(pkg.getBody());
            case NodeAdd:disposeNodeAdd(pkg.getBody());
            case NodeDel:disposeNodeDel(pkg.getBody());
        }
    }


    private void disposeNodeDel(Object body) {
        Integer NodeNo = (Integer) body;
        subNet.remove(NodeNo);
        updatePreNextNode();
    }

    private void disposeNodeAdd(Object body) {
        if (context.selfNode != null){
            Node node = (Node) body;
            subNet.addNode(node);
            updatePreNextNode();
        }
    }

    private void disposeHeart(Object body) {
        Address address = (Address) body;
        // should judge whether address is nextNode
        if (!address.equals(context.selfNode.getAddress())){
            System.err.println(address + "should not send to me!");
            System.err.println(nextNode.getAddress() + "is my next Node!");
        }
        updateGetHeartBeatTime();
        if ("true".equals(MyConfig.get("printHeartBeat"))){
            System.out.println("get heartbeat :" + address);
        }
    }

    private void disposeJoinAck(Object body) {
        SubNet subNet = (SubNet) body;
        this.subNet = subNet;
        context.network.put(subNet.getNo(),subNet);
        context.selfNode = subNet.getLastNode();
        updatePreNextNode();
    }

    // group SAP dispose Join pkg to allow the node join
    private void disposeJoin(Object body) {
        int nextNo = subNet.getSubNextNo();
        Node node = new Node(nextNo,1,new Address((String) body));
        subNet.addNode(node); // add to node list

        // board cast new node to all nodes
        NetsMaintainPackage np = new NetsMaintainPackage(NetsMaintainType.NodeAdd,node);
        TransmitPackage tp = new TransmitPackage(TransmitType.NetsMaintain,np);
        context.sender.boardCast(tp);

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
        Address address = new Address((String) body);   // group SAP address
        NetsMaintainPackage maintainPackage = new NetsMaintainPackage(NetsMaintainType.Join, MyConfig.myAddress);
        TransmitPackage transmitPackage = new TransmitPackage(TransmitType.NetsMaintain,maintainPackage);
        context.sender.send(address,transmitPackage);  // send to target address self address
        System.out.println("Join to :"+address);
    }

    // group SAP dispose discover pkg
    private void disposeDiscover(Object body) {
        if (context.selfNode != null){
            Address address = new Address((String) body);  // the candidate address
            NetsMaintainPackage maintainPackage = new NetsMaintainPackage(NetsMaintainType.DiscoverAck, MyConfig.myAddress);
            TransmitPackage transmitPackage = new TransmitPackage(TransmitType.NetsMaintain,maintainPackage);
            context.sender.send(address,transmitPackage);  // send to target address self address
            System.out.println("disposeDiscover:"+address);
        }
    }

    // node board cast self address
    private void nodeDiscovery(){
        NetsMaintainPackage maintainPackage = new NetsMaintainPackage(NetsMaintainType.Discover, MyConfig.myAddress);
        TransmitPackage transmitPackage = new TransmitPackage(TransmitType.NetsMaintain,maintainPackage);
        context.sender.boardCast(transmitPackage);  // board cast self address
        System.out.println("nodeDiscovery");
    }

}
