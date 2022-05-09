* `BgGetMessageApp.mc`의 `onMail()` 혹은 `onPhone()`에서 휴대폰 메시지(String)를 받고 있음

* 기존 Garmin 예제에서는 foreground에서 화면 View에 받은 텍스트를 보여주고 있으나, 메시지를 받는 함수를 Background App... 혹은 `onTemporalEvent()`에 구현한 뒤 받은 텍스트를 판별하는 코드를 짜보자