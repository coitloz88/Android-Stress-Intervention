import Toybox.Lang;
import Toybox.WatchUi;

class HeartRateTestDelegate extends WatchUi.BehaviorDelegate {

    function initialize() {
        BehaviorDelegate.initialize();
    }

    function onMenu() as Boolean {
        WatchUi.pushView(new Rez.Menus.MainMenu(), new HeartRateTestMenuDelegate(), WatchUi.SLIDE_UP);
        return true;
    }

}