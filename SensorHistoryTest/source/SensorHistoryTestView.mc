import Toybox.Graphics;
import Toybox.WatchUi;

import Toybox.SensorHistory;
import Toybox.Lang;
import Toybox.System;

import Toybox.Timer;

class SensorHistoryTestView extends WatchUi.View {

    function initialize() {
        View.initialize();
    }

    // Load your resources here
    function onLayout(dc as Dc) as Void {
        setLayout(Rez.Layouts.MainLayout(dc));
        // var testTimer = new Timer.Timer();
        // testTimer.start(method(:timerCallback), 5000, true);
    }

    // Called when this View is brought to the foreground. Restore
    // the state of this View and prepare it to be shown. This includes
    // loading resources into memory.
    function onShow() as Void {
    }

    // Update the view
    function onUpdate(dc as Dc) as Void {
        dc.clear();
        dc.setColor(Graphics.COLOR_WHITE, Graphics.COLOR_TRANSPARENT);
        dc.drawText(dc.getWidth() / 2, 60, Graphics.FONT_SMALL, "Running...", Graphics.TEXT_JUSTIFY_CENTER);
        // Call the parent onUpdate function to redraw the layout
        // View.onUpdate(dc);
    }

    // Called when this View is removed from the screen. Save the
    // state of this View here. This includes freeing resources from
    // memory.
    function onHide() as Void {
    }

    // Functions for Sensor History Test
    // Create a method to get the SensorHistoryIterator object
    function getIterator() {
        // Check device for SensorHistory compatibility
        if ((Toybox has :SensorHistory) && (Toybox.SensorHistory has :getHeartRateHistory)) {
            return Toybox.SensorHistory.getHeartRateHistory({});
        }
        return null;
    }

    function timerCallback(){
        var sensorIter = getIterator();

        while(sensorIter != null){
            // System.println(sensorIter.next().data);
            // sensorIter = sensorIter.next(); //sensorIter은 SensorHistoryIterator인데 next()는 SensorSample을 반환함 
            System.println("Max: " + sensorIter.getMax());
            System.println("min: " + sensorIter.getMin());
        }

        System.println("=====================");

        WatchUi.requestUpdate();
    }

}

