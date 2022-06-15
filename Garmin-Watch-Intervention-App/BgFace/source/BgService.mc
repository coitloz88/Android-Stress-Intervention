import Toybox.Lang;
import Toybox.WatchUi;

import Toybox.Background;
import Toybox.System;
import Toybox.SensorHistory;
// import Toybox.ActivityMonitor;

(:background)
public class BackgroundServiceDelegate extends System.ServiceDelegate {
    // When a scheduled background event triggers, make a request to
    // a service and handle the response with a callback function
    // within this delegate.
    var MIN_HRV = 20;
    var timeUnit = 15; // 아마 가장 최근 5개 데이터 수집하는 걸로 수정해야 할듯!

    function initialize(){
        System.ServiceDelegate.initialize();
        // System.println("call initialize()");
    }

    function onTemporalEvent() {
        // A callback method that is triggered in the background when time-based events occur.
        // System.println("call onTemporalEvent()");

        // for debugging log
        var time = System.getClockTime();
        System.println(Lang.format("BgEvent: $1$:$2$:$3$", [time.hour, time.min, time.sec]));
    
        // logics for collecting heart rate history
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

        // logics for collecting stress history
        // var stressDatas = getStressData();
        // if(stressDatas != null){
        //     System.println("stress data: " + stressDatas);
        // } else {
        //     System.println("No Stress Data!");
        // }
    }

    //functions for heart rate data
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

    // functions for stress
    // function getStressData() {
    //     var stressIter = getStressIterator();
    //     var sample;
    //     var stressDatas = [];

    //     if(stressIter != null){
    //         sample = stressIter.next();
    //     } else { sample = null; }
        
    //     while (sample != null) {
    //         stressDatas.add(sample.data);
    //         sample = stressIter.next();
    //     }
    //     return stressDatas;
    // }

    // function getStressIterator() {
    //     // Check device for SensorHistory compatibility
    //     if ((Toybox has :SensorHistory) && (Toybox.SensorHistory has :getStressHistory)) {
    //         var options = {:period => timeUnit, :order => 0};
    //         return Toybox.SensorHistory.getHeartRateHistory(options);
    //     }
    //     return null;
    // }

}