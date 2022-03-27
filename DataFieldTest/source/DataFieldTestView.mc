import Toybox.Activity;
import Toybox.Graphics;
import Toybox.Lang;
import Toybox.WatchUi;

class DataFieldTestView extends WatchUi.DataField {

    hidden var mValue as Numeric;
    hidden var label = "Total"; //label의 초기값

    hidden var timerRunning = 0; //유저가 시작 버튼을 눌렀는지
    hidden var stepsNonActive = 0; //이번 activity의 steps
    hidden var stepsRecorded = 0; //이번 activity에 의해 기록된 steps의 수

    hidden var ticker = 0; //active상태의 timer

    hidden var valueFormat = "%d";

    function initialize() {
        DataField.initialize();
        mValue = 0; //0.0f
    }

    // Set your layout here. Anytime the size of obscurity of
    // the draw context is changed this will be called.
    function onLayout(dc as Dc) as Void {
        var obscurityFlags = DataField.getObscurityFlags();

        // Top left quadrant so we'll use the top left layout
        if (obscurityFlags == (OBSCURE_TOP | OBSCURE_LEFT)) {
            View.setLayout(Rez.Layouts.TopLeftLayout(dc));

        // Top right quadrant so we'll use the top right layout
        } else if (obscurityFlags == (OBSCURE_TOP | OBSCURE_RIGHT)) {
            View.setLayout(Rez.Layouts.TopRightLayout(dc));

        // Bottom left quadrant so we'll use the bottom left layout
        } else if (obscurityFlags == (OBSCURE_BOTTOM | OBSCURE_LEFT)) {
            View.setLayout(Rez.Layouts.BottomLeftLayout(dc));

        // Bottom right quadrant so we'll use the bottom right layout
        } else if (obscurityFlags == (OBSCURE_BOTTOM | OBSCURE_RIGHT)) {
            View.setLayout(Rez.Layouts.BottomRightLayout(dc));

        // Use the generic, centered layout
        } else {
            View.setLayout(Rez.Layouts.MainLayout(dc));
            var labelView = View.findDrawableById("label");
            labelView.locY = labelView.locY - 16;
            var valueView = View.findDrawableById("value");
            valueView.locY = valueView.locY + 7;
        }

        (View.findDrawableById("label") as Text).setText(Rez.Strings.label);
    }

    function isSingleFieldLayout(){
        return (DataField.getObscurityFlags() == OBSCURE_TOP | OBSCURE_LEFT | OBSCURE_BOTTOM | OBSCURE_RIGHT);
    }

    // The given info object contains all the current workout information.
    // Calculate a value and save it locally in this method.
    // Note that compute() and onUpdate() are asynchronous, and there is no
    // guarantee that compute() will be called before onUpdate().
    function compute(info as Activity.Info) as Void {
        // See Activity.Info in the documentation for available information.
        
        /*
        if(info has :currentHeartRate){
            if(info.currentHeartRate != null){
                mValue = info.currentHeartRate as Number;
            } else {
                mValue = 0.0f;
            }
        }
        */

        var activityMonitorInfo = Toybox.ActivityMonitor.getInfo();
        if(timerRunning){
            stepsRecorded = activityMonitorInfo.steps - stepsNonActive;
            ++ticker;
        }

        var timerSlot = (ticker % 20); 

        if (timerSlot <= 4) { 
            label = "Total Steps";
            mValue = activityMonitorInfo.steps;
        } else if (timerSlot <= 9) {
            label = "Active Steps";
            mValue = stepsRecorded;
        } else if (timerSlot <= 14) {
            label = "Steps To Go";
            mValue = (activityMonitorInfo.stepGoal - activityMonitorInfo.steps);
            if (mValue < 0) {
                mValue = 0;
            }
        } else if (timerSlot <= 19) {
            label = "Goal (%)";
            mValue = (activityMonitorInfo.steps * 100.0) / activityMonitorInfo.stepGoal;
        } else {
            mValue = 0;
        }

    }

    // Display the value you computed here. This will be called
    // once a second when the data field is visible.
    function onUpdate(dc as Dc) as Void {

        /*
        // Set the background color
        (View.findDrawableById("Background") as Text).setColor(getBackgroundColor());

        // Set the foreground color and value
        var value = View.findDrawableById("value") as Text;
        if (getBackgroundColor() == Graphics.COLOR_BLACK) {
            value.setColor(Graphics.COLOR_WHITE);
        } else {
            value.setColor(Graphics.COLOR_BLACK);
        }
        value.setText(mValue.format("%.2f"));

        //label도 정기적으로 update
        //View.findDrawableById("label").setText(label);

        // Call parent's onUpdate(dc) to redraw the layout
        View.onUpdate(dc);
        */

        var width = dc.getWidth();
        var height = dc.getHeight();
        var textCenter = Graphics.TEXT_JUSTIFY_CENTER | Graphics.TEXT_JUSTIFY_VCENTER;
        var backgroundColor = getBackgroundColor();

        dc.setColor(backgroundColor, Graphics.COLOR_TRANSPARENT);
        dc.fillRectangle(0, 0, width, height);
        
        dc.setColor((backgroundColor == Graphics.COLOR_BLACK) ? Graphics.COLOR_WHITE : Graphics.COLOR_BLACK, Graphics.COLOR_TRANSPARENT);
    
        if (isSingleFieldLayout()) {
            dc.drawText(width / 2, height / 2 - 40, Graphics.FONT_TINY, label, textCenter);
            dc.drawText(width / 2, height / 2 + 7, Graphics.FONT_NUMBER_THAI_HOT, mValue.format(valueFormat), textCenter);
        } else {
            dc.drawText(width / 2, 5 + (height - 55) / 2, Graphics.FONT_TINY, label, textCenter);
            dc.drawText(width / 2, (height - 23) - (height - 55) / 2 - 1, Graphics.FONT_NUMBER_HOT, mValue.format(valueFormat), textCenter);
        }
    
    }

    //stop -> running
    function onTimerStart(){
        if(!timerRunning){
            var activityMonitorInfo = Toybox.ActivityMonitor.getInfo();
            stepsNonActive = activityMonitorInfo.steps - stepsRecorded;
            timerRunning = true;
        }
    }

    //running -> stop
    function onTimerStop(){
        timerRunning = false;
        ticker = 0;
    }

    //Activity 종료
    function onTimerReset(){
        stepsRecorded = 0;
    }

}
