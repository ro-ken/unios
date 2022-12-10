package manager;

import context.PlatformContext;
import structure.Node;
import structure.SubNet;
import structure.type.NetsMaintainPackage;
import structure.type.NetsMaintainType;
import structure.type.TransmitPackage;
import structure.type.TransmitType;
import util.MyConfig;
import util.MyUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @Author: ro_kin
 * @Data:2022/12/4 11:12
 * @Description: TODO
 */
public abstract class ResourceManager implements Runnable {
    protected Queue<NetsMaintainPackage> packageQueue;
    protected final PlatformContext context;

    protected SubNet subNet;
    protected long lastSendHeartBeatTime;
    public static final long threadSleepTime = 200 ; // (ms)
    public static final long heartbeatInterval = 1000 ; // (ms)
    public static final long nodeDeadInterval = 3*heartbeatInterval ; // (ms)

    public ResourceManager() {
        this.packageQueue = new LinkedList<>();
        this.context = PlatformContext.getInstance();
    }

    @Override
    public void run() {
        System.out.println(MyConfig.get("manager")+"ResourceManager running!");
        networkEstablishment();
        networkMaintenance();
    }

    // establish network
    protected abstract void networkEstablishment();


    protected void networkMaintenance() {
        while (true){
            while (packageQueue.size() > 0){
                NetsMaintainPackage netsMaintainPackage = poll();
                disposePackage(netsMaintainPackage);
            }

            if (subNet.getListSize() > 1){
                SelfIsAlive();
                OthersIsAlive();
            }
            // thread take a break
            MyUtils.sleep(threadSleepTime);
        }
    }

    protected abstract void disposePackage(NetsMaintainPackage netsMaintainPackage);
    // send a heartbeat pkg to other node indicate I am alive
    protected abstract void SelfIsAlive();
    protected abstract void OthersIsAlive();


    public void offer(NetsMaintainPackage netsMaintainPackage){
        packageQueue.offer(netsMaintainPackage);
    }

    protected NetsMaintainPackage poll(){
        return packageQueue.poll();
    }

    protected NetsMaintainPackage peek(){
        return packageQueue.peek();
    }

    protected void clear(){
        packageQueue.clear();
    }

    // node board cast self address
    protected void nodeDiscovery(){
        NetsMaintainPackage maintainPackage = new NetsMaintainPackage(NetsMaintainType.Discover, MyConfig.myAddress);
        TransmitPackage transmitPackage = new TransmitPackage(TransmitType.NetsMaintain,maintainPackage);
        context.sender.boardCast(transmitPackage);  // board cast self address
        System.out.println("nodeDiscovery");
    }

    protected void createNetworkBySelf() {
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

    protected void updateSendHeartBeatTime(){
        lastSendHeartBeatTime = System.currentTimeMillis();
    }
}
