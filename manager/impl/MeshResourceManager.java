package manager.impl;

import manager.ResourceManager;
import structure.Address;
import structure.Node;
import structure.type.NetsMaintainPackage;
import structure.type.NetsMaintainType;
import structure.type.TransmitPackage;
import structure.type.TransmitType;
import util.MyConfig;
import util.MyUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ro_kin
 * @Data:2022/12/10 11:23
 * @Description: TODO
 */
public class MeshResourceManager extends ResourceManager {
    private Map<Address,Node> neighbour;

    @Override
    protected void networkEstablishment() {
        neighbour = new HashMap<>();
        super.createNetworkBySelf();
        super.nodeDiscovery();
    }

    @Override
    protected void OthersIsAlive() {
        long now = System.currentTimeMillis();
        for (Address address:neighbour.keySet()){
            Node node = neighbour.get(address);
            if (now - node.getLastGetHeartBeatTime() > nodeDeadInterval){
                System.out.println(node + " is dead !");
                removeDeadNode(node);
            }
        }
    }

    private void removeDeadNode(Node node) {
        subNet.remove(node.getNo());
        neighbour.remove(node.getAddress());
    }

    @Override
    protected void SelfIsAlive() {
        if (System.currentTimeMillis() - lastSendHeartBeatTime >= heartbeatInterval){
            for (Address address:neighbour.keySet()){
                NetsMaintainPackage maintainPackage = new NetsMaintainPackage(NetsMaintainType.Heart, MyConfig.myAddress);
                TransmitPackage transmitPackage = new TransmitPackage(TransmitType.NetsMaintain,maintainPackage);
                context.sender.send(address,transmitPackage);  // send to nextNode address heartbeat
                updateSendHeartBeatTime();
            }
            if ("true".equals(MyConfig.get("printHeartBeat"))){
                System.out.println("send heartbeat to neighbour:" + neighbour.size());
            }
        }
    }

    @Override
    protected void disposePackage(NetsMaintainPackage pkg) {
        switch (pkg.getType()){
            case Discover:disposeDiscover(pkg.getBody());  break;
            case Heart:disposeHeart(pkg.getBody());   break;
            default:break;
        }
    }

    private void disposeHeart(Object body) {
        Address address = (Address) body;
        if (neighbour.containsKey(address)){
            Node node = neighbour.get(address);
            node.setLastGetHeartBeatTime(System.currentTimeMillis()); // update get time
        }else {
            addNewNode(address);
        }
    }

    private void addNewNode(Address address) {
        Node node = new Node(subNet.getSubNextNo(),1,address);
        node.setLastGetHeartBeatTime(System.currentTimeMillis());
        super.subNet.addNode(node);
        neighbour.put(address,node);
    }

    private void disposeDiscover(Object body) {
        Address address = (Address) body;
        addNewNode(address);
        System.out.println("disposeDiscover:"+address);
    }
}
