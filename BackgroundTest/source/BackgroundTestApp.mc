import Toybox.Application;
import Toybox.Lang;
import Toybox.WatchUi;

import Toybox.Time;
import Toybox.Background;
import Toybox.System;

class BackgroundTestApp extends Application.AppBase {

    function initialize() {
        AppBase.initialize();
    }

    // onStart() is called on application start up
    function onStart(state as Dictionary?) as Void {
    }

    // onStop() is called when your application is exiting
    function onStop(state as Dictionary?) as Void {
    }

    // Return the initial view of your application here
    function getInitialView() as Array<Views or InputDelegates>? {
        return [ new BackgroundTestView(), new BackgroundTestDelegate() ] as Array<Views or InputDelegates>;
    }

    function getServiceDelegate(){
        var DURATION_SECONDS = new Time.Duration(5 * 60); //5m
        var eventTime = Time.now().add(DURATION_SECONDS);  

        Background.registerForTemporalEvent(eventTime);
// https://developer.garmin.com/connect-iq/api-docs/Toybox/Application/AppBase.html#getServiceDelegate-instance_function            
//            Background.getServiceDelegate();
        System.println("background event registered");
    }

}

function getApp() as BackgroundTestApp {
    return Application.getApp() as BackgroundTestApp;
}