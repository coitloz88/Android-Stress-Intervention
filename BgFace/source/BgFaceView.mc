import Toybox.Graphics;
import Toybox.Lang;
import Toybox.System;
import Toybox.WatchUi;

class BgFaceView extends WatchUi.WatchFace {
    var timer;

    function initialize() {
        WatchFace.initialize();
        timer = 0;
    }

    // Load your resources here
    function onLayout(dc as Dc) as Void {        
        setLayout(Rez.Layouts.WatchFace(dc));
    }

    // Called when this View is brought to the foreground. Restore
    // the state of this View and prepare it to be shown. This includes
    // loading resources into memory.
    function onShow() as Void {
    }

    // Update the view
    function onUpdate(dc as Dc) as Void {
        if(needFeedback){
            dc.setColor(Graphics.COLOR_BLACK, Graphics.COLOR_BLUE);
            dc.clear();
            var image = WatchUi.loadResource(Rez.Drawables.img_Breathing);
            dc.drawBitmap(10, 30, image);
            dc.drawText(dc.getWidth() / 2, 60,  Graphics.FONT_SMALL, "Take a Breath", Graphics.TEXT_JUSTIFY_CENTER);
            if(timer >= 3){ //set max showing period in here (3 = 3 seconds)
                timer = 0;
                needFeedback = false;
            } else {
                timer += 1;
            }
        }
        else {
            dc.setColor(Graphics.COLOR_WHITE, Graphics.COLOR_BLACK);
            dc.clear();
            var clockTime = System.getClockTime();
            var timeString = Lang.format("$1$:$2$", [clockTime.hour, clockTime.min.format("%02d")]);        

            var systemStats = System.getSystemStats();

            // var view = View.findDrawableById("TimeLabel") as Text;
            // view.setText(timeString);                
            dc.drawText(dc.getWidth() / 2, 65,  Graphics.FONT_MEDIUM, timeString, Graphics.TEXT_JUSTIFY_CENTER);
            dc.drawText(dc.getWidth() / 2, 95,  Graphics.FONT_TINY, systemStats.battery.toNumber() + " / 100", Graphics.TEXT_JUSTIFY_CENTER);
            // Call the parent onUpdate function to redraw the layout

            dc.setColor(Graphics.COLOR_YELLOW, Graphics.COLOR_BLACK);
            dc.fillRectangle(dc.getWidth() / 2, 55, 4, 4);
        }
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

}
