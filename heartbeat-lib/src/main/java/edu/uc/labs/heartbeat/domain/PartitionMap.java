package edu.uc.labs.heartbeat.domain;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

public class PartitionMap implements Serializable {

    private String part1 = null;
    private String part2 = null;
    private String part3 = null;
    private String part4 = null;

    public PartitionMap(Map<Integer, String> parts) {
        Iterator it = parts.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            if(pairs.getKey() == 1) { this.setPart1((String) pairs.getValue());}
            if(pairs.getKey() == 2) { this.setPart2((String) pairs.getValue());}
            if(pairs.getKey() == 3) { this.setPart3((String) pairs.getValue()); }
            if(pairs.getKey() == 4) { this.setPart4((String) pairs.getValue()); }
            it.remove();
        }
    }

    /**
     * @return the part1
     */
    public String getPart1() {
        return part1;
    }

    /**
     * @param part1 the part1 to set
     */
    public void setPart1(String part1) {
        this.part1 = part1;
    }

    /**
     * @return the part2
     */
    public String getPart2() {
        return part2;
    }

    /**
     * @param part2 the part2 to set
     */
    public void setPart2(String part2) {
        this.part2 = part2;
    }

    /**
     * @return the part3
     */
    public String getPart3() {
        return part3;
    }

    /**
     * @param part3 the part3 to set
     */
    public void setPart3(String part3) {
        this.part3 = part3;
    }

    /**
     * @return the part4
     */
    public String getPart4() {
        return part4;
    }

    /**
     * @param part4 the part4 to set
     */
    public void setPart4(String part4) {
        this.part4 = part4;
    }
}
