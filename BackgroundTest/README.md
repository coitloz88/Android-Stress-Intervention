# BackgroundTest
목표: Background(앱 실행 X)에서 5분에 한번씩 측정한 심박수를 휴대폰으로 보냄  

* 심박수 전송 성공
* Accel Data의 경우 센서 데이터 항목만 바꿔서 보내면 될 것 같음

## TODO
* Background에서 Toybox.Timer 사용 불가
    - 어떻게 30초동안 특정 센서 데이터를 일정 간격으로 수집할 것인가?
```
Error: Permission Required
Details: Module 'Toybox.Timer' not available to 'Background'
```

* ~~Background에서 Fit data Simulation~~: 가능함
* Background에서 30초 동안 메모리의 Dictionary 변수에 심박수 기록?
* 기록된 변수를 휴대폰으로 보냄
* 휴대폰으로 보낸 뒤 해당 변수 초기화
    - 초기화할 때 Key로 사용한 값도 초기화

## References
* <https://developer.garmin.com/connect-iq/api-docs/Toybox/Background.html#registerForTemporalEvent-instance_function>
* <https://developer.garmin.com/connect-iq/api-docs/Toybox/System/ServiceDelegate.html>
* <https://developer.garmin.com/connect-iq/api-docs/Toybox/Application/AppBase.html#getServiceDelegate-instance_function>