import Toybox.Lang;
import Toybox.WatchUi;

import Toybox.Background;
import Toybox.System;
import Toybox.Sensor;

class BgGetMessageDelegate extends WatchUi.BehaviorDelegate {

    function initialize() {
        BehaviorDelegate.initialize();
    }

    function onMenu() as Boolean {
        WatchUi.pushView(new Rez.Menus.MainMenu(), new BgGetMessageMenuDelegate(), WatchUi.SLIDE_UP);
        return true;
    }

}

(:background)
public class BackgroundServiceDelegate extends System.ServiceDelegate {
    // When a scheduled background event triggers, make a request to
    // a service and handle the response with a callback function
    // within this delegate.

    var max_IBI;
    var periodSetting;

    function initialize(){
        System.ServiceDelegate.initialize();
        System.println("call initialize()");
        max_IBI = 500;
        periodSetting = 3; // 1 ~ 4s
    }

    function onTemporalEvent(){
        // A callback method that is triggered in the background when time-based events occur.
        System.println("call onTemporalEvent()");

        var maxSampleRate = Sensor.getMaxSampleRate();
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
        var HRV_samples = sensorData.heartRateData;
        var IBI_samples = HRV_samples.heartBeatIntervals;

        System.println("=========================");

        if(HRV_samples != null){

            //for debugging
            System.println("IBI_samples: " + IBI_samples);
            
            for(var i = 0; i < IBI_samples.size(); i += 1){
                if(IBI_samples[i] > max_IBI){
                    Background.requestApplicationWake("Take a breath");
                    saveBackgroundData(1);
                }
        }

        } else {
            System.println("    *** no HeartRate data! ***");
        }
    }

    function saveBackgroundData(currentData){
        var backgroundData;
        var app = Application.getApp();

        backgroundData = {
            app.BACKGROUND_REPONSE_CODE => currentData
        };
        Background.exit(backgroundData);
    }

    public function disableSensorDataListener() as Void {
        System.println("call disableSensorDataListener()");
        Sensor.unregisterSensorDataListener();
    }

}



