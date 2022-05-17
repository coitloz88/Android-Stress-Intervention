import Toybox.Lang;
import Toybox.WatchUi;

import Toybox.Background;
import Toybox.System;
import Toybox.Communications;
import Toybox.Sensor;

class BgGetMessageDelegate extends WatchUi.BehaviorDelegate {

    function initialize() {
        BehaviorDelegate.initialize();
    }

    function onMenu() as Boolean {
        WatchUi.pushView(new Rez.Menus.MainMenu(), new BgGetMessageMenuDelegate(), WatchUi.SLIDE_UP);
        return true;
    }

}

(:background)
class CommListener extends Communications.ConnectionListener {
    function initialize() {
        Communications.ConnectionListener.initialize();
    }

    function onComplete() {
        System.println("Transmit Complete");
    }

    function onError() {
        System.println("Transmit Failed");
    }

}


(:background)
public class BackgroundServiceDelegate extends System.ServiceDelegate {
    // When a scheduled background event triggers, make a request to
    // a service and handle the response with a callback function
    // within this delegate.

    private var listener;

    function initialize(){
        System.ServiceDelegate.initialize();
        System.println("call initialize()");


    }

    function onTemporalEvent(){
        // A callback method that is triggered in the background when time-based events occur.
        System.println("call onTemporalEvent()");

        // TODO: write something to do in background in this block
        
        // if(Communications has :registerForPhoneAppMessages) {
        //     Communications.registerForPhoneAppMessages(method(:onPhone));
        // } else {
        //     hasDirectMessagingSupport = false;
        //     System.println("No registerForPhoneAppMessages");
        // }
    }



}