import Toybox.Sensor;
import Toybox.System;
import Toybox.Background;

(:background)
public class SensorDelegate {
    public function getHeartRate(){
        var sensorInfo = Sensor.getInfo();
        if (sensorInfo has :heartRate && sensorInfo.heartRate != null) {
            var userHeartRate = sensorInfo.heartRate;
            System.println("Current Heart Rate: " + userHeartRate);
            return userHeartRate;
        } else {
            System.println("No accessible heart rate data.");
            return -1;
        }
    }

    public function getAcceleration(){
        var sensorInfo = Sensor.getInfo();
        if (sensorInfo has :accel && sensorInfo.accel != null) {
            var accelArray = sensorInfo.accel;
            System.println("Current Accel Data - x: " + accelArray[0] + ", y: " + accelArray[1]);
            return accelArray;
        } else {
            System.println("No accessible accel data.");
            return -1;
        }
    }
}