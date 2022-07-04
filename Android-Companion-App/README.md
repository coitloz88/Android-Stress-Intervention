# Stress-Intervention 앱 구조

## Activity

1.	`MainActivity`
    * 기존 코드 거의 그대로 사용
    * 연결 가능한 디바이스 목록 출력
    * 단, 해당 디바이스를 클릭하는 경우 기존 앱의 `DeviceActivity`가 아닌 `BgService`가 시작됨

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
    <details>
    <summary>TODO</summary>

    * foreground 서비스
        - foreground 서비스를 돌리기 위해서는 기본적으로 알림이 계속 떠있어야 함
        - 특정 기준을 넘는 경우 알림 외에 진동 기능 등 추가
    * receive IBI data from garmin smart watch
    * save data in Room DB
    * 필요한 경우 피드백 알림 (→ 터치 시 `InterventionActivity`로 넘어가도록) 
    
    </details>

    <details>
    <summary>Current issues & fix</summary>

    1. issues
        - 서비스 강제 종료시(버튼을 눌러 종료)
        ```
        E/BgService: com.garmin.android.connectiq.exception.InvalidStateException: SDK not initialized
        ```
        - 서비스 강제 종료 후 재시작시
        ```
        E/BgService: ConnectIQ is not in a valid state
        ```
        아예 앱을 삭제 후 시작하는 경우는 exception없이 작동

    2. fix
        * `connectIQ.shutdown()`이 뭔가 작동이 제대로 되지 않음 
             - 본래 MainActivity의 context를 받아 종료하는데, 그럴 경우 MainAcitivity의 `setOnClickListener`가 모두 수행된 뒤 BgService가 종료되기 때문에 SDK를 찾지 못함
             - MainActivity를 구성할때 SDK를 초기화하므로(그러지 않고 Service에서 초기화하면 맨 처음 연결할 Device 선택이 불가) 종료 후 재시작시 SDK가 초기화 되지 않음
             - 일단 현재는 `connectIQ.shutdown()`을 주석 처리
    
    </details>

# 기능

- [X] Garmin Watch와 foreground service에서 연결
- [X] notification창 클릭 시 intervention activity 실행
- [X] Main Activity의 **연결된 기기** 혹은 **버튼**으로 foreground service 실행 및 멈춤 가능
- [ ] Garmin Watch에서 주기적으로 데이터 받기(이벤트 등록)
- [ ] 메모리 누수 확인
- [ ] SDK 초기화 확인
