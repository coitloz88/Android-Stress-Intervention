import Toybox.Lang;
import Toybox.WatchUi;

class BgGetMessageDelegate extends WatchUi.BehaviorDelegate {

    function initialize() {
        BehaviorDelegate.initialize();
    }

    function onMenu() as Boolean {
        WatchUi.pushView(new Rez.Menus.MainMenu(), new BgGetMessageMenuDelegate(), WatchUi.SLIDE_UP);
        return true;
    }

}