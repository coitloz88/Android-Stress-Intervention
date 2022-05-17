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

    private var _samplesX = null;
    private var _samplesY = null;
    private var _samplesZ = null;
    private var timeCount;
    private var periodSetting;
    private var listener;

    function initialize(){
        System.ServiceDelegate.initialize();
        System.println("call initialize()");
        periodSetting = 4; //1~4 (max 4)
        timeCount = 0 + periodSetting;
    }

    function onTemporalEvent() {
        // A callback method that is triggered in the background when time-based events occur.
        System.println("call onTemporalEvent()");

        var maxSampleRate = Sensor.getMaxSampleRate();
        listener = new CommListener();

        // initialize accelerometer & heart rate intervals to request the maximum amount of data possible    
        var options = {:period => periodSetting, :accelerometer => {:enabled => true, :sampleRate => maxSampleRate}, :heartBeatIntervals => {:enabled=> true}};
        try {
            Sensor.registerSensorDataListener(method(:HRHistoryCallback), options);
        }
        catch(e) {
            System.println(" *** " + e.getErrorMessage());
            disableSensorDataListener(); 
        }

    }

    public function accelHistoryCallback(sensorData as SensorData) as Void {
        _samplesX = sensorData.accelerometerData.x;
        _samplesY = sensorData.accelerometerData.y;
        _samplesZ = sensorData.accelerometerData.z;

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
                System.println("    *** fail to send(not connected) ***");
            }
            
        } else {
            System.println("    *** no accel data! ***");
        }
        
    }

    public function HRHistoryCallback(sensorData as SensorData) as Void {
        var HR_samples = sensorData.heartRateData;

        System.println("=========================");

        if(sensorData.heartRateData != null){
            var dicHR = {};

            dicHR.put(timeCount, HR_samples.heartBeatIntervals);

            timeCount += periodSetting;

            System.println("dicHR: " + dicHR);

            if(System.getDeviceSettings().phoneConnected){
                Communications.transmit(dicHR, "null", listener);
            } else {
                System.println("    *** fail to send(not connected) ***");
            }
        } else {
            System.println("    *** no HeartRate data! ***");
        }
        
    }

    public function SensorDataCallback(sensorData as SensorData) as Void {
        _samplesX = sensorData.accelerometerData.x;
        _samplesY = sensorData.accelerometerData.y;
        _samplesZ = sensorData.accelerometerData.z;

        var HR_samples = sensorData.heartRateData;

        if(sensorData.accelerometerData != null && sensorData.heartRateData != null){
            var dicSensorDatas = {};

            dicSensorDatas.put(timeCount + "X", _samplesX);
            dicSensorDatas.put(timeCount + "Y", _samplesY);
            dicSensorDatas.put(timeCount + "Z", _samplesZ);        
            dicSensorDatas.put(timeCount, HR_samples.heartBeatIntervals);

            timeCount += periodSetting;
            System.println("timeCount: " + timeCount);

            //for debugging
            System.println("sensor data:" + dicSensorDatas);

            if(System.getDeviceSettings().phoneConnected){
                Communications.transmit(dicSensorDatas, "null", listener);
            } else {
                System.println("    *** fail to send(not connected) ***");
            }


        } else {
            //for debugging
            System.println("No Sensor Data Object!");
        }

    }

    // public function testCallback(sensorData as SensorData) as Void {

    //     System.println("call testCallback()");

    //     var testDictionary = {};
    //     var array1 = [300, 400, 650, 600];
    //     var array2 = [100, 200, 300, 400];

    //     if(timeCount % 12 == 0){
    //         testDictionary.put(timeCount, array1);
    //     } else {
    //         testDictionary.put(timeCount, array2);
    //     }

    //     timeCount += periodSetting;

    //     System.println(testDictionary);

    //     if(System.getDeviceSettings().phoneConnected){
    //         Communications.transmit(testDictionary, "null", listener);
    //     } else {
    //         System.println("    *** fail to send(not connected) ***");
    //     }
    // }

    public function disableSensorDataListener() as Void {
        System.println("call disableSensorDataListener()");
        Sensor.unregisterSensorDataListener();
    }

}