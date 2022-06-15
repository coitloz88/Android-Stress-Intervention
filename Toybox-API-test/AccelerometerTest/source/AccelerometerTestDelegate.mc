import Toybox.Lang;
import Toybox.WatchUi;

class AccelerometerTestDelegate extends WatchUi.BehaviorDelegate {

    function initialize() {
        BehaviorDelegate.initialize();
    }

    function onMenu() as Boolean {
        WatchUi.pushView(new Rez.Menus.MainMenu(), new AccelerometerTestMenuDelegate(), WatchUi.SLIDE_UP);
        return true;
    }

}