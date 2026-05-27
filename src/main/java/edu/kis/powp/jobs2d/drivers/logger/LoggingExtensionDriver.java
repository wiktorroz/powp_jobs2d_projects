package edu.kis.powp.jobs2d.drivers.logger;

import edu.kis.powp.jobs2d.drivers.DecoratorDriver;
import edu.kis.powp.jobs2d.drivers.visitor.DriverVisitor;
import edu.kis.powp.jobs2d.drivers.visitor.VisitableDriver;

import java.util.logging.Logger;

/**
 * Decorator driver that logs all operations and delegates to an inner driver.
 * Acts as an extension: it can be toggled independently of the selected driver.
 */
public class LoggingExtensionDriver implements DecoratorDriver {

    private final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private VisitableDriver target;
    private int operationCount = 0;

    public LoggingExtensionDriver(VisitableDriver target) {
        this.target = target;
    }

    public synchronized void setTarget(VisitableDriver target) {
        this.target = target;
    }

    public synchronized VisitableDriver getTarget() {
        return target;
    }

    @Override
    public synchronized void setPosition(int x, int y) {
        logger.info("Position set to x: " + x + ", y: " + y + " [" + operationCount + "]");
        target.setPosition(x, y);
    }

    @Override
    public synchronized void operateTo(int x, int y) {
        operationCount++;
        logger.info("Operate to x: " + x + ", y: " + y + " [" + operationCount + "]");
        target.operateTo(x, y);
    }

    @Override
    public String toString() {
        return "Tracking Logger -> " + target;
    }

    @Override
    public void accept(DriverVisitor visitor) {
        visitor.visit(this);
    }
}
