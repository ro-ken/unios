package manager;

import structure.type.NetsMaintainPackage;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @Author: ro_kin
 * @Data:2022/12/4 11:12
 * @Description: TODO
 */
public abstract class ResourceManager implements Runnable {
    protected Queue<NetsMaintainPackage> packageQueue;

    public ResourceManager() {
        this.packageQueue = new LinkedList<>();
    }

    public void offer(NetsMaintainPackage netsMaintainPackage){
        packageQueue.offer(netsMaintainPackage);
    }

    protected NetsMaintainPackage poll(){
        return packageQueue.poll();
    }

    protected NetsMaintainPackage peek(){
        return packageQueue.peek();
    }



}
