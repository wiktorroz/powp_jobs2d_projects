package edu.kis.powp.jobs2d.features;

import edu.kis.powp.appbase.Application;
import edu.kis.powp.jobs2d.drivers.RecordingDriver;
import edu.kis.powp.jobs2d.drivers.logger.LoggingExtensionDriver;
import edu.kis.powp.jobs2d.drivers.visitor.VisitableDriver;
import edu.kis.powp.jobs2d.events.SelectClearRecordingOptionListener;
import edu.kis.powp.jobs2d.events.SelectToggleRecordingOptionListener;

import java.awt.event.ActionEvent;

/**
 * Feature that provides optional extensions (add-ons) working independently
 * of the selected driver. Extensions are toggled via checkboxes in the
 * Extensions menu.
 */
public class ExtensionsFeature implements IFeature {

    private static Application app;
    private static LoggingExtensionDriver loggingDriver;

    @Override
    public void setup(Application application) {
        app = application;
        app.addComponentMenu(ExtensionsFeature.class, "Extensions");
    }

    @Override
    public String getName() {
        return "Extensions";
    }

    /**
     * Add Tracking Logger extension checkbox to the Extensions menu.
     * When enabled, wraps the current driver with a logging decorator.
     */
    public static void setupTrackingLoggerExtension() {
        app.addComponentMenuElementWithCheckBox(
                ExtensionsFeature.class,
                "Tracking Logger",
                (ActionEvent e) -> {
                    javax.swing.AbstractButton btn = (javax.swing.AbstractButton) e.getSource();
                    if (btn.isSelected()) {
                        enableLogging();
                    } else {
                        disableLogging();
                    }
                    DriverFeature.updateDriverInfo();
                },
                false
        );
    }

    /**
     * Add Recording extension checkbox and clear button to the Extensions menu.
     * Must be called after RecordingFeature.setup().
     */
    public static void setupRecordingExtension() {
        RecordingDriver rec = RecordingFeature.getRecordingDriver();
        boolean initial = rec.isRecordingEnabled();

        app.addComponentMenuElementWithCheckBox(
                ExtensionsFeature.class,
                "Recording",
                new SelectToggleRecordingOptionListener(rec),
                initial
        );

        app.addComponentMenuElement(
                ExtensionsFeature.class,
                "Clear recording",
                new SelectClearRecordingOptionListener()
        );
    }

    private static void enableLogging() {
        if (loggingDriver != null) {
            return;
        }
        VisitableDriver current = DriverFeature.getDriverManager().getCurrentDriver();
        loggingDriver = new LoggingExtensionDriver(current);
        DriverFeature.getDriverManager().setCurrentDriver(loggingDriver);
    }

    private static void disableLogging() {
        if (loggingDriver == null) {
            return;
        }
        VisitableDriver inner = loggingDriver.getTarget();
        if (DriverFeature.getDriverManager().getCurrentDriver() == loggingDriver) {
            DriverFeature.getDriverManager().setCurrentDriver(inner);
        }
        loggingDriver = null;
    }
}
