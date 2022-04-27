import Toybox.Lang;
import Toybox.WatchUi;

import Toybox.System;
import Toybox.Sensor;
import Toybox.SensorHistory;
import Toybox.Activity;
import Toybox.ActivityMonitor;

import Toybox.Time;

class SensorHistoryTestDelegate extends WatchUi.BehaviorDelegate {

    function initialize() {
        BehaviorDelegate.initialize();
    }

    function onMenu() as Boolean {
        WatchUi.pushView(new Rez.Menus.MainMenu(), new SensorHistoryTestMenuDelegate(), WatchUi.SLIDE_UP);
        return true;
    }

}

// Create a method to get the SensorHistoryIterator object
public class SensorDataClass {
    /*
    public function getIterator(){
    // Check device for SensorHistory compatibility
        if ((Toybox has :SensorHistory) && (Toybox.SensorHistory has :getHeartRateHistory)) {
            return Toybox.SensorHistory.getHeartRateHistory({});
        }
        return null;
    }
    
    var options;
    public function initialize(){
        options = {:period => new Time.Duration(5), :order => ORDER_NEWEST_FIRST};
    }
    */

    public function getHeartRateData(seconds){
        var value = "-1";
        if (Toybox has :SensorHistory){
            System.println("*** HeartRate getData: use SensorHistory");

            if(Toybox.SensorHistory has :getHeartRateHistory){
                var iter = SensorHistory.getHeartRateHistory({:seconds => 2, :order => SensorHistory.ORDER_NEWEST_FIRST});
                System.println("    getHeartRateHistory check");
                if(iter != null){
                    System.println("    iter check");
                    var sample = iter.next();
                    if(sample != null){
                        System.println("    sample check");
                        if(sample.data != null){
                            System.println("    data check");
                            value = sample.data.toString();
                        }
                    }
                }
            }
        } else {
            System.println("*** HeartRate getData: use Activity Monitor");
            var info = Activity.getActivityInfo();
            if(info != null){
                value = info.currentHeartRate;
            }
        }
        System.println("HeartRate: " + value);
        return value;
    }

    public function getSteps(){
        var value = "-1";
        var info = ActivityMonitor.getInfo();
		if (info has :steps){
			value = info.steps;
			if (value > 9999){
				value = (value/1000).format("%.1f")+"k";
			}
		}
        System.println("Steps: " + value);
        return value;
    }
}