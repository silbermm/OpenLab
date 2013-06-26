package edu.uc.labs.heartbeat.dao;


import com.typesafe.config.Config;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Logger;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uc.labs.heartbeat.domain.*;


public class CommandDaoImpl implements CommandDao {

	public CommandDaoImpl(Config config){
		this.config = config;
	}


	public String run(Command c){
	 
		String s = null;	
		String cmd = c.getCmd();	
		try {
			File f = new File(config.getString("heartbeat.scriptsdir") + "/" + cmd);
			if(f.exists()){
				// First try to execute a script/cmd in the scripts directory
				cmd = config.getString("heartbeat.scriptsdir") + "/" + c.getCmd();
			} 
			ByteArrayOutputStream stdout = new ByteArrayOutputStream();
		  PumpStreamHandler psh = new PumpStreamHandler(stdout);
			CommandLine cmdLine = CommandLine.parse(cmd);	
			for(String args : c.getArgs()){
				cmdLine.addArgument(args);	
			}	
			DefaultExecutor executor = new DefaultExecutor();
			executor.setStreamHandler(psh);
			int returnval = executor.execute(cmdLine);
			s = stdout.toString().replaceAll("\\n", "");
			return "Return value: " + returnval + "\n" + s;			
	 	} catch (ExecuteException e) {
			return e.getMessage();	
		}	catch (IOException e) {
			return e.getMessage();
		}
	}

	private Config config;
}
