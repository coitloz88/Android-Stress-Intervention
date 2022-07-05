# Stress-Intervention 앱 구조

## Activity

1.	`MainActivity`
    * 기존 코드 거의 그대로 사용
    * 연결 가능한 디바이스 목록 출력
    * 단, 해당 디바이스를 클릭하는 경우 기존 앱의 `DeviceActivity`가 아닌 `BgService`가 시작됨

<img src="https://user-images.githubusercontent.com/88723775/176818519-050734d1-56eb-48cf-982b-caacbecf05c3.png  width="50%"/>

2. `InterventionActivity`
    * 기존 코드 그대로 사용

<img src="https://user-images.githubusercontent.com/88723775/176818513-6a90b483-285b-4824-b6a1-2b169fd61574.png  width="50%"/>


3. `SensorActivity`

    * Recyclerview를 추가함: SensorFactory.kt에 원하는 센서 항목을 추가하고,
    * SensorActivity.kt의 `onItemClick()`에 클릭시 무엇을 실행할지 작성

<img src="https://user-images.githubusercontent.com/88723775/177260762-ec9baa89-07e0-4f30-879b-3a88b857941f.png  width="50%"/>



## Service

1. `BgService`
    <details>
    <summary>TODO</summary>

    * merge

    </details>

    <details>
    <summary>issues & fix</summary>

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
        * `SharedPreferences`에 서비스 구동 여부를 저장하면 강제 종료시 핸들링 불가
            - [Service 구동 확인 코드](https://stackoverflow.com/questions/600207/how-to-check-if-a-service-is-running-on-android?answertab=trending#tab-top) 추가
    
    </details>

# 기능

- [X] Garmin Watch와 foreground service에서 연결
- [X] notification창 클릭 시 intervention activity 실행
- [X] Main Activity의 **연결된 기기** 혹은 **버튼**으로 foreground service 실행 및 멈춤 가능
- [X] Garmin Watch에서 주기적으로 데이터 받기(이벤트 등록)
- [X] SensorActivity 합치기
- [ ] 메모리 누수 확인
- [ ] SDK 초기화 확인
