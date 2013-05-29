package edu.uc.labs.heartbeat.dao;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.EnumVariant;
import com.jacob.com.Variant;
import com.typesafe.config.Config;
import edu.uc.labs.heartbeat.domain.Machine;
import edu.uc.labs.heartbeat.domain.MachineGroup;
import edu.uc.labs.heartbeat.utils.HeartbeatPropertiesLoader;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MachineDaoImpl implements MachineDao {

    private static final Logger log = Logger.getLogger(MachineDaoImpl.class);
    private Config config;
    private RestTemplate restTemplate;
    private MachineGroup g = new MachineGroup();

    public MachineDaoImpl(Config config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }

    public Machine getMachineInfo() {
        Machine m = new Machine();
        m.setOs(System.getProperty("os.name"));
        m.setOsVersion(System.getProperty("os.version"));
        m.setName(getCompName());
        m.setSerialNumber(getSerialNumber());
        m.setManufacturer(getManufacturer());
        m.setModel(getModel());
        m.setMac(getMacAddresses());
        m.setUuid(getUuid());
        m.setCurrentUser(getUser());
        return m;
    }

    public String findLoggedInUser() {
        return getUser();
    }

    public String findComputerName() {
        return this.getCompName();
    }

    public String findUUID() {
        return this.getUuid();
    }

    public void writeToFile(Machine m) {
        File savedInfo = new File(HeartbeatPropertiesLoader.getFilename(config));
        try {
            Properties props = new Properties();
            //props.load(new FileInputStream(savedInfo));
            props.setProperty("uuid", m.getUuid());
            props.setProperty("serialnumber", m.getSerialNumber());
            props.setProperty("macaddresses", m.getMac());
            props.setProperty("os", m.getOs());
            props.setProperty("osversion", m.getOsVersion());
            props.setProperty("manufacturer", m.getManufacturer());
            props.setProperty("model", m.getModel());
            props.store(new FileOutputStream(savedInfo), "Heartbeat Auto Generated Properties");
        } catch (IOException ex) {
            log.info("Unable to write the properties file " + savedInfo.getAbsolutePath());
        }
    }

    public void sendToServer(Machine m) {
        try {
            String url = config.getString("heartbeat.protocol") + "://" +
                    config.getString("heartbeat.server") + ":" + config.getString("heartbeat.port") +
                    "/" + config.getString("heartbeat.path") + "/create";
            restTemplate.postForLocation(url, m);
            // now post it to the server...
            //} catch (JsonProcessingException ex) {
            //    log.error("There was a problem converting the machine to JSON... " + ex.getMessage());
        } catch (RestClientException ex) {
            log.error("There was a problem sending the data to the server: " + ex.getMessage());
        }
    }

    /**
     * Used only to update time of last check in the database
     *
     * @param uuid
     */
    public void sendToServer(String uuid) {
        String url = config.getString("heartbeat.protocol") + "://" +
                config.getString("heartbeat.server") + ":" + config.getString("heartbeat.port") +
                "/" + config.getString("heartbeat.path") + "/update/" + uuid;
        try {
            restTemplate.put(url, null);
        } catch (RestClientException ex) {
            log.error("There was a problem sending the data to the server: " + ex.getMessage());
        }
    }

    /**
     * Get the computer name
     *
     * @return string The computer name
     */
    private String getCompName() {
        String computername;
        try {
            computername = InetAddress.getLocalHost().getHostName();
            computername = computername.replaceFirst("\\.(.+)", "");
        } catch (UnknownHostException e) {
            computername = "localhost";
        }
        return computername;
    }

    /**
     * Gets the machines SerialNumber using the WMI for windows, system_profiler
     * for Mac and dmidecode for Linux
     */
    private String getSerialNumber() {
        String serialNumber = null;
        try {
            Properties p = HeartbeatPropertiesLoader.getProperties(config);
            if (p != null) {
                if (p.getProperty("serialnumber") != null) return p.getProperty("serialnumber");
            }
        } catch (IOException e) {
            log.debug(e.getMessage());
        }

        if (System.getProperty("os.name").startsWith("Win")) {
            EnumVariant s = queryWMI("SELECT * FROM Win32_SystemEnclosure");
            Dispatch item = null;
            while (s.hasMoreElements()) {
                item = s.nextElement().toDispatch();
                serialNumber = Dispatch.call(item, "SerialNumber").toString();
            }
        } else if (System.getProperty("os.name").startsWith("Mac")) {
            String s = null;
            try {
                ByteArrayOutputStream stdout = new ByteArrayOutputStream();
                PumpStreamHandler psh = new PumpStreamHandler(stdout);
                String cmd = "system_profiler SPHardwareDataType";
                CommandLine commandLine = CommandLine.parse(cmd);
                DefaultExecutor executor = new DefaultExecutor();
                executor.setStreamHandler(psh);
                int returnval = executor.execute(commandLine);
                Pattern pattern = Pattern.compile("Serial Number \\(system\\): (\\w+)");
                Matcher mat = pattern.matcher(stdout.toString());
                if (mat.find()) {
                    s = mat.group(1);
                }
            } catch (ExecuteException e) {
                s = "none";
            } catch (IOException e) {
                s = "none";
            }
            serialNumber = s;

        } else if (System.getProperty("os.name").startsWith("Linux")) {
            String s = null;
            try {
                ByteArrayOutputStream stdout = new ByteArrayOutputStream();
                PumpStreamHandler psh = new PumpStreamHandler(stdout);
                String cmd = "dmidecode";
                CommandLine commandLine = CommandLine.parse(cmd);
                commandLine.addArgument("-s");
                commandLine.addArgument("system-serial-number");
                DefaultExecutor executor = new DefaultExecutor();
                executor.setStreamHandler(psh);
                int returnval = executor.execute(commandLine);
                s = stdout.toString().replaceAll("\\n", "");
            } catch (ExecuteException e) {
                s = "none";
            } catch (IOException e) {
                s = "none";
            }
            serialNumber = s;

        }
        if(serialNumber.equals(null) || serialNumber.equals(""))
            serialNumber = "none";
        return serialNumber;
    }

    /**
     * Returns a String representation of the computer Manufacturer
     *
     * @return String - Computer Manufacturer
     */
    private String getManufacturer() {
        String m = null;
        if (System.getProperty("os.name").startsWith("Mac")) {
            m = "Apple Inc.";
        } else if (System.getProperty("os.name").startsWith("Linux")) {
            try {
                ByteArrayOutputStream stdout = new ByteArrayOutputStream();
                PumpStreamHandler psh = new PumpStreamHandler(stdout);
                String cmd = "dmidecode";
                CommandLine commandLine = CommandLine.parse(cmd);
                commandLine.addArgument("-s");
                commandLine.addArgument("system-manufacturer");
                DefaultExecutor executor = new DefaultExecutor();
                executor.setStreamHandler(psh);
                executor.execute(commandLine);
                m = stdout.toString().replaceAll("\\n", "");
            } catch (ExecuteException e) {
                m = "";
            } catch (IOException e) {
                m = "";
            }
        } else if (System.getProperty("os.name").startsWith("Win")) {
            String q = "SELECT * FROM Win32_ComputerSystem";
            EnumVariant s = queryWMI(q);
            Dispatch item = null;
            item = s.nextElement().toDispatch();
            m = Dispatch.call(item, "Manufacturer").toString();
            m = m.replaceAll("\\'", "");
        } else {
            m = "Operating System " + System.getProperty("os.name") + " is not supported";
        }

        return m;
    }

    /**
     * Returns a string representation of the Computer Model
     *
     * @return String - Computer Model
     */
    private String getModel() {
        String m = null;
        if (System.getProperty("os.name").startsWith("Linux")) {
            try {
                ByteArrayOutputStream stdout = new ByteArrayOutputStream();
                PumpStreamHandler psh = new PumpStreamHandler(stdout);
                String cmd = "dmidecode";
                CommandLine commandLine = CommandLine.parse(cmd);
                commandLine.addArgument("-s");
                commandLine.addArgument("system-product-name");
                DefaultExecutor executor = new DefaultExecutor();
                executor.setStreamHandler(psh);
                executor.execute(commandLine);
                m = stdout.toString().replaceAll("\\n", "");
            } catch (ExecuteException e) {
                m = "";
            } catch (IOException e) {
                m = "";
            }
        } else if (System.getProperty("os.name").startsWith("Mac")) {

            try {
                ByteArrayOutputStream stdout = new ByteArrayOutputStream();
                PumpStreamHandler psh = new PumpStreamHandler(stdout);
                String cmd = "system_profiler SPHardwareDataType";
                CommandLine commandLine = CommandLine.parse(cmd);
                DefaultExecutor executor = new DefaultExecutor();
                executor.setStreamHandler(psh);
                executor.execute(commandLine);

                Pattern pattern = Pattern.compile("Model Identifier: (.+)\\n");
                Matcher mat = pattern.matcher(stdout.toString());
                if (mat.find()) {
                    m = mat.group(1);
                }
            } catch (ExecuteException e) {
                m = "";
            } catch (IOException e) {
                m = "";
            }
        } else if (System.getProperty("os.name").startsWith("Windows")) {
            String q = "SELECT * FROM Win32_ComputerSystem";
            EnumVariant s = queryWMI(q);
            Dispatch item = null;
            item = s.nextElement().toDispatch();
            m = Dispatch.call(item, "Model").toString();
            m = m.replaceAll("\\'", "");
        }
        return m;
    }

    //}
    private String getMacAddresses() {
        ArrayList<String> addresses = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface in = interfaces.nextElement();
                if (in.getDisplayName().contains("Loopback")) {
                    continue;
                }
                byte[] macaddresses = in.getHardwareAddress();
                if (macaddresses != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < macaddresses.length; i++) {
                        if (i < macaddresses.length - 1) {
                            sb.append(String.format("%1$02X:", macaddresses[i]));
                        } else {
                            sb.append(String.format("%1$02X", macaddresses[i]));
                        }
                    }
                    addresses.add(sb.toString());
                }
            }
            return StringUtils.collectionToCommaDelimitedString(addresses);
        } catch (java.net.SocketException ex) {
            log.info("Unable to get NetworkInterface: " + ex.getMessage());
            return "";
        }
    }

    /**
     * Return a generated uuid unless one has already been created in the
     * properties file
     *
     * @return
     */
    private String getUuid() {
        try {
            Properties p = HeartbeatPropertiesLoader.getProperties(config);
            if (p == null) {
                // no properties yet, let's create a uuid
                return UUID.randomUUID().toString();
            } else {
                if (p.getProperty("uuid") != null) {
                    return p.getProperty("uuid");
                } else {
                    return UUID.randomUUID().toString();
                }
            }
        } catch (IOException ex) {
            return UUID.randomUUID().toString();
        }
    }

    /**
     * Try to find out if there is a currently logged in user
     *
     * @return username as a String
     */
    private String getUser() {
        String user = null;
        if (System.getProperty("os.name").startsWith("Win")) {
            String q = "SELECT * FROM Win32_ComputerSystem";
            EnumVariant s = queryWMI(q);
            Dispatch item = null;
            item = s.nextElement().toDispatch();
            user = Dispatch.call(item, "UserName").toString();
            user = user.replaceAll("\\'", "");
        } else if (System.getProperty("os.name").startsWith("Mac") || System.getProperty("os.name").startsWith("Linux")) {
            CommandLine commandLine;
            try {
                ByteArrayOutputStream stdout = new ByteArrayOutputStream();
                PumpStreamHandler psh = new PumpStreamHandler(stdout);
                String cmd = "who";
                commandLine = CommandLine.parse(cmd);
                commandLine.addArgument("-q");
                DefaultExecutor executor = new DefaultExecutor();
                executor.setStreamHandler(psh);
                executor.execute(commandLine);
                Pattern pattern = Pattern.compile("^(.+)\\n");
                Matcher mat = pattern.matcher(stdout.toString());
                if (mat.find()) {
                    String[] u = mat.group(1).split("\\s");
                    if (u.length > 1) {
                        if (u[0].equals(u[1])) {
                            user = u[0];
                        } else {
                            for (int i = 0; i < u.length; i++) {
                                if (u[i].equals("sysadmin")) {
                                    continue;
                                } else {
                                    user = u[i];
                                }
                            }
                        }
                    } else {
                        user = mat.group(1);
                    }

                } else {
                    user = "None";
                }
            } catch (ExecuteException e) {
                user = "None";
            } catch (IOException e) {
                user = "None";
            }
        } else {
            user = "None";
        }
        return user;
    }

    /**
     * Connect to the WMI and return an ActiveXComponent to run query on
     *
     * @return
     */
    private EnumVariant queryWMI(String query) {
        String host = "localhost";
        String connectStr = String.format("winmgmts:\\\\%s\\root\\CIMV2", host);
        ActiveXComponent axWMI = new ActiveXComponent(connectStr);
        Variant vCollection = axWMI.invoke("ExecQuery", new Variant(query));
        EnumVariant enumVariant = new EnumVariant(vCollection.toDispatch());
        return enumVariant;
    }
}
