import Toybox.Graphics;
import Toybox.WatchUi;
import Toybox.Sensor;
import Toybox.System;
import Toybox.Graphics;
import Toybox.Communications;

class ArrayTransmitView extends WatchUi.View {

    var timeUnit;
    var timeCount;
    var sendTimeUnit;
    public var dicHeartRate = {};

    function initialize() {
        View.initialize();
        timeUnit = 2000; //2s
        timeCount = 0;
        sendTimeUnit = 10000; //10s
    }

    function timerCallback(){
        timeCount += timeUnit / 1000;
        System.println("timeCount: " + timeCount + "s");

        var sensorInfo = Sensor.getInfo();
        if (sensorInfo has :heartRate && sensorInfo.heartRate != null) {
            var userHeartRate = sensorInfo.heartRate;
            System.println("Current Heart Rate: " + userHeartRate);

            dicHeartRate.put(timeCount, userHeartRate);
        } else {
            System.println("There is no accessible data.");
        }

        if(timeCount % (3 * timeUnit / 1000) == 0){
            System.println("=========================");
            System.println(dicHeartRate);      
            System.println("=========================");            
            
            //모바일 앱에 등록된 Garmin Watch Device App ID와 현재 만든 앱의 ID가 달라서 Transmit이 인식되지 않는 것 같음
            //익셉션이나 에러가 발생하지는 않는 듯?
            ArrayTransmitDelegate.transmitTarget(dicHeartRate);

            dicHeartRate = {};
        }

        WatchUi.requestUpdate();
    }

    // Load your resources here
    function onLayout(dc as Dc) as Void {
        setLayout(Rez.Layouts.MainLayout(dc));
        var myTimer = new Timer.Timer();
        myTimer.start(method(:timerCallback), timeUnit, true);
    }

    // Called when this View is brought to the foreground. Restore
    // the state of this View and prepare it to be shown. This includes
    // loading resources into memory.
    function onShow() as Void {
    }

    // Update the view
    function onUpdate(dc as Dc) as Void {
        // Call the parent onUpdate function to redraw the layout
        dc.setColor(Graphics.COLOR_TRANSPARENT, Graphics.COLOR_BLACK);

        dc.clear();
        dc.setColor(Graphics.COLOR_WHITE, Graphics.COLOR_TRANSPARENT);

        //화면에 출력
        //onUpdate가 TimerCallback보다 늦게 호출돼서 3번째 HeartRate는 출력 안됨
        if(!dicHeartRate.isEmpty()){
            dc.drawText(dc.getWidth() / 2, 30, Graphics.FONT_SMALL, "Heart Rates", Graphics.TEXT_JUSTIFY_CENTER);
            dc.drawText(dc.getWidth() / 2, 60,  Graphics.FONT_SMALL, dicHeartRate.toString(), Graphics.TEXT_JUSTIFY_CENTER);
        }
        //View.onUpdate(dc);
    }

    // Called when this View is removed from the screen. Save the
    // state of this View here. This includes freeing resources from
    // memory.
    function onHide() as Void {
    }

}
