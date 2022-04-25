import Toybox.Sensor;
import Toybox.System;
import Toybox.Background;

(:background)
public class HeartRateSensorDelegate {
    public function getHeartRate(){
        var sensorInfo = Sensor.getInfo();
        if (sensorInfo has :heartRate && sensorInfo.heartRate != null) {
            var userHeartRate = sensorInfo.heartRate;
            System.println("Current Heart Rate: " + userHeartRate);
            return userHeartRate;
        } else {
            System.println("No accessible data.");
            return -1;
        }
    }
}