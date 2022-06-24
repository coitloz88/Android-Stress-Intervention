# ⌚Garmin Watch Application

In Garmin watch Application, we can 

- collect target data in background process
- transfer collected data to mobile companion application through BLE
- get feedback from mobile companion application if needed and show the feedback message on foreground display of watch application

All logic runs in real time.

## Collecting sensor data in background process

### Registering for Events

We can register for events the application can subscribe to by using calls in the `Toybox.Background` module.

There are many types of background events, however, in this case, we are going to use `registerForTemporalEvent()` to allow the service to be woken at a repeatedly at a certain interval.

Temporal Events can fire at most every 5 minutes, and run at most 30 seconds each time it runs.

Therefore, during 30 seconds, sensor data is collected and transferred to mobile companion app.

### Collecting sensor data in temporal event

`Toybox.Timer` module causes app crash when it calls `start()` in background process. Therefore, we had to use other similar methods that can be called periodically in background process.

`Sensor.registerSensorDataListener()` was able to call the callback function periodically, but didn't cause the app to crash. The sampling rate can be specified from 1 second to 4 seconds.

In the callback function called by `Sensor.registerSensorDataListener()` , it was possible to collect raw data such as sensor data as well as more complex data such as activity data.

You can set the call period by setting the option of `Sensor.registerSensorDataListener()` as desired, and you can write code that collects the desired sensor data within the called callback function.

In order to transmit such data to the mobile phone, a method called `transmit()` is used.

> 💡 `transmit()` causes memory leak when a dictionary that is too large is transferred with the method. If you want to transmit a lot of data, additional code modifications are required, such as reducing the amount of data by setting the transmission period shorter. However, remember that only 32KB memory can be used in total background process.


## Show feedback message in foreground display

When watch app receives feedback from the phone, first open the Garmin watch app that was running in the background to the foreground to receive a message. After that, the feedback message received from the mobile phone is displayed on the screen using the module `Communications.registerForPhoneAppMessages` .

<br>

# 📱Mobile Companion application

The mobile companion app performs certain calculations based on user data collected and transferred in real time from the Garmin watch app. Afterwards, if it determines that feedback is necessary, the mobile app opens the designated Garmin watch app and sends a feedback message.

## Get data collected in real-time

Data sent from the Garmin watch is handled by event handlers.  `listenByMyAppEvents()` method can receive message from Garmin watch.

## Parsing data

On Garmin watches, data is transmitted in the form of `Toybox.Lang.Dictionary`, but on mobile companion app, it is received as a string in its entirety. That's why we need to parse the received data.

`parseSensorData()` parses received data. Currently, it is parsed assuming that only heartbeatintervals are transmitted, so in order to parse more data, it is necessary to modify the code in the function.

## Save data

Save data received from Garmin Smart Watch in Room DB(local database for Android). You can download data in form of `.csv` file.

## Check whether feedback is needed or not

`isLowerHeartBeatInterval()` check whether the `IBI` is lower than minimum value. This method can also be manipulated in various ways as desired. Finally, it returns a boolean value that determines whether the criterion is met or not.

> 💡 The method `isLowerHeartBeatInterval()` is currently written very simply. If necessary, you can create several more functions, perform more complex operations, and determine whether feedback is needed or not.


## Send feedback

If you need feedback, `giveFeedBack()` is called and you can send a message to your Garmin watch. However, the watch app must be running in the foreground in order to receive messages from the Garmin watch app and display the messages. Therefore, in order to first launch the Garmin Watch app in the foreground, the mobile app prompts the user to confirm whether or not to open the app. When the user opens the app, the sent feedback message is displayed on the screen.


# 📑Reference
<https://pastebin.com/BZRfzcUv>
