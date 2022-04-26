import Toybox.Lang;
import Toybox.WatchUi;

import Toybox.Background;
import Toybox.System;
import Toybox.Communications;
import Toybox.Timer;

class BackgroundTestDelegate extends WatchUi.BehaviorDelegate {

    function initialize() {
        BehaviorDelegate.initialize();
    }

    function onMenu() as Boolean {
        WatchUi.pushView(new Rez.Menus.MainMenu(), new BackgroundTestMenuDelegate(), WatchUi.SLIDE_UP);
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

    // var timerCount;
    // var myTimer;

    // var maxTime;
    // var timerUnit;

    function initialize(){
        System.ServiceDelegate.initialize();
        System.println("call initialize()");
        // maxTime = 20; //20s
        // timerUnit = 2; //2s
    }

    function onTemporalEvent() {
        // A callback method that is triggered in the background when time-based events occur.
        System.println("call onTemporalEvent()");
        // timerCount = 0;

        if(System.getDeviceSettings().phoneConnected){
            System.println("Call `if` clause");
            var listener = new CommListener();
            var currentHeartRateData = SensorDelegate.getHeartRate();
            System.println(currentHeartRateData);
            Communications.transmit(currentHeartRateData, "null", listener);
            // Background.exit(null);
        } else {
            // myTimer.stop();
            // timerCount = 0;
            Background.exit(null);
        }

        // myTimer = new Timer.Timer();
        // myTimer.start(method(:timerCallback), timerUnit, true);
    }

    // function timerCallback(){

    //     System.println("call timerCallback()");

    //     if(timerCount >= maxTime){
    //         myTimer.stop();
    //         timerCount = 0;
    //     } else {
    //         timerCount += timerUnit;
    //         System.print(timerCount + "s: ");
    //         WatchUi.requestUpdate();
    //     }
    // }
}