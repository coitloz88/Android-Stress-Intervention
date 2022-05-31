import Toybox.Lang;
import Toybox.WatchUi;

import Toybox.Background;
import Toybox.System;
import Toybox.SensorHistory;

(:background)
public class BackgroundServiceDelegate extends System.ServiceDelegate {
    // When a scheduled background event triggers, make a request to
    // a service and handle the response with a callback function
    // within this delegate.
    var MIN_HRV = 40;
    var timeUnit = 15;

    function initialize(){
        System.ServiceDelegate.initialize();
        System.println("call initialize()");

    }

    function onTemporalEvent() {
        // A callback method that is triggered in the background when time-based events occur.
        System.println("call onTemporalEvent()");

        // for debugging log
        var time = System.getClockTime();
        System.println(Lang.format("timerCallback: $1$:$2$:$3$", [time.hour, time.min, time.sec]));
    
        var hrDatas = getHeartRateData();
        System.println("HeartRate History data: " + hrDatas);
        if(hrDatas != null){
            var HRVdata = IBItoHRV(BPMtoIBI(hrDatas));
            System.println("HRV data: " + HRVdata);

            if(HRVdata > 0 && HRVdata <= MIN_HRV){
                Background.exit(true);
            }

        } else {
            System.println("No HeartRate Data!");
        }
    }

    function getHeartRateData() {
        var heartRateSensorIter = getHeartRateIterator();
        var sample;
        var hrDatas = [];

        if(heartRateSensorIter != null){
            sample = heartRateSensorIter.next();
        } else { sample = null; }
        
        while (sample != null) {
            hrDatas.add(sample.data);
            sample = heartRateSensorIter.next();
        }
        return hrDatas;
    }

    function getHeartRateIterator() {
        // Check device for SensorHistory compatibility
        if ((Toybox has :SensorHistory) && (Toybox.SensorHistory has :getHeartRateHistory)) {
            var options = {:period => timeUnit, :order => 0};
            return Toybox.SensorHistory.getHeartRateHistory(options);
        }
        return null;
    }

    function BPMtoIBI(BPM_samples){
        var IBI_samples = [];

        for(var i = 0; i < BPM_samples.size(); i += 1){
            if(BPM_samples[i] != null){
                IBI_samples.add(60 * 1000 / BPM_samples[i]);
            }
        }
        System.println("IBI data: " + IBI_samples);

        return IBI_samples;
    }


    function IBItoHRV(IBI_samples){
        if(IBI_samples == null || IBI_samples.size() == 0){
            return null;
        }

        var HRVdata = 0;

        for(var i = 0; i < IBI_samples.size() - 1; i += 1){
            HRVdata += Math.pow(IBI_samples[i + 1] - IBI_samples[i], 2);
        }
        HRVdata /= (IBI_samples.size() - 1);

        return Math.sqrt(HRVdata);
    }


}