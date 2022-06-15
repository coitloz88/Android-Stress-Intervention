# Current process
* HRV가 `MIN_HRV` 이하일 경우 피드백 메시지 화면을 출력하는데, 이때 다시 본래 화면으로 돌아가는 것은 current time + 3초 정도 간격으로 설정해볼 것
    - 관련: `onUpdate()` 호출 주기?
    - 저전력모드에서는 호출 주기가 좀 더 긴 것 같음(피드백 화면이 빨리 안꺼짐..)


# TODO
* 피드백 화면과 동시에 [진동 기능](https://developer.garmin.com/connect-iq/api-docs/Toybox/Attention.html#vibrate-instance_function) 추가할 것

# More Information

* <https://elitehrv.com/heart-rate-variability-vs-heart-rate>
