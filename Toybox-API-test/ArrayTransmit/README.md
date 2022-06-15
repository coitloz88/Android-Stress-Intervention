## ArrayTransmit
가장 최근의 HeartRate 3개가 담긴 Dictionary를 휴대폰으로 보냄  

### DONE
* HeartRate 수집
* 데이터 셋 3개를 하나의 Object(Dictionary) 모으기
* Array가 아니라 Dictionary로 데이터 타입 변경

### TODO
* ~~휴대폰 모바일 앱에서 정보를 받는 Garmin Watch Device App ID를 발급 받는 방법?~~
    - 앱 ID가 Garmin에서 개발한 Comm Watch 샘플과 달라서 Transmit 자체를 인식하지 못하는 것 같음
    - **해결**:  `manifest.xml`에서 확인 가능
* dictionary를 보낼때, time count를 key로 휴대폰 앱에 보내주는데, INT_MAX 초과시 어떻게?
    - 현재 시간을 key로 저장(String)
* Garmin에서 보낸 Data 저장
    - `.fit`파일을 바로 받을 수 있으면 좋을텐데
    - 시계에는 최근 n시간의 센서(걸음수, 심박수) 데이터가 저장되며 용량이 초과되면 삭제되는 것 같으나 정확하지는 않음