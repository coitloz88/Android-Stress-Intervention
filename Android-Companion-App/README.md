# Stress-Intervention 앱 구조


## Activity

1.	`MainActivity`
    * 기존 코드 거의 그대로 사용
    * 연결 가능한 디바이스 목록 출력
    * 단, 해당 디바이스를 클릭하는 경우 기존 앱의 `DeviceActivity`가 아닌 `GetDataService`가 시작됨

<center>

![Main Activity](https://user-images.githubusercontent.com/88723775/176818519-050734d1-56eb-48cf-982b-caacbecf05c3.png)
Main Activity의 구성

</center>


2. `InterventionActivity`
    * 기존 코드 그대로 사용

<center>

![Intervention Activity](https://user-images.githubusercontent.com/88723775/176818513-6a90b483-285b-4824-b6a1-2b169fd61574.png)
Intervention Activity의 구성

</center>


## Service

1. `BgService`
    * foreground 서비스
        - foreground 서비스를 돌리기 위해서는 기본적으로 알림이 계속 떠있어야 함
        - 특정 기준을 넘는 경우 알림 외에 진동 기능 등 추가
    * receive IBI data from garmin smart watch
    * save data in Room DB
    * 필요한 경우 피드백 알림 (→ 터치 시 `InterventionActivity`로 넘어가도록) 