import Toybox.Graphics;
import Toybox.Lang;
import Toybox.System;
import Toybox.WatchUi;

class BgFaceView extends WatchUi.WatchFace {


    function initialize() {
        WatchFace.initialize();
    }

    // Load your resources here
    function onLayout(dc as Dc) as Void {        
        System.println("call onLayout()");

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
        }
        else {
            dc.setColor(Graphics.COLOR_WHITE, Graphics.COLOR_BLACK);
            dc.clear();
            var clockTime = System.getClockTime();
            var timeString = Lang.format("$1$:$2$", [clockTime.hour, clockTime.min.format("%02d")]);        
                
            // var view = View.findDrawableById("TimeLabel") as Text;
            // view.setText(timeString);    

            dc.drawText(dc.getWidth() / 2, dc.getHeight() / 2,  Graphics.FONT_SMALL, timeString, Graphics.TEXT_JUSTIFY_CENTER);
            // Call the parent onUpdate function to redraw the layout
        }
        needFeedback = false;
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
