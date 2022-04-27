# BackgroundTest
목표: Background(앱 실행 X)에서 5분에 한번씩 측정한 심박수를 휴대폰으로 보냄  

* 심박수 전송 성공
* ~~Accel Data의 경우 센서 데이터 항목만 바꿔서 보내면 될 것 같음~~ 함수 `getAcceleration()` 추가함
* 센서 데이터를 받지 못하는 경우 일괄적으로 `-1` 전송

<details>
<summary>TODO</summary>
<div markdown = "1">

* Background에서 Toybox.Timer 사용 불가
    - 어떻게 30초동안 특정 센서 데이터를 일정 간격으로 수집할 것인가?
```
Error: Permission Required
Details: Module 'Toybox.Timer' not available to 'Background'
```

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

* Sensor History(혹은 ActivityMonitor)로 지난 Sensor 기록 받아오기
    - `while(iter != null) iter = iter.next()`이거하면 익셉션남 왜지?
    - [Sensor Core Topics](https://developer.garmin.com/connect-iq/core-topics/sensors/)에서 `Sensor.registerSensorDataListener()`를 이용해보는 건 어떨지... → 메모리 초과
* FIT 파일 접근하는 방법?

<br>

---

## References
* <https://developer.garmin.com/connect-iq/api-docs/Toybox/Background.html#registerForTemporalEvent-instance_function>
* <https://developer.garmin.com/connect-iq/api-docs/Toybox/System/ServiceDelegate.html>
* <https://developer.garmin.com/connect-iq/api-docs/Toybox/Application/AppBase.html#getServiceDelegate-instance_function>

* 수행 가능한 작업 정리: <https://developer.garmin.com/connect-iq/core-topics/backgrounding/>
* Background의 Data ↔ Process의 Data 관련: <https://developer.garmin.com/connect-iq/connect-iq-faq/how-do-i-create-a-connect-iq-background-service/>