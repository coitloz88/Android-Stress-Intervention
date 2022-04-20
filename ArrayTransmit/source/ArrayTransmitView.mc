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
    var arrayHeartRates = [];

    function initialize() {
        View.initialize();
        timeUnit = 3000; //3s
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

            arrayHeartRates.add(userHeartRate);
        } else {
            System.println("There is no accessible data.");
        }

        if(timeCount % (3 * timeUnit / 1000) == 0){
            //TODO: transmit past data here
            System.println("=========================");

            for( var i = 0; i < arrayHeartRates.size(); i += 1 ) {
                System.println("past heartRate: " + arrayHeartRates[i]);
            }        
            System.println("=========================");
            //arrayHeartRates.removeAll(arrayHeartRates); //arguments?
            
            //CHECK: 이렇게 해도 되는건가?
            arrayHeartRates = [];
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
        // dc.setColor(Graphics.COLOR_TRANSPARENT, Graphics.COLOR_BLACK);

        // dc.clear();
        // dc.setColor(Graphics.COLOR_WHITE, Graphics.COLOR_TRANSPARENT);

        //화면에 출력
        //onUpdate가 TimerCallback보다 늦게 호출되는 듯.. 3번째 HeartRate는 출력 안됨
        // if(arrayHeartRates != null){
        //     dc.drawText(dc.getWidth() / 2, 30, Graphics.FONT_SMALL, "Heart Rates", Graphics.TEXT_JUSTIFY_CENTER);
        //     for( var i = 0; i < arrayHeartRates.size(); i += 1 ) {
        //         dc.drawText(dc.getWidth() / 2, (i + 2) * 30,  Graphics.FONT_SMALL, arrayHeartRates[i], Graphics.TEXT_JUSTIFY_CENTER);
        //     }        
        // }
        //View.onUpdate(dc);
    }

    // Called when this View is removed from the screen. Save the
    // state of this View here. This includes freeing resources from
    // memory.
    function onHide() as Void {
    }

}
