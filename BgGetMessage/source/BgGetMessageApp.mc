import Toybox.Application;
import Toybox.Lang;
import Toybox.WatchUi;

import Toybox.Communications;
import Toybox.Background;
import Toybox.System;
import Toybox.Time;

var page = 0;
var receivedString;
var phoneMethod;
var hasDirectMessagingSupport = true;

(:background)
class BgGetMessageApp extends Application.AppBase {

    function initialize() {
        AppBase.initialize();

        phoneMethod = method(:onPhone);
        if(Communications has :registerForPhoneAppMessage){
            Communications.registerForPhoneAppMessages(phoneMethod);
        } else {
            hasDirectMessagingSupport = false;
        }
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
            System.println("Background Process Not Available");
        }
        return [ new BgGetMessageView(), new BgGetMessageDelegate() ] as Array<Views or InputDelegates>;
    }

    function getServiceDelegate(){
        return [new BackgroundServiceDelegate()];
    }

    function canDoBackground(){
        return (Toybox.System has :ServiceDelegate);
    }

    function onPhone(msg){
        System.println("call onPhone()");

        hasDirectMessagingSupport = true;
        receivedString = msg.data.toString();
        page = 1;

        System.println("Received message: " + receivedString);

        WatchUi.requestUpdate();
    }

}

function getApp() as BgGetMessageApp {
    return Application.getApp() as BgGetMessageApp;
}