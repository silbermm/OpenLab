package edu.uc.labs.heartbeat.domain;

import java.util.*;

public class Command {

    private String cmd;
    private List<String> args = new ArrayList<String>();

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getCmd() {
        return this.cmd;
    }

    public void addArg(String arg) {
        this.args.add(arg);
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public List<String> getArgs() {
        return this.args;
    }
}
