package main;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import static java.lang.management.ManagementFactory.getPlatformMBeanServer;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author tbaum
 * @since 17.03.2014
 */
class FdMonitor {

    void start() throws MalformedObjectNameException {
        final ObjectName os = ObjectName.getInstance("java.lang:type=OperatingSystem");
        newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override public void run() {
                try {
                    Object result = getPlatformMBeanServer().getAttribute(os, "OpenFileDescriptorCount");
                    System.err.println("open files: " + result);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 5, SECONDS);
    }

}
