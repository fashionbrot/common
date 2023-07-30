package com.github.fashionbrot.common;

import com.github.fashionbrot.common.ribbon.Server;
import com.github.fashionbrot.common.ribbon.loadbalancer.BaseLoadBalancer;
import com.github.fashionbrot.common.ribbon.loadbalancer.ILoadBalancer;
import com.github.fashionbrot.common.ribbon.rule.RandomRule;

/**
 * @author fashionbrot
 */
public class RibbonTest {

    public static void main(String[] args) {
        ILoadBalancer balancer = new BaseLoadBalancer();
        balancer.setServer("https://a.com,http://b.com,http://c.com,d.com");
        balancer.setRule(new RandomRule());
        for (int i = 0; i < 20; i++) {
            Server server = balancer.chooseServer();
            System.out.println(server);
        }


    }
}
