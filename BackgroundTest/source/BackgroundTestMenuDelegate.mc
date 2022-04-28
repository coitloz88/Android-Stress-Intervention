import Toybox.Lang;
import Toybox.System;
import Toybox.WatchUi;
import Toybox.Background;
import Toybox.Time;

(:background)
class BackgroundTestMenuDelegate extends WatchUi.MenuInputDelegate {

    function initialize() {
        MenuInputDelegate.initialize();
    }

    function onMenuItem(item as Symbol) as Void {
        if (item == :item_1) {
            Background.exit(null);
            Background.deleteTemporalEvent();
            System.println("item 1");
        } else if (item == :item_2) {
            System.println("item 2");
        }
    }

}