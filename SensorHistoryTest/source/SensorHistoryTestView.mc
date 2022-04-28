import Toybox.Graphics;
import Toybox.WatchUi;

class SensorHistoryTestView extends WatchUi.View {
    
    private var _samplesX = null;
    private var _samplesY = null;
    private var _samplesZ = null;

    function initialize() {
        View.initialize();

        var maxSampleRate = Sensor.getMaxSampleRate();

         // initialize accelerometer to request the maximum amount of data possible
        var options = {:period => 1, :sampleRate => maxSampleRate, :enableAccelerometer => true};
        try {
            Sensor.registerSensorDataListener(method(:accelHistoryCallback), options);
        }
        catch(e) {
            System.println("*** " + e.getErrorMessage());
            disableAccel();
        }
    }

    // Load your resources here
    function onLayout(dc as Dc) as Void {
        setLayout(Rez.Layouts.MainLayout(dc));
    }

    // Called when this View is brought to the foreground. Restore
    // the state of this View and prepare it to be shown. This includes
    // loading resources into memory.
    function onShow() as Void {
    }

    // Update the view
    function onUpdate(dc as Dc) as Void {
        dc.setColor(Graphics.COLOR_TRANSPARENT, Graphics.COLOR_BLACK);
        dc.clear();
        dc.setColor(Graphics.COLOR_WHITE, Graphics.COLOR_TRANSPARENT);
        dc.drawText(dc.getWidth() / 2, 30, Graphics.FONT_SMALL, "Running...", Graphics.TEXT_JUSTIFY_CENTER);
    }

    // Called when this View is removed from the screen. Save the
    // state of this View here. This includes freeing resources from
    // memory.
    function onHide() as Void {
    }

    public function accelHistoryCallback(sensorData as SensorData) as Void {
        _samplesX = sensorData.accelerometerData.x;
        _samplesY = sensorData.accelerometerData.y;
        _samplesZ = sensorData.accelerometerData.z;

        System.println("Raw samples, X axis: " + _samplesX);
        System.println("Raw samples, Y axis: " + _samplesY);
        System.println("Raw samples, Z axis: " + _samplesZ);
    }

    public function disableAccel() as Void {
        Sensor.unregisterSensorDataListener();
    }

}