package com.github.fashionbrot.common.ribbon.ping;

import com.github.fashionbrot.common.ribbon.Server;

/**
 * @author fashionbrot
 */
public class DefaultPing implements IPing{

    @Override
    public boolean isAlive(Server server) {
        return true;
    }
}
