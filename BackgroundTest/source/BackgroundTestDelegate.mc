import Toybox.Lang;
import Toybox.WatchUi;

import Toybox.Background;
import Toybox.System;
import Toybox.Communications;
import Toybox.Sensor;

class BackgroundTestDelegate extends WatchUi.BehaviorDelegate {

    function initialize() {
        BehaviorDelegate.initialize();
    }

    function onMenu() as Boolean {
        WatchUi.pushView(new Rez.Menus.MainMenu(), new BackgroundTestMenuDelegate(), WatchUi.SLIDE_UP);
        return true;
    }
}

(:background)
class CommListener extends Communications.ConnectionListener {
    function initialize() {
        Communications.ConnectionListener.initialize();
    }

    function onComplete() {
        System.println("Transmit Complete");
    }

    function onError() {
        System.println("Transmit Failed");
    }

}

(:background)
public class BackgroundServiceDelegate extends System.ServiceDelegate {
    // When a scheduled background event triggers, make a request to
    // a service and handle the response with a callback function
    // within this delegate.

    // var timerCount;
    // var myTimer;

    // var maxTime;
    // var timerUnit;

    private var _samplesX = null;
    private var _samplesY = null;
    private var _samplesZ = null;
    private var timeCount;
    private var periodSetting;
    private var listener;

    function initialize(){
        System.ServiceDelegate.initialize();
        System.println("call initialize()");
        periodSetting = 3;
        timeCount = 0 + periodSetting;
    }

    function onTemporalEvent() {
        // A callback method that is triggered in the background when time-based events occur.
        System.println("call onTemporalEvent()");

        var maxSampleRate = Sensor.getMaxSampleRate();
        listener = new CommListener();

        // if(System.getDeviceSettings().phoneConnected){
            // initialize accelerometer to request the maximum amount of data possible
            // var options = {:period => periodSetting, :sampleRate => maxSampleRate, :enableAccelerometer => true};
            var options = {:period => periodSetting, :accelerometer => {:enabled => true, :sampleRate => maxSampleRate}, :heartBeatIntervals => { :enabled=> true}};
            try {
                // Sensor.registerSensorDataListener(method(:accelHistoryCallback), options);
                Sensor.registerSensorDataListener(method(:HRHistoryCallback), options);
            }
            catch(e) {
                System.println(" *** " + e.getErrorMessage());
                disableSensorDataListener();
            }
            // Background.exit(null);
        // } else {
        //     Background.exit(null);
        // }
    }

    public function accelHistoryCallback(sensorData as SensorData) as Void {
        _samplesX = sensorData.accelerometerData.x;
        _samplesY = sensorData.accelerometerData.y;
        _samplesZ = sensorData.accelerometerData.z;

        // System.println("Raw samples, X axis: " + _samplesX);
        // System.println("Raw samples, Y axis: " + _samplesY);
        // System.println("Raw samples, Z axis: " + _samplesZ);
        System.println("=========================");

        if(sensorData.accelerometerData != null){
            var dicAccel = {};

            dicAccel.put(timeCount + "X", _samplesX);
            dicAccel.put(timeCount + "Y", _samplesY);
            dicAccel.put(timeCount + "Z", _samplesZ);

            timeCount += periodSetting;

            System.println(dicAccel);

            if(System.getDeviceSettings().phoneConnected){
                Communications.transmit(dicAccel, "null", listener);
            } else {
                System.println("    *** fail to send ***");
            }
        }
        
    }

    public function HRHistoryCallback(sensorData as SensorData) as Void {
        var HR_samples = sensorData.heartRateData;

        // System.println("Raw samples, X axis: " + _samplesX);
        // System.println("Raw samples, Y axis: " + _samplesY);
        // System.println("Raw samples, Z axis: " + _samplesZ);
        System.println("=========================");

        if(sensorData.heartRateData != null){
            var dicHR = {};

            dicHR.put(timeCount, HR_samples);

            timeCount += periodSetting;

            System.println("dicHR: " + dicHR);

            if(System.getDeviceSettings().phoneConnected){
                Communications.transmit(dicHR, "null", listener);
            } else {
                System.println("    *** fail to send ***");
            }
        }
        
    }

    public function disableSensorDataListener() as Void {
        Sensor.unregisterSensorDataListener();
    }

}