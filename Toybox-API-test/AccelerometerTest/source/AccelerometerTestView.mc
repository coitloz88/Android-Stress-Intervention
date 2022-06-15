import Toybox.Graphics;
import Toybox.WatchUi;
import Toybox.Sensor;
import Toybox.Timer;

class AccelerometerTestView extends WatchUi.View {

    var timeUnit;
    var timeCount;

    function initialize() {
        View.initialize();
        //Sensor.enableSensorEvents(method(:onSensor));
        timeUnit = 5000;
        timeCount = 0;       
    }

    function timerCallback(){
        timeCount += timeUnit / 1000;
        System.println("timeCount: " + timeCount + "s");

        var sensorInfo = Sensor.getInfo();

        if(sensorInfo has :accel && sensorInfo.accel != null){
            var accelArray = sensorInfo.accel;
            System.println("x: " + accelArray[0] + ", y: " + accelArray[1]);
        } else {
            System.println("There is no accelerometer data.");
        }
        WatchUi.requestUpdate();
    }

    // Load your resources here
    function onLayout(dc as Dc) as Void {
        setLayout(Rez.Layouts.MainLayout(dc));

        var myTimer = new Timer.Timer();
        myTimer.start(method(:timerCallback), timeUnit, true);
    }

    // Called when this View is brought to the foreground. Restore
    // the state of this View and prepare it to be shown. This includes
    // loading resources into memory.
    function onShow() as Void {
    }

    // Update the view
    function onUpdate(dc as Dc) as Void {
        // Call the parent onUpdate function to redraw the layout
        View.onUpdate(dc);
    }

    // Called when this View is removed from the screen. Save the
    // state of this View here. This includes freeing resources from
    // memory.
    function onHide() as Void {
    }

}
