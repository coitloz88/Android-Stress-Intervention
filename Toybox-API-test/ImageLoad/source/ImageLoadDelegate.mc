import Toybox.Lang;
import Toybox.WatchUi;

class ImageLoadDelegate extends WatchUi.BehaviorDelegate {

    function initialize() {
        BehaviorDelegate.initialize();
    }

    function onMenu() as Boolean {
        WatchUi.pushView(new Rez.Menus.MainMenu(), new ImageLoadMenuDelegate(), WatchUi.SLIDE_UP);
        return true;
    }

}