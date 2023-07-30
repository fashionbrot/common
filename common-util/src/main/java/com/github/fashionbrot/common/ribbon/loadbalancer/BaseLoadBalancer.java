package com.github.fashionbrot.common.ribbon.loadbalancer;


import com.github.fashionbrot.common.consts.ProtocolConst;
import com.github.fashionbrot.common.ribbon.Server;
import com.github.fashionbrot.common.ribbon.ping.DefaultPing;
import com.github.fashionbrot.common.ribbon.ping.IPing;
import com.github.fashionbrot.common.ribbon.rule.IRule;
import com.github.fashionbrot.common.ribbon.rule.RoundRobinRule;
import com.github.fashionbrot.common.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@Slf4j
public class BaseLoadBalancer implements ILoadBalancer {

    protected volatile List<Server> allServerList = Collections.synchronizedList(new ArrayList<Server>());

    protected ReadWriteLock allServerLock = new ReentrantReadWriteLock();

    protected IRule rule = new RoundRobinRule();

    protected IPing ping = new DefaultPing();

    @Override
    public void addServers(List<Server> serverList) {
        if (ObjectUtil.isNotEmpty(serverList)){
            setServersList(serverList);
        }
    }


    @Override
    public void setServer(String serverAddress) {

        if (ObjectUtil.isNotEmpty(serverAddress)) {
            String[] serverSplit = serverAddress.split(",");
            List<Server> serverList = new ArrayList<>(serverSplit.length);
            for (String url : serverSplit) {

                Server newServer=new Server();
                String[] urlSplit = url.split("//");
                if (url.startsWith(ProtocolConst.HTTPS_SCHEME)){
                    newServer.setScheme(ProtocolConst.HTTPS_SCHEME);
                    newServer.setHost(urlSplit[1]);
                }else if (url.startsWith(ProtocolConst.HTTP_SCHEME)){
                    newServer.setScheme(ProtocolConst.HTTP_SCHEME);
                    newServer.setHost(urlSplit[1]);
                }else{
                    newServer.setScheme(ProtocolConst.HTTP_SCHEME);
                    newServer.setHost(urlSplit[0]);
                }

                serverList.add(newServer);
            }
            this.addServers(serverList);
        }
    }

    @Override
    public List<Server> getAllServers() {
        return allServerList;
    }

    @Override
    public Server chooseServer() {
        Lock lock = allServerLock.readLock();
        lock.lock();
        try {
            return rule.choose(this);
        }finally {
            lock.unlock();
        }

    }

    @Override
    public void setRule(IRule rule) {
        if (rule!=null){
            this.rule = rule;
        }
    }

    @Override
    public IRule getRule() {
        return this.rule;
    }

    @Override
    public void setPing(IPing ping) {
        if (ping!=null){
            this.ping = ping;
        }
    }

    @Override
    public IPing getPing() {
        return this.ping;
    }



    /**
     * Set the list of servers used as the server pool. This overrides existing
     * server list.
     */
    public void setServersList(List<Server> serverList) {
        Lock writeLock = allServerLock.writeLock();

        writeLock.lock();
        try {
            ArrayList<Server> allServers = new ArrayList<Server>();
            for (Server server : serverList) {
                if (server == null) {
                    continue;
                }
                allServers.add(server);
            }

            // This will reset readyToServe flag to true on all servers
            // regardless whether
            // previous priming connections are success or not
            allServerList = allServers;
        } finally {
            writeLock.unlock();
        }
    }

}
