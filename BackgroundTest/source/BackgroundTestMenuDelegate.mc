import Toybox.Lang;
import Toybox.System;
import Toybox.WatchUi;
import Toybox.Background;
import Toybox.Time;

class BackgroundTestMenuDelegate extends WatchUi.MenuInputDelegate {

    function initialize() {
        MenuInputDelegate.initialize();
    }

    function onMenuItem(item as Symbol) as Void {
        if (item == :item_1) {
            System.println("item 1");
            Background.deleteTemporalEvent();
        } else if (item == :item_2) {
            System.println("item 2");
        }
    }

}