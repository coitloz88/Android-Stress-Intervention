import Toybox.Lang;
import Toybox.System;
import Toybox.WatchUi;

class ArrayTransmitMenuDelegate extends WatchUi.MenuInputDelegate {

    function initialize() {
        MenuInputDelegate.initialize();
    }

    function onMenu(){
    }

    function onMenuItem(item as Symbol) as Void {
        if (item == :item1) {
            System.println("Send Data");
        } else if (item == :item_2) {
            System.println("item 2");
        }
    }

}