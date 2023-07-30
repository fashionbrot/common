package com.github.fashionbrot.common.ribbon.ping;


import com.github.fashionbrot.common.ribbon.Server;

public interface IPing {

    /**
     * Checks whether the given <code>Server</code> is "alive" i.e. should be
     * considered a candidate while loadbalancing
     */
    boolean isAlive(Server server);


}
