import Toybox.Application;
import Toybox.Lang;
import Toybox.WatchUi;

import Toybox.Background;
import Toybox.System;
import Toybox.Time;

var IBI_data;
var needBreath; //피드백 필요 여부 저장: 0 - 피드백 X, 1 - 피드백 O

(:background)
class BgGetMessageApp extends Application.AppBase {

    enum {
        BACKGROUND_REPONSE_CODE //피드백 필요 여부 저장
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
        // if(data[BACKGROUND_REPONSE_CODE] != null){

        //     //background에서 넘어온 데이터가 있다면, 해당 데이터(reponse code)를 퍼블릭 변수에 저장

        //     Application.Storage.setValue(BACKGROUND_REPONSE_CODE, data[BACKGROUND_REPONSE_CODE]);
        //     // System.println("data code: " + data[BACKGROUND_REPONSE_CODE]);
        //     needBreath = Application.Storage.getValue(BACKGROUND_REPONSE_CODE);
        //     System.println("BACKGROUND_REPONSE_CODE: " + needBreath);
        // }

        if(data != null){
            needBreath = data;
            System.println("data: " + data);
        } 
    }
}

function getApp() as BgGetMessageApp {
    return Application.getApp() as BgGetMessageApp;
}