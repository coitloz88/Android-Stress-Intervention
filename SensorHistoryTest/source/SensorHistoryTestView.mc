import Toybox.Graphics;
import Toybox.WatchUi;

import Toybox.Timer;
import Toybox.SensorHistory;

class SensorHistoryTestView extends WatchUi.View {

    hidden var timerCount;
    hidden var timerUnit; // > 1s

    hidden var userHeartRate;
    hidden var options;

    hidden var HRStrings = "***";

    function initialize() {
        View.initialize();
        timerCount = 0;
        timerUnit = 5; //5s

        options = {:period => (timerUnit - 1), :order => SensorHistory.ORDER_NEWEST_FIRST};
    }

    // Load your resources here
    function onLayout(dc as Dc) as Void {
        setLayout(Rez.Layouts.MainLayout(dc));

        var myTimer = new Timer.Timer();
        myTimer.start(method(:timerCallback), timerUnit * 1000, true);
    }

    // Called when this View is brought to the foreground. Restore
    // the state of this View and prepare it to be shown. This includes
    // loading resources into memory.
    function onShow() as Void {
    }

    // Update the view
    function onUpdate(dc as Dc) as Void {
        dc.setColor(Graphics.COLOR_TRANSPARENT, Graphics.COLOR_BLACK);
        dc.clear();
        dc.setColor(Graphics.COLOR_WHITE, Graphics.COLOR_TRANSPARENT);
        dc.drawText(dc.getWidth() / 2, 20, Graphics.FONT_SMALL, "Running...", Graphics.TEXT_JUSTIFY_CENTER);
        dc.drawText(dc.getWidth() / 2, 40, Graphics.FONT_SMALL, HRStrings, Graphics.TEXT_JUSTIFY_CENTER);
    }

    // Called when this View is removed from the screen. Save the
    // state of this View here. This includes freeing resources from
    // memory.
    function onHide() as Void {
    }

    function timerCallback(){
        timerCount += timerUnit;
        userHeartRate = getIterator();


        if(userHeartRate != null){
            var sample = userHeartRate.next();
            do {
                var data = sample.data;
                System.println("@ Data: " + data);
                sample = userHeartRate.next();
            } while(sample.data != null);

            HRStrings = userHeartRate.next().data;
        } else {
            HRStrings = "***";
        }

        System.println("* HRStrings: " + HRStrings);
        WatchUi.requestUpdate();
    }

    function getIterator(){
        if ((Toybox has :SensorHistory) && (Toybox.SensorHistory has :getHeartRateHistory)) {
            return Toybox.SensorHistory.getHeartRateHistory(options);
        }
        return null;
    }

}