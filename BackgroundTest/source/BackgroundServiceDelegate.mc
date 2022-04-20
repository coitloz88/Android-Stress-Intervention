import Toybox.Background;
import Toybox.Communications;
import Toybox.System;

(:background)
public class BackgroundServiceDelegate extends System.ServiceDelegate{
    // When a scheduled background event triggers, make a request to
    // a service and handle the response with a callback function
    // within this delegate.

    function onTemporalEvent(){
        // do something in here
    }

    function responseCallback(responseCode, data){
        // Do stuff with the response data here and send the data
        // payload back to the app that originated the background
        // process.
        Background.exit(backgroundData);
    }
}