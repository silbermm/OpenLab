package edu.uc.labs.heartbeat;

import edu.uc.labs.heartbeat.config.AppConfig;
import edu.uc.labs.heartbeat.config.RabbitConfig;
import edu.uc.labs.heartbeat.domain.Machine;
import edu.uc.labs.heartbeat.service.HeartbeatService;
import org.apache.commons.cli.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The entry point for Heartbeat
 *
 * @author Matt Silbernagel
 */
public class App {

    public App(String[] args) {
        parse(args);

        if (cmd.hasOption("r")) {
            ctx = new AnnotationConfigApplicationContext(AppConfig.class, RabbitConfig.class);
        } else {
            ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        }
        ctx.registerShutdownHook();

        if (cmd.hasOption("u")) {
            HeartbeatService service = ctx.getBean(HeartbeatService.class);
            Machine m = service.getMachineInfo();
            service.writeMachineInfo(m);
            System.out.println(m.getUuid());
            System.exit(0);
        } else if (cmd.hasOption("a")) {
            HeartbeatService service = ctx.getBean(HeartbeatService.class);
            Machine m = service.getMachineInfo();
            service.writeMachineInfo(m);
            System.out.println(m);
            System.exit(0);
        }
    }

    public int stop() {
        ctx.stop();
        return 0;
    }

    /**
     * Parse the commandline options
     *
     * @param args
     */
    private void parse(String[] args) {
        CommandLineParser parser = new PosixParser();
        options.addOption(OptionBuilder.withArgName("h").withDescription(r.getString("cli.help")).withLongOpt("help").create("h"));
        options.addOption(OptionBuilder.withArgName("v").withDescription(r.getString("cli.version")).withLongOpt("version").create("v"));
        options.addOption(OptionBuilder.withArgName("d").withDescription(r.getString("cli.debug")).withLongOpt("debug").create("d"));
        options.addOption(OptionBuilder.withArgName("c").hasArg().withDescription(r.getString("cli.config")).withLongOpt("configfile").create("c"));
        options.addOption(OptionBuilder.withArgName("u").withDescription(r.getString("cli.uuid")).withLongOpt("getUUID").create("u"));
        options.addOption(OptionBuilder.withArgName("a").withDescription(r.getString("cli.all")).withLongOpt("all").create("a"));
        options.addOption(OptionBuilder.withArgName("r").withDescription(r.getString("cli.rabbit")).withLongOpt("rabbit").create("r"));

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
            printHelp();
        }
        if (cmd.hasOption("d")) {
            Logger.getRootLogger().setLevel(Level.DEBUG);
        }

        if (cmd.hasOption("h")) {
            printHelp();
        }
        if (cmd.hasOption("v")) {
            System.out.println(r.getString("app.title") + " - " + r.getString("app.version"));
            System.exit(0);
        }
        if (cmd.hasOption("c")) {
            File newfile = new File(cmd.getOptionValue("c"));
            log.debug("setting config.file property to " + newfile.getAbsolutePath());
            System.setProperty("config.file", newfile.getAbsolutePath());
        }
    }

    /**
     * Print the help message
     */
    private void printHelp() {
        HelpFormatter help = new HelpFormatter();
        help.printHelp(r.getString("app.title").toLowerCase(), options);
        System.exit(1);
    }

    public static void main(String[] args) {
        App a = new App(args);
    }

    private AnnotationConfigApplicationContext ctx;
    private ResourceBundle r = ResourceBundle.getBundle("locale/CliBundle", Locale.getDefault());
    private static final Logger log = Logger.getLogger(App.class);
    private Options options = new Options();
    private CommandLine cmd = null;
}
