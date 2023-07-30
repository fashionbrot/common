package com.github.fashionbrot.common.ribbon.loadbalancer;


import com.github.fashionbrot.common.ribbon.Server;
import com.github.fashionbrot.common.ribbon.ping.IPing;
import com.github.fashionbrot.common.ribbon.rule.IRule;

import java.util.List;


public interface ILoadBalancer {



    void addServers(List<Server> newServers);

    void setServer(String serverAddress);

    List<Server> getAllServers();

    Server chooseServer();

    void setRule(IRule rule);

    IRule getRule();

    void setPing(IPing ping);

    IPing getPing();
}
