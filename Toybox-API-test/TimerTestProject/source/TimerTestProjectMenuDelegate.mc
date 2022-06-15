import Toybox.Lang;
import Toybox.System;
import Toybox.WatchUi;

// Device App을 실행하여 Up 버튼을 눌렀을 때 선택할 수 있는 메뉴를 여기서 설정
class TimerTestProjectMenuDelegate extends WatchUi.MenuInputDelegate {

    function initialize() {
        MenuInputDelegate.initialize();
    }

    function onMenuItem(item as Symbol) as Void {
        if (item == :item_1) {
            System.println("item 1");
        } else if (item == :item_2) {
            System.println("item 2");
        }
    }

}