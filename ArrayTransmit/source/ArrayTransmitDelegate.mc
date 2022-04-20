import Toybox.Lang;
import Toybox.WatchUi;
import Toybox.Communications;
import Toybox.System;

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

class ArrayTransmitDelegate extends WatchUi.BehaviorDelegate {

    function initialize() {
        BehaviorDelegate.initialize();
    }

    function onMenu() as Boolean {
        WatchUi.pushView(new Rez.Menus.MainMenu(), new ArrayTransmitMenuDelegate(), WatchUi.SLIDE_UP);
        return true;
    }

    public function transmitArraytoStringForm(target){
        if(target != null){
            var targetStringForm;
            for(var i = 0; i < target.size(); i += 1){
                
            }
        }
    }

}