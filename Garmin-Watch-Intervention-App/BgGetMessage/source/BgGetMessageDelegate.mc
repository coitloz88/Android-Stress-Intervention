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

    var MIN_HRV;
    var periodSetting;
    var IBI_samples as Lang.Array<Lang.Number>;
    var timeCount;

    function initialize(){
        System.ServiceDelegate.initialize();
        // System.println("call initialize()");
        MIN_HRV = 20; //TODO: need to set
        periodSetting = 4; // 1 ~ 4s
        IBI_samples = [];
        timeCount = 0;
    }

    function onTemporalEvent(){
        // A callback method that is triggered in the background when time-based events occur.

        var time = System.getClockTime();
        System.println(Lang.format("onTemporalEvent: $1$:$2$:$3$", [time.hour, time.min, time.sec]));

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
        IBI_samples.addAll(rawHeartRateData.heartBeatIntervals);

        timeCount += periodSetting;

        // if(timeCount % (periodSetting * 3) == 0 || timeCount >= (30 - periodSetting)){
        if(timeCount >= (30 - periodSetting)){
            //for debugging
            System.println("IBI_samples: " + IBI_samples);

            var HRVdata = IBItoHRV(IBI_samples);
            System.println("HRVdata: " + HRVdata);

            IBI_samples = []; //reset

            if(HRVdata > 0 && HRVdata <= MIN_HRV){
                Background.requestApplicationWake("stress!");
                // saveBackgroundData(1);
                Background.exit(true);
             }
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



