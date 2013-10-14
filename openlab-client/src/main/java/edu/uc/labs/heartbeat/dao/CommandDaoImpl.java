package edu.uc.labs.heartbeat.dao;

import com.typesafe.config.Config;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Logger;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import edu.uc.labs.heartbeat.domain.*;

public class CommandDaoImpl implements CommandDao {

    public CommandDaoImpl(Config config) {
        this.config = config;
    }
    
    @Override
    public CommandResult run(Command c) {

        CommandResult cmdResult = new CommandResult();
        String cmd = c.getCmd();
        try {
            File f = new File(config.getString("heartbeat.scriptsdir") + "/" + cmd);
            if (f.exists()) {
                log.debug("The command file " + f.getAbsolutePath() + " exists...");
                // First try to execute a script/cmd in the scripts directory
                cmd = config.getString("heartbeat.scriptsdir") + "/" + c.getCmd();
                log.debug("Going to run " + f.getAbsolutePath());
            }
            
            ByteArrayOutputStream stdout = new ByteArrayOutputStream();
            PumpStreamHandler psh = new PumpStreamHandler(stdout);
            CommandLine cmdLine = CommandLine.parse(cmd);
            for (String args : c.getArgs()) {
                cmdLine.addArgument(args);
            }
            DefaultExecutor executor = new DefaultExecutor();
            executor.setStreamHandler(psh);
            int returnval = executor.execute(cmdLine);
            cmdResult.setMessage(stdout.toString().replaceAll("\\n", ""));
            cmdResult.setExitCode(returnval);
            return cmdResult;
        } catch (ExecuteException e) {
            log.error("Unable to run the command! " + e.getMessage());
            cmdResult.setMessage(e.getMessage());
            cmdResult.setExitCode(-1);
            return cmdResult;
        } catch (IOException ex) {
            cmdResult.setMessage(ex.getMessage());
            cmdResult.setExitCode(-1);
            return cmdResult;
        }
    }
    private Config config;
    private Logger log = Logger.getLogger(CommandDaoImpl.class);
}
