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

    var timeCount;
    var periodSetting;
    var IBI_samples as Lang.Array<Lang.Number>;
    var listener;

    function initialize(){
        System.ServiceDelegate.initialize();
        periodSetting = 4; //1~4s (max 4)
        timeCount = 0;
        IBI_samples = [];
    }

    function onTemporalEvent() {
        // A callback method that is triggered in the background when time-based events occur.
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

    public function HRHistoryCallback(sensorData as SensorData) as Void {
        var rawIBIData = sensorData.heartRateData;
        var send_dict = {};

        IBI_samples.addAll(rawIBIData.heartBeatIntervals);

        timeCount += periodSetting;

        if(timeCount >= (30 - periodSetting)){            
            var time = System.getClockTime();
            System.print(Lang.format("$1$:$2$:$3$", [time.hour, time.min, time.sec]));
            System.println(IBI_samples);
            send_dict.put(timeCount, IBI_samples);

            if(System.getDeviceSettings().phoneConnected){
                Communications.transmit(send_dict, null, listener);
            } else {
                System.println("    *** fail to send(not connected) ***");
            }
        } else if(timeCount == periodSetting * 4){        
            var time = System.getClockTime();
            System.print(Lang.format("$1$:$2$:$3$", [time.hour, time.min, time.sec]));
            System.println(IBI_samples);
            send_dict.put(timeCount, IBI_samples);

            if(System.getDeviceSettings().phoneConnected){
                Communications.transmit(send_dict, null, listener);
            } else {
                System.println("    *** fail to send(not connected) ***");
            }
            send_dict = {};
            IBI_samples = [];
        }
        
    }

    public function disableSensorDataListener() as Void {
        System.println("call disableSensorDataListener()");
        Sensor.unregisterSensorDataListener();
    }

}
