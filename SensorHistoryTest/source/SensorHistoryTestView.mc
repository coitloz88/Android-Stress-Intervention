import Toybox.Graphics;
import Toybox.WatchUi;

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
        dc.setColor(Graphics.COLOR_TRANSPARENT, Graphics.COLOR_BLACK);
        dc.clear();
        dc.setColor(Graphics.COLOR_WHITE, Graphics.COLOR_TRANSPARENT);
        dc.drawText(dc.getWidth() / 2, 30, Graphics.FONT_SMALL, "Running...", Graphics.TEXT_JUSTIFY_CENTER);
        // Call the parent onUpdate function to redraw the layout
        var userHeartRate = SensorDataClass.getHeartRateData();
        var userSteps = SensorDataClass.getSteps();

        if(userHeartRate != null && userSteps != null){
            dc.drawText(dc.getWidth() / 2, 60, Graphics.FONT_SMALL, userHeartRate, Graphics.TEXT_JUSTIFY_CENTER);
            dc.drawText(dc.getWidth() / 2, 90, Graphics.FONT_SMALL, userSteps, Graphics.TEXT_JUSTIFY_CENTER);
        }
        // WatchUi.requestUpdate();
    }

    // Called when this View is removed from the screen. Save the
    // state of this View here. This includes freeing resources from
    // memory.
    function onHide() as Void {
    }

}