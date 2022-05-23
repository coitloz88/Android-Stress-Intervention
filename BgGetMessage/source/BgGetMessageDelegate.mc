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

    var MAX_HRV;
    var periodSetting;

    function initialize(){
        System.ServiceDelegate.initialize();
        System.println("call initialize()");
        MAX_HRV = 4; //TODO: need to set
        periodSetting = 4; // 1 ~ 4s
    }

    function onTemporalEvent(){
        // A callback method that is triggered in the background when time-based events occur.
        System.println("call onTemporalEvent()");

        var maxSampleRate = Sensor.getMaxSampleRate();
        var options = {:period => periodSetting, :accelerometer => {:enabled => true, :sampleRate => maxSampleRate}, :heartBeatIntervals => {:enabled=> true}};
        try {
            Sensor.registerSensorDataListener(method(:HRVHistoryCallback), options);
        }
        catch(e) {
            System.println(" *** " + e.getErrorMessage());
            disableSensorDataListener(); 
        }    
    }

    public function HRVHistoryCallback(sensorData as SensorData) as Void {
        var rawHeartRateData = sensorData.heartRateData;
        var IBI_samples = rawHeartRateData.heartBeatIntervals;

        System.println("=========================");

        if(rawHeartRateData != null){

            //for debugging
            System.println("IBI_samples: " + IBI_samples);
            
            // for(var i = 0; i < IBI_samples.size(); i += 1){
            //     if(IBI_samples[i] < MAX_HRV){
            //         Background.requestApplicationWake("Take a breath");
            //         saveBackgroundData(1);
            //     }
            // }

            var HRVdata = IBItoHRV(IBI_samples);

            System.println("HRVdata: " + HRVdata);

            if(HRVdata >= MAX_HRV){
                Background.requestApplicationWake("Tachycardia");
                saveBackgroundData(1);
            }

        } else {
            System.println("    *** no HeartRate data! ***");
        }
    }

    function IBItoHRV(IBI_samples){
        var HRVdata = 0;

        for(var i = 0; i < IBI_samples.size() - 1; i += 1){
            HRVdata += Math.pow(IBI_samples[i + 1] - IBI_samples[i], 2);
        }
        HRVdata /= (IBI_samples.size() - 1);

        return Math.sqrt(HRVdata);
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



