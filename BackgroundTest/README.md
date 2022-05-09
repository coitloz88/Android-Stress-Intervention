# BackgroundTest
목표: Background(앱 실행 X)에서 5분에 한번씩 측정한 심박수를 휴대폰으로 보냄  

1. 단일 측정 동작
    * background 심박수 전송 성공
    * ~~Accel Data의 경우 센서 데이터 항목만 바꿔서 보내면 될 것 같음~~ 함수 `getAcceleration()` 추가함
    * 센서 데이터를 받지 못하는 경우 일괄적으로 `-1` 전송

2. 여러 번 측정 동작
    * Emulator에서 `registerSensorDataListener`의 콜백함수를 주기적으로 호출해서 `SensorData`가 측정됨



    * ~~그러나 해당 콜백함수는 Background에서 실행된다고 표시되지만 실제로 앱을 나가게 되면 수집 함수가 호출되지 않음(왜?)~~
        - 센서 데이터 수집 중 앱이 종료되면 수집이 종료됨(에뮬레이터)
        - stack overflow에서 background에서의 callback함수 호출 불가에 관한 [논의](https://stackoverflow.com/questions/66377387/garmin-makewebrequest-in-background-service-delegate-can-not-wake-app-from-callb)가 있었음
        - 실제 디바이스에서 잘 됨! **에뮬레이터 상에서만 백그라운드 디버깅을 어떻게 하는지 이해가 부족했던 것으로 보임**
    * 30초 후 Temporal Background Event는 자동으로 종료되며, 5분뒤에 재실행됨
    * 실제 디바이스 & 휴대폰에서 테스트해본 결과 `transmit`이 안됨 => ~~[garmin forum](https://forums.garmin.com/developer/connect-iq/i/bug-reports/background-event-system-does-not-work) 확인 결과 메모리가 부족한 것으로 보임~~ log file을 생성하여 디버그해본 결과 transmit이 되긴 하는데 지연이 있을 때가 있음
        - 워치 앱에서 콜백함수는 주기적으로 호출을 잘 하는 것 같은데 휴대폰에서 약간의 지연이 생김
    * `for`문은 돌리면 당연히 시간 조절이 안되고 1초 안에 순식간에 많은 프레임을 반복한 뒤 종료됨




---

<br>

<details>
<summary>TODO가 너무 많아서 분할1</summary>
<div markdown = "1">

* 휴대폰이랑 연결이 안되어있어도 데이터는 계속 측정해야할 듯?
    - `phoneConnected`를 기준으로 `exit()`을 결정하면 안될 것 같음
    - `exit()`는 전체 백그라운드 프로세스를 종료하는 것 같음... 
* ~~Background에서 Fit data Simulation~~: 가능함
* Background에서 30초 동안 메모리의 Dictionary 변수에 심박수 기록?
    - 이걸 메모리 말고 [Application Storage](https://developer.garmin.com/connect-iq/api-docs/Toybox/Application/Storage.html)에 저장하고, 5분마다(휴대폰이 연결되어있지 않다면 연결될 때) 보내는 건? (근데 그냥 FIT data를 보낼 수 있으면 가장 좋을 것 같음)
        + [Sensor History](https://developer.garmin.com/connect-iq/api-docs/Toybox/SensorHistory.html#getHeartRateHistory-instance_function)를 가져와서 보내는 테스트 해보기
    - 저장데이터 구현 관련해서는 [여기](https://github.com/miharekar/ForecastLine/blob/master/source/ForecastLine.mc)를 참고해보면 좋을듯
    - Background에서는 메모리를 32KB 밖에 못써서 메모리 저장은 X.. 언제 연결될지도 몰라서 이건 안될듯
    - 그러면 `phoneConnected` & `5분 지남` 을 기준으로 `transmit()`을 실행
* 기록된 변수를 휴대폰으로 보냄
* 휴대폰으로 보낸 뒤 해당 변수 초기화
    - 초기화할 때 Key로 사용한 값도 초기화

</div>
</details>


<details>
<summary>TODO: 30초 recording 반복 작업 관련</summary>
<div markdown = "1">

* Background에서 Toybox.Timer 사용 불가
    - 어떻게 30초동안 특정 센서 데이터를 일정 간격으로 수집할 것인가?
```
Error: Permission Required
Details: Module 'Toybox.Timer' not available to 'Background'
```

* Sensor History(혹은 ActivityMonitor)로 지난 Sensor 기록 받아오기
    - `while(iter != null) iter = iter.next()`이거하면 익셉션남 왜지?
    - [Sensor Core Topics](https://developer.garmin.com/connect-iq/core-topics/sensors/)에서 `Sensor.registerSensorDataListener()`를 이용해보는 건 어떨지... → 메모리 초과

* FIT 파일 접근하는 방법?

* 현재 시간을 받아와서([참고](https://youtu.be/DPt7rkVZG1s?t=838))
    - oldest 시간보다 30초가 초과되지 않았다면 데이터 수집
    - oldest 시간보다 30초 초과되면 데이터 수집 멈춤
    - oldest 시간의 초기화 업데이트 주기: 새롭게 이벤트가 시작될때마다? < 이걸 어떻게 맞추지?

* 생각해보니까 Activity Recording이나... 암튼 따로 기록해서 넘길 순 없나

</div>
</details>



<br>

---

# References
* <https://developer.garmin.com/connect-iq/api-docs/Toybox/Background.html#registerForTemporalEvent-instance_function>
* <https://developer.garmin.com/connect-iq/api-docs/Toybox/System/ServiceDelegate.html>
* <https://developer.garmin.com/connect-iq/api-docs/Toybox/Application/AppBase.html#getServiceDelegate-instance_function>  

* 수행 가능한 작업 정리: <https://developer.garmin.com/connect-iq/core-topics/backgrounding/>
* Background의 Data ↔ Process의 Data 관련: <https://developer.garmin.com/connect-iq/connect-iq-faq/how-do-i-create-a-connect-iq-background-service/>
* Background Process ↔ Foreground Process: [Appdata를 이용해서 통신하는 방법 뿐?](https://forums.garmin.com/developer/connect-iq/f/discussion/284157/can-i-force-a-running-background-to-quit-when-the-mainapp-starts-up)

* How to debug garmin watch apps: <https://forums.garmin.com/developer/connect-iq/w/wiki/4/new-developer-faq#debugging>
* transmit Queue full: <https://forums.garmin.com/developer/connect-iq/f/discussion/5135/comm-transmit---communications-transmit-queue-full-error-in-comm-callback>