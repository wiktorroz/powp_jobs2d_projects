package edu.kis.powp.jobs2d.drivers.visitor;

import edu.kis.powp.jobs2d.drivers.BoundsDriver;
import edu.kis.powp.jobs2d.drivers.RealTimeDriver;
import edu.kis.powp.jobs2d.drivers.optional_drivers.RecordingDriver;
import edu.kis.powp.jobs2d.drivers.adapter.LineDriverAdapter;
import edu.kis.powp.jobs2d.drivers.optional_drivers.LoggingExtensionDriver;
import edu.kis.powp.jobs2d.drivers.optional_drivers.TrackingLoggerDriver;
import edu.kis.powp.jobs2d.drivers.packet_composite.CompositeDriver;
import edu.kis.powp.jobs2d.drivers.transformations.TransformingDriver;

public interface DriverVisitor {
    void visit(CompositeDriver driver);
    void visit(LineDriverAdapter adapter);
    void visit(TrackingLoggerDriver driver);
    //TODO: PRZENIEŚĆ TO DO ABSTRAKCYJNEJ KLASY
    void visit(LoggingExtensionDriver driver);
    void visit(RealTimeDriver driver);
    void visit(RecordingDriver driver);
    void visit(TransformingDriver driver);
    //TODO: DALEJ BEZ ZMIAN
    void visit(BoundsDriver driver);
}
