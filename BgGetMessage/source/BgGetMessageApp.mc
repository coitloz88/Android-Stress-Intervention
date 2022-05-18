import Toybox.Application;
import Toybox.Lang;
import Toybox.WatchUi;

import Toybox.Background;
import Toybox.System;
import Toybox.Time;

var IBI_data;
var needBreath;
//그런데 어차피 foreground로 앱이 켜지는 거라면 foreground에서 다시 데이터를 수집하면 되니까 굳이 background data를 넘길 필요는 없지 않나?

(:background)
class BgGetMessageApp extends Application.AppBase {
    enum {
        BACKGROUND_REPONSE_CODE
    }

    function initialize() {
        AppBase.initialize();
        needBreath = 0;
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

    function onBackgroundData(data) {
        if(data[BACKGROUND_REPONSE_CODE] != null){
            Application.Storage.setValue(BACKGROUND_REPONSE_CODE, data[BACKGROUND_REPONSE_CODE]);
            // System.println("data code: " + data[BACKGROUND_REPONSE_CODE]);
            needBreath = Application.Storage.getValue(BACKGROUND_REPONSE_CODE);
            // System.println("BACKGROUND_REPONSE_CODE: " + code);      
        } 
    }
}

function getApp() as BgGetMessageApp {
    return Application.getApp() as BgGetMessageApp;
}