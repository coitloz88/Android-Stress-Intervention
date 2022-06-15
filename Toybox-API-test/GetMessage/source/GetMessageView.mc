import Toybox.Graphics;
import Toybox.WatchUi;

import Toybox.Communications;
import Toybox.System;

class GetMessageView extends WatchUi.View {

    function initialize() {
        View.initialize();
    }

    // Load your resources here
    function onLayout(dc as Dc) as Void {
        setLayout(Rez.Layouts.MainLayout(dc));
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
        
        if(hasDirectMessagingSupport) {
            if(page == 0) {
                dc.drawText(dc.getWidth() / 2, 80,  Graphics.FONT_MEDIUM, "Waiting...", Graphics.TEXT_JUSTIFY_CENTER);
            } else {
                var i;
                var y = 50;

                dc.drawText(dc.getWidth() / 2, 20,  Graphics.FONT_MEDIUM, "Strings Received:", Graphics.TEXT_JUSTIFY_CENTER);
                for(i = 0; i < stringsSize; i += 1) {
                    dc.drawText(dc.getWidth() / 2, y,  Graphics.FONT_SMALL, strings[i], Graphics.TEXT_JUSTIFY_CENTER);
                    y += 20;
                }
             }
        } else {
            dc.drawText(dc.getWidth() / 2, dc.getHeight() / 3, Graphics.FONT_MEDIUM, "Direct Messaging API\nNot Supported", Graphics.TEXT_JUSTIFY_CENTER);
        }
    }

    // Called when this View is removed from the screen. Save the
    // state of this View here. This includes freeing resources from
    // memory.
    function onHide() as Void {
    }

}
