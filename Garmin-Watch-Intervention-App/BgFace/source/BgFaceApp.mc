import Toybox.Application;
import Toybox.Lang;
import Toybox.WatchUi;

import Toybox.Background;
import Toybox.System;
import Toybox.Time;

var needFeedback;

(:background)
class BgFaceApp extends Application.AppBase {

    function initialize() {
        AppBase.initialize();
        needFeedback = false;
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
        return [ new BgFaceView() ] as Array<Views or InputDelegates>;
    }

    function getServiceDelegate(){
        return [new BackgroundServiceDelegate()];
    }

    function canDoBackground(){
        return (Toybox.System has :ServiceDelegate);
    }

    function onBackgroundData(data) {
        if(data != null){
            needFeedback = data;
            System.println("passed data: " + data);
        } 
    }
}

function getApp() as BgFaceApp {
    return Application.getApp() as BgFaceApp;
}