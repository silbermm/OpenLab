package edu.uc.labs.heartbeat.dao;

import edu.uc.labs.heartbeat.domain.ClientMachine;

public interface MachineDao {
    ClientMachine getMachineInfo();

    String findLoggedInUser();

    String findComputerName();

    String findUUID();

    void writeToFile(ClientMachine m);

    void sendToServer(ClientMachine m);

    void sendToServer(String uuid);
}
