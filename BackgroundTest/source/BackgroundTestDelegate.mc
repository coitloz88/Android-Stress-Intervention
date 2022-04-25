import Toybox.Lang;
import Toybox.WatchUi;

import Toybox.Background;
import Toybox.System;
import Toybox.Communications;

class BackgroundTestDelegate extends WatchUi.BehaviorDelegate {

    function initialize() {
        BehaviorDelegate.initialize();
    }

    function onMenu() as Boolean {
        WatchUi.pushView(new Rez.Menus.MainMenu(), new BackgroundTestMenuDelegate(), WatchUi.SLIDE_UP);
        return true;
    }
}

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

    function initialize(){
        System.ServiceDelegate.initialize();
        System.println("call initialize()");
    }

    function onTemporalEvent() {
        // A callback method that is triggered in the background when time-based events occur.
        System.println("call onTemporalEvent()");

        // if(System.getDeviceSettings().phoneConnected){
            // System.println("Call `if` clause");
            // var listener = new CommListener();
            var currentHeartRateData = HeartRateSensorDelegate.getHeartRate();
            System.println(currentHeartRateData);
            // Communications.transmit(currentHeartRateData, "null", listener);
        // } else {
        //    Background.exit(null);
        // }
    }
}