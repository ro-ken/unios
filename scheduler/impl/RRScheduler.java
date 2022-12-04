package scheduler.impl;

import scheduler.Scheduler;

/**
 * @Author: ro_kin
 * @Data:2022/12/4 11:07
 * @Description: round robin Scheduler
 */
public class RRScheduler implements Scheduler {
    @Override
    public void run() {
        System.out.println("RRScheduler running!");

    }
}
