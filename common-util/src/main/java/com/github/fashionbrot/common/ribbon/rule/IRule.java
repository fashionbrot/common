package com.github.fashionbrot.common.ribbon.rule;


import com.github.fashionbrot.common.ribbon.Server;
import com.github.fashionbrot.common.ribbon.loadbalancer.ILoadBalancer;

public interface IRule {

    /**
     * choose a server  from load balancer
     * @param lb
     * @return Server
     */
    Server choose(ILoadBalancer lb);
}
