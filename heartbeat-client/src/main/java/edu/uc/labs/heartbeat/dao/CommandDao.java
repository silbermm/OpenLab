package edu.uc.labs.heartbeat.dao;

import edu.uc.labs.heartbeat.domain.Command;

public interface CommandDao {
	public String run(Command c);
}
