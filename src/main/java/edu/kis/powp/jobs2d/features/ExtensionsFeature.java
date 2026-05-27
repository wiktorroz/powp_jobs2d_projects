package edu.kis.powp.jobs2d.features;

import edu.kis.powp.appbase.Application;
import edu.kis.powp.jobs2d.drivers.DriverChainUtils;
import edu.kis.powp.jobs2d.drivers.optional_drivers.RecordingDriver;
import edu.kis.powp.jobs2d.drivers.optional_drivers.LoggingExtensionDriver;
import edu.kis.powp.jobs2d.drivers.visitor.VisitableDriver;
import edu.kis.powp.jobs2d.events.SelectClearRecordingOptionListener;
import edu.kis.powp.jobs2d.events.SelectToggleRecordingOptionListener;

import java.awt.event.ActionEvent;

/**
 * Feature that provides optional extensions (add-ons) working independently
 * of the selected driver. Extensions are toggled via checkboxes in the
 * Extensions menu.
 *
 * Extensions form a decorator chain. Each extension can be enabled/disabled
 * independently — disabling removes it from the chain like a linked-list node
 * removal, connecting its predecessor directly to its successor.
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
     * When enabled, inserts LoggingExtensionDriver at the top of the chain.
     * When disabled, removes it from anywhere in the chain (linked-list removal).
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

    /**
     * Inserts LoggingExtensionDriver at the top of the current driver chain.
     */
    private static void enableLogging() {
        if (loggingDriver != null) {
            return;
        }
        VisitableDriver current = DriverFeature.getDriverManager().getCurrentDriver();
        loggingDriver = new LoggingExtensionDriver(current);
        DriverFeature.getDriverManager().setCurrentDriver(loggingDriver);
    }

    /**
     * Removes LoggingExtensionDriver from anywhere in the decorator chain,
     * connecting its predecessor directly to its successor (linked-list removal).
     * Works correctly even for nested chains like: Recording -> Logging -> actual.
     */
    private static void disableLogging() {
        if (loggingDriver == null) {
            return;
        }
        VisitableDriver current = DriverFeature.getDriverManager().getCurrentDriver();
        VisitableDriver newRoot = DriverChainUtils.removeFromChain(current, loggingDriver);
        if (newRoot != current) {
            DriverFeature.getDriverManager().setCurrentDriver(newRoot);
        }
        //If newRoot == current, logging was in the middle of the chain —
        //predecessor's target was already updated by removeFromChain.
        loggingDriver = null;
    }
}
