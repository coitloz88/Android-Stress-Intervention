import Toybox.Graphics;
import Toybox.WatchUi;
import Toybox.Timer;
import Toybox.System;

// Main view로, Device 앱을 처음 실행하면 보이는 화면을 여기서 설정한다
class TimerTestProjectView extends WatchUi.View {

    var timerCount;

    function initialize() {
        View.initialize();
        timerCount = 0;
    }

    //myTimer가 start되면 myTimer.start의 두번째 argument 간격으로(1000=1s) 첫번째 argument의 메서드(timerCallback)를 호출함
    function timerCallback(){
        timerCount += 5;
        System.println(timerCount);
        WatchUi.requestUpdate();
    }

    // Load your resources here
    // 5초에 한번씩 timerCount를 print함
    function onLayout(dc as Dc) as Void {
        setLayout(Rez.Layouts.MainLayout(dc));
        var myTimer = new Timer.Timer();
        myTimer.start(method(:timerCallback), 5000, true);
    }

    // Called when this View is brought to the foreground. Restore
    // the state of this View and prepare it to be shown. This includes
    // loading resources into memory.
    function onShow() as Void {
    }

    // Update the view
    function onUpdate(dc as Dc) as Void {
        // Call the parent onUpdate function to redraw the layout
        View.onUpdate(dc);
    }

    // Called when this View is removed from the screen. Save the
    // state of this View here. This includes freeing resources from
    // memory.
    function onHide() as Void {
    }

}
