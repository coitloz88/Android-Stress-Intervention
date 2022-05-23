# BgGetMessage
Background Event가 실행되고 있을 때, IBI 값이 일정 수치를 넘어가면 앱을 foreground로 불러오고 열어서 호흡하라는 메시지를 띄움


<details>
<summary>Previous Process</summary>
<div markdown = "1">

* `BgGetMessageApp.mc`의 `onPhone()`에서 휴대폰 메시지(String)를 받고 있음

* 기존 Garmin 예제는 일단 foreground고...
    - 휴대폰에서 메시지를 받으면 변수 String에 파싱해서 담아줌
    - foreground 화면 View에 받은 텍스트를 보여주고 있으나, 메시지를 받는 함수를 Background App... 혹은 `onTemporalEvent()`에 구현한 뒤 받은 텍스트를 판별하는 코드를 짜보자

→ 휴대폰 메시지를 받는 건 foreground에서만 가능한 동작으로 추정됨(아닐 수도 있음ㅜ)

~~[안드로이드](https://github.com/coitloz88/connectiq-android-sdk/tree/main/Comm%20Android)의 `ConnectIQ.IQOpenApplicationListener()`와 `openMyApp()`을 사용~~

~~ - 워치에서 수집된 센서 데이터가 휴대폰 앱으로 넘어오면, 이런저런 연산 후 워치 앱으로 피드백을 주어야 한다고 판단하는 경우(앱을 열어서 메시지를 보낼 필요 O), 일단 워치에서 앱을 열고 foreground가 실행되면 메시지를 띄운다.~~

* 그런데 어차피 foreground로 앱이 켜지는 거라면 foreground에서 다시 데이터를 수집하면 되니까 굳이 background data를 넘길 필요는 없지 않나? → IBI 데이터를 넘길 필요는 없고, 호흡하라는 메시지를 띄워야하는지 아닌지만 판단하면 될듯

* 애니메이션 기능 자체는 [다른 가민 워치](https://developer.garmin.com/connect-iq/api-docs/Toybox/WatchUi/AnimationDelegate.html)에서 지원하기는 하나 forerunner 55에서는 지원되지 않는 기능..

</div>
</details>

<details>
<summary>Current Process</summary>
<div markdown = "1">


### Emulator
* `BACKGROUND_REPONSE_CODE`가 정상적으로 넘어옴
* IBI값이 잘 측정됨
* emulator에서는 `requestApplicationWake()`를 테스트 할 수 없는 것으로 보임([참고](https://forums.garmin.com/developer/connect-iq/f/discussion/252923/requestapplicationwake-in-onrecive))


### 실제 Device
*  IBI sample이 측정되지 않음...
* `requestApplicationWake()`는 제대로 작동하지 않는데 `saveBackgroundData()`와 `Background.exit()`는 잘 작동하는 것 같음(why..)
* 조건문 없이 실행하는 경우 피드백 화면(+이미지)가 정상적으로 출력됨

</div>
</details>


