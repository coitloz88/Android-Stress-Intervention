import Toybox.Graphics;
import Toybox.Lang;
import Toybox.System;
import Toybox.WatchUi;

import Toybox.SensorHistory;
import Toybox.Timer;

class BgFaceView extends WatchUi.WatchFace {

    var timerUnit = 15;

    function initialize() {
        WatchFace.initialize();
    }

    // Load your resources here
    function onLayout(dc as Dc) as Void {        
        System.println("call onLayout()");

        setLayout(Rez.Layouts.WatchFace(dc));
        var sensorTimer = new Timer.Timer();
        sensorTimer.start(method(:timerCallback), timerUnit * 1000, true);
    }

    // Called when this View is brought to the foreground. Restore
    // the state of this View and prepare it to be shown. This includes
    // loading resources into memory.
    function onShow() as Void {
    }

    // Update the view
    function onUpdate(dc as Dc) as Void {
        // Get and show the current time
        var clockTime = System.getClockTime();
        var timeString = Lang.format("$1$:$2$", [clockTime.hour, clockTime.min.format("%02d")]);
        var view = View.findDrawableById("TimeLabel") as Text;
        view.setText(timeString);
        
        // Call the parent onUpdate function to redraw the layout
        View.onUpdate(dc);
    }

    // Called when this View is removed from the screen. Save the
    // state of this View here. This includes freeing resources from
    // memory.
    function onHide() as Void {
    }

    // The user has just looked at their watch. Timers and animations may be started here.
    function onExitSleep() as Void {
    }

    // Terminate any active timers and prepare for slow updates.
    function onEnterSleep() as Void {
    }

    function timerCallback() {
        // var stressIter = getStressIterator();
        getHeartRateData();
    }

    function getHeartRateData() {
        var heartRateSensorIter = getHeartRateIterator();
        var sample;
        var hrDatas = [];

        if(heartRateSensorIter != null){
            sample = heartRateSensorIter.next();
        } else { sample = null; }
        
        var time = System.getClockTime();
        System.println(Lang.format("timerCallback: $1$:$2$:$3$", [time.hour, time.min, time.sec]));

        while (sample != null) {
            hrDatas.add(sample.data);
            sample = heartRateSensorIter.next();
        }

        System.println(hrDatas);
    }

    function getHeartRateIterator() {
        // Check device for SensorHistory compatibility
        if ((Toybox has :SensorHistory) && (Toybox.SensorHistory has :getHeartRateHistory)) {
            var options = {:period => timerUnit, :order => 0};
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
