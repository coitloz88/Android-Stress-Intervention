import Toybox.Application;
import Toybox.Lang;
import Toybox.WatchUi;
import Toybox.Communications;

var page = 0;
var strings = ["","","","",""];
var stringsSize = 5;
var mailMethod;
var phoneMethod;
var crashOnMessage = false;
var hasDirectMessagingSupport = true;

class GetMessageApp extends Application.AppBase {

    function initialize() {
        AppBase.initialize();

        if(Communications has :registerForPhoneAppMessages) {
            Communications.registerForPhoneAppMessages(method(:onPhone));
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
        return [ new GetMessageView(), new GetMessageDelegate() ] as Array<Views or InputDelegates>;
    }

    function onPhone(msg) {
        var i;

        if((crashOnMessage == true) && msg.data.equals("Hi")) {
            msg.length(); // Generates a symbol not found error in the VM
        }

        for(i = (stringsSize - 1); i > 0; i -= 1) {
            strings[i] = strings[i-1];
        }
        strings[0] = msg.data.toString();
        page = 1;

        WatchUi.requestUpdate();
    }

}

function getApp() as GetMessageApp {
    return Application.getApp() as GetMessageApp;
}