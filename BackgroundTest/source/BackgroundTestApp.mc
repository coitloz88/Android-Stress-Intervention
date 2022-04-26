import Toybox.Application;
import Toybox.Lang;
import Toybox.WatchUi;

import Toybox.Time;
import Toybox.Background;
import Toybox.System;

(:background)
class BackgroundTestApp extends Application.AppBase {

    hidden var _view;

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
        if(canDoBackground()){
            Background.registerForTemporalEvent(new Time.Duration(5 * 60));
        } else {
            System.println("*** Background Process Not Available ***");
        }
        return [ new BackgroundTestView(), new BackgroundTestDelegate() ] as Array<Views or InputDelegates>;
        // return new BackgroundTestView();
    }

    function getServiceDelegate(){
        // var DURATION_SECONDS = new Time.Duration(5 * 60); //5min
        // var eventTime = Time.now().add(DURATION_SECONDS);  

        // Background.registerForTemporalEvent(eventTime);
        // System.println("background event registered");
        return [new BackgroundServiceDelegate()];
    }

    function canDoBackground(){
        return (Toybox.System has :ServiceDelegate);
    }
}

function getApp() as BackgroundTestApp {
    return Application.getApp() as BackgroundTestApp;
}