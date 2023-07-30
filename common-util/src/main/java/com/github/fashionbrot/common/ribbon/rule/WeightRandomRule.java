package com.github.fashionbrot.common.ribbon.rule;

import com.github.fashionbrot.common.ribbon.Server;
import com.github.fashionbrot.common.ribbon.loadbalancer.ILoadBalancer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;

/**
 * 加权随机
 * @author fashionbrot
 */
@Slf4j
public class WeightRandomRule implements IRule{


    @Override
    public Server choose(ILoadBalancer lb) {
        if (lb == null) {
            log.warn("no load balancer");
            return null;
        }
        List<Server> serverList = lb.getAllServers();

        int sumWeight = 0;
        for (Server server : serverList) {
            sumWeight += server.getWeight();
        }
        Random serverSelector = new Random();
        int nextServerRange = serverSelector.nextInt(sumWeight);
        int sum = 0;
        Server selectedServer = null;
        for (Server server : serverList) {
            if (nextServerRange >= sum && nextServerRange < server.getWeight() + sum) {
                selectedServer = server;
            }
            sum += server.getWeight();
        }
        return selectedServer;
    }
}
