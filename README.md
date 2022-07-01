# Stress-Intervention
* Stress feedback derived from HRV provided in real time  
* Platform: Garmin Smart Watch(fr55), Android

## Android Companion App
Garmin Watch에서 전송된 IBI 데이터 세트를 HRV로 변환한다.  
데이터를 받은 시간, 계산된 HRV를 로컬 데이터베이스(Room DB)에 저장한다.  
HRV값이 정상 수치를 벗어나는 경우 워치 앱을 켜고 피드백 메시지를 전송한다.  
(현재 수정중)

## Garmin Watch Intervention App
**BackgroundTest**  
Watch Device App으로, 실행하면 이후 워치 백그라운드에서 계속 IBI값을 모으고 모은 값을 Android Companion App으로 전송한다. (5분에 한번, 30초씩)  
휴대폰에서 피드백 메시지가 전송된 경우 워치 앱이 켜질 수 있으며, 켜는 경우 피드백 메시지가 출력된다.


* IBI Collecting Test Result: <https://github.com/coitloz88/Garmin-IBI-Test-Result>
* 220616 Merge Repository
