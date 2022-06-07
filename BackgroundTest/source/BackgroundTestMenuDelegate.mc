import Toybox.Lang;
import Toybox.System;
import Toybox.WatchUi;

import Toybox.Background;
import Toybox.Communications;

(:background)
class BackgroundTestMenuDelegate extends WatchUi.MenuInputDelegate {

    function initialize() {
        MenuInputDelegate.initialize();
    }

    function onMenuItem(item as Symbol) as Void {
        if (item == :item_1) {
            Background.exit(null);
            Background.deleteTemporalEvent();
            System.println("stop background event");
        } else if (item == :item_2) {
            var listener = new CommListener();
            var sampleDictionary = {1 => [800, 700, 900]};
            Communications.transmit(sampleDictionary, null, listener);
            System.println("send high HRV data in foreground");
        } else if (item == :item_3) {
            var listener = new CommListener();
            var sampleDictionary = {2 => [800, 805, 795]};
            Communications.transmit(sampleDictionary, null, listener);
            System.println("send low HRV data in foreground");
        }
    }

}