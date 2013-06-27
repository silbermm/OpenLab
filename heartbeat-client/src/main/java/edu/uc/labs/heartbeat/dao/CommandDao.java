package edu.uc.labs.heartbeat.dao;

import edu.uc.labs.heartbeat.domain.Command;
import edu.uc.labs.heartbeat.domain.CommandResult;

public interface CommandDao {
	public CommandResult run(Command c);
}
