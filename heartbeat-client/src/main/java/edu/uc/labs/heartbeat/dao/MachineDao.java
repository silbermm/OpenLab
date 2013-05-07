package edu.uc.labs.heartbeat.dao;

import edu.uc.labs.heartbeat.domain.Machine;

public interface MachineDao {
    Machine getMachineInfo();

    String findLoggedInUser();

    String findComputerName();

    String findUUID();

    void writeToFile(Machine m);

    void sendToServer(Machine m);

    void sendToServer(String uuid);
}
