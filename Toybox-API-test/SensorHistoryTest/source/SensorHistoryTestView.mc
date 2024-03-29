import Toybox.Graphics;
import Toybox.WatchUi;

import Toybox.Timer;
import Toybox.ActivityMonitor;
import Toybox.SensorHistory;
import Toybox.System;
import Toybox.Lang;

class SensorHistoryTestView extends WatchUi.View {

    var timerUnit = 3; // 3s

    function initialize() {
        View.initialize();
    }

    // Load your resources here
    function onLayout(dc as Dc) as Void {
        setLayout(Rez.Layouts.MainLayout(dc));
        // var sensorTimer = new Timer.Timer();
        // sensorTimer.start(method(:timerCallback), timerUnit * 1000, true);
        getHeartRateData();
    }

    // Called when this View is brought to the foreground. Restore
    // the state of this View and prepare it to be shown. This includes
    // loading resources into memory.
    function onShow() as Void {
    }

    // Update the view
    function onUpdate(dc as Dc) as Void {
        dc.setColor(Graphics.COLOR_WHITE, Graphics.COLOR_BLACK);
        dc.clear();
        dc.drawText(dc.getWidth() / 2, 80,  Graphics.FONT_SMALL, "Running..", Graphics.TEXT_JUSTIFY_CENTER);
    }

    // Called when this View is removed from the screen. Save the
    // state of this View here. This includes freeing resources from
    // memory.
    function onHide() as Void {
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
            var options = {:period => timerUnit * 10, :order => 0};
            return Toybox.SensorHistory.getHeartRateHistory(options);
        }
        return null;
    }

    function getStressIterator() {
        if ((Toybox has :SensorHistory) && (Toybox.SensorHistory has :getStressHistory)) {
            // Set up the method with parameters
            return Toybox.SensorHistory.getStressHistory({});
        }
        return null;
    }

}
