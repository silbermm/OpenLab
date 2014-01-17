
package co.silbersoft.openlab.utils;

import co.silbersoft.openlab.models.Machine;
import edu.uc.labs.heartbeat.domain.ClientMachine;

/**
 *
 * @author silbermm
 */
public class HeartbeatUtil {
    public static ClientMachine convertMachineToClientMachine(Machine m){
        ClientMachine cm = new ClientMachine();
        cm.setCurrentUser(m.getCurrentUser());
        cm.setMac(m.getMac());
        cm.setManufacturer(m.getManufacturer());
        cm.setModel(m.getModel());
        cm.setName(m.getName());
        cm.setOs(m.getOs());
        cm.setOsVersion(m.getOs());
        cm.setSerialNumber(m.getSerialNumber());
        cm.setUuid(m.getUid());
        return cm;
    }
    
    
}
