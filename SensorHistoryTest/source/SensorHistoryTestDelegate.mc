import Toybox.Lang;
import Toybox.WatchUi;

import Toybox.System;
import Toybox.SensorHistory;

class SensorHistoryTestDelegate extends WatchUi.BehaviorDelegate {

    function initialize() {
        BehaviorDelegate.initialize();
    }

    function onMenu() as Boolean {
        WatchUi.pushView(new Rez.Menus.MainMenu(), new SensorHistoryTestMenuDelegate(), WatchUi.SLIDE_UP);
        return true;
    }

}
