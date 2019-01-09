package com.hxshijie.service;

import com.hxshijie.Thread.ClientAgentThread;

public interface ClientService {
    void dokiDoki(ClientAgentThread clientAgentThread);

    void home_online(ClientAgentThread clientAgentThread);

    void result(ClientAgentThread clientAgentThread);

    void kicked(ClientAgentThread clientAgentThread);
}
