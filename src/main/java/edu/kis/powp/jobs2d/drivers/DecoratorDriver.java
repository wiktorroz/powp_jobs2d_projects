package edu.kis.powp.jobs2d.drivers;

import edu.kis.powp.jobs2d.drivers.visitor.VisitableDriver;


public interface DecoratorDriver extends VisitableDriver {
    VisitableDriver getTarget();
    void setTarget(VisitableDriver target);
}
