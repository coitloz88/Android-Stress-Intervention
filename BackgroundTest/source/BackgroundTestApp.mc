import Toybox.Application;
import Toybox.Lang;
import Toybox.WatchUi;

import Toybox.Communications;
import Toybox.Background;
import Toybox.System;

var page = 0;
var receivedString;
var mailMethod;
var phoneMethod;
var crashOnMessage = false;
var hasDirectMessagingSupport = true;

(:background)
class BackgroundTestApp extends Application.AppBase {

    hidden var _view;

    function initialize() {
        AppBase.initialize();

        // mailMethod = method(:onMail);
        // phoneMethod = method(:onPhone);

        // if(Communications has :registerForPhoneAppMessages) {
        //     Communications.registerForPhoneAppMessages(phoneMethod);
        // } else if(Communications has :setMailboxListener) {
        //     Communications.setMailboxListener(mailMethod);
        // } else {
        //     hasDirectMessagingSupport = false;
        // }

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

    function onMail(mailIter){
        var mail;
        mail = mailIter.next();

        if(mail != null){
            receivedString = mail.toString();
            page = 1;
        }

        Communications.emptyMailbox();
        WatchUi.requestUpdate();
    }

    function onPhone(msg){
        if((crashOnMessage) == true)

        receivedString = msg.data.toString();
        page = 1;

        WatchUi.requestUpdate();
    }
}

function getApp() as BackgroundTestApp {
    return Application.getApp() as BackgroundTestApp;
}