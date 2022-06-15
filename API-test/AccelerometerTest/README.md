## AccelerometerTest
[Garmin watch fr55] Device App: 설정한 `timeUnit`마다 현재 Accelerometer Sensor data를 받아와서 디버그 콘솔에 출력함
에뮬레이터로 실행하는 경우, 컴파일 후 에뮬레이터가 실행되면 `Simulation → FIT Data → Simulate Data`를 실행해주면 됨
### TODO
1. 샘플에 있던 안드로이드→워치로 문자열 보내기 기능 삭제
2. 안드로이드에서 워치로부터 데이터를 받으면 팝업 대화 상자가 아닌 새로운 `Activity`가 열리면서 그동안 받은 정보가 화면에 기록됨
3. 2.에서 받은 정보를 자체 DB에 따로 저장?
    - `SharedPreferneces`에 저장하기([참고](https://developer.android.com/training/data-storage/shared-preferences?hl=ko))
    - Room 사용하기([참고](https://developer.android.com/training/data-storage/room?hl=ko))