package edu.uc.labs.heartbeat;

import org.tanukisoftware.wrapper.WrapperListener;
import org.tanukisoftware.wrapper.WrapperManager;

public class AppServiceWrapper implements WrapperListener {
    private App m_app;
    private String[] args;
    private int exitCode;

    private AppServiceWrapper() {
    }

    public Integer start(String[] args) {
        this.args = args;
        m_app = new App(args);
        return null;
    }

    public int stop(int exitCode) {
        this.exitCode = exitCode;
        m_app.stop();
        return exitCode;
    }

    /**
     * Called whenever the native Wrapper code traps a system control signal
     * against the Java process.  It is up to the callback to take any actions
     * necessary.  Possible values are: WrapperManager.WRAPPER_CTRL_C_EVENT,
     * WRAPPER_CTRL_CLOSE_EVENT, WRAPPER_CTRL_LOGOFF_EVENT, or
     * WRAPPER_CTRL_SHUTDOWN_EVENT
     *
     * @param event The system control signal.
     */
    public void controlEvent(int event) {
        if (exitCode == 8) {
            // We want to restart the service
            start(args);
        } else if ((event == WrapperManager.WRAPPER_CTRL_LOGOFF_EVENT)
                && (WrapperManager.isLaunchedAsService())) {
            // Ignore
        } else {
            WrapperManager.stop(0);
            // Will not get here.
        }
    }

    public static void main(String[] args) {
        WrapperManager.start(new AppServiceWrapper(), args);
    }
}