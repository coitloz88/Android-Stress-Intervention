import Toybox.Graphics;
import Toybox.WatchUi;
import Toybox.Sensor;
import Toybox.Timer;

class HeartRateTestView extends WatchUi.View {

    var userHeartRate;
/*
    function timerCallback() {
        var sensorInfo = Sensor.getInfo();
        if (sensorInfo has :heartRate && sensorInfo.heartRate != null) {
            userHeartRate = sensorInfo.heartRate;
        } else {userHeartRate = 0;}
    }
*/
    function initialize() {
        View.initialize();
        Sensor.setEnabledSensors([Sensor.SENSOR_HEARTRATE]);
        Sensor.enableSensorEvents(method(:onSensor));
    }

    function onSensor(sensorInfo){
        if (sensorInfo has :heartRate && sensorInfo.heartRate != null) {
            userHeartRate = sensorInfo.heartRate;
            System.println("Current Heart Rate: " + userHeartRate);
        } else {
            userHeartRate = 0;
            System.println("cannot access");
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
        // Call the parent onUpdate function to redraw the layout
        //onSensor(Sensor.getInfo());
        System.println(userHeartRate.toString());
        dc.drawText(dc.getWidth() / 2, 100, Graphics.FONT_SMALL, "Test message", Graphics.TEXT_JUSTIFY_CENTER);
        //dc.drawText(dc.getWidth() / 2, 160,  Graphics.FONT_TINY,  strTmp, Graphics.TEXT_JUSTIFY_CENTER);
        View.onUpdate(dc);
    }

    // Called when this View is removed from the screen. Save the
    // state of this View here. This includes freeing resources from
    // memory.
    function onHide() as Void {
    }

}
