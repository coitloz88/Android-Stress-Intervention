<div align = "center">

# Connecting Android App to Garmin Watch device

나를 위한 kotlin 문법 정리: [공식 사이트](https://kotlinlang.org/docs/basic-syntax.html)

</div>

<details>
<summary>Previous process</summary>
<div markdown = "1">

## How to import monkeybrains-sdk-release.aar 
### Errors
* Error 1
```
Execution failed for task
Execution failed for task ':app:javaPreCompileDebug'.
> Could not resolve all files for configuration ':app:debugCompileClasspath'.
   > Failed to transform file '...' to match attributes {artifactType=android-classes, org.gradle.usage=java-api}
      > Execution failed for AarToClassTransform: ...
```

* Error 2
```
Caused by: org.gradle.api.internal.artifacts.ivyservice.DefaultLenientConfiguration$ArtifactResolveException: Could not resolve all files for configuration ':app:debugRuntimeClasspath'.
```

다른 안드로이드 프로젝트는 잘 빌드됐으며, [기존에 클론받은 프로젝트](https://github.com/garmin/connectiq-android-sdk/tree/main/Comm%20Android)의 `implementation("com.garmin.connectiq:monkeybrains:1.0.2")` 부분에서 문제가 생기는 것 같았다.
classpath 설정, sdk version, 등등 여러가지를 시도해보았는데 다음과 같은 방법을 통해 해결했다.

### Solution
`build.gradle.kts`의 `dependencies` 부분을 다음과 같이 수정한다.
```
implementation(files("monkeybrains-sdk-release.aar가 있는 절대 경로(로컬 경로)"))
```

## TODO
1. 안드로이드에서 Garmin Watch로 메시지 보내는 기능 및 화면은 없어도 괜찮을 듯
2. 대화상자에서 Watch에서 받은 메시지를 표시하는 것이 아니라 다른 activity가 실행되며 받은 메시지를 보여주기  
    혹은 기존의 `activity_device.xml`의 구성에 1.을 적용할 것
    ```
    빈 화면에서 새로운 메시지를 받을 때마다 이전 메시지를 지우지 않고 새롭게 메시지를 업데이트  
    (기존 메시지 += 새로운 메시지)
    ```
3. 프로젝트 쪼개기... 

</div>
</details>


<details>
<summary>Current process</summary>
<div markdown = "1">

* `DeviceActivity.kt`의 `COMM_WATCH_ID`을 바꿔서 내가 만든 Garmin Watch 앱과 연결 가능함

* `listenByMyAppEvents()`에서
    1. string을 파싱하는 함수(`parseSensorData()`)가 호출되므로, 해당 `parseSensorData()`에서 센서 데이터 string을 key value에 따라 [파싱](https://hanyeop.tistory.com/304)한 후 리턴하고, 
    2. `isHighHeartRateInterval()`에 파싱한 데이터를 넘겨주면 해당 함수에서 heartRateInterval이 일정치를 넘었는지 판단한 뒤,
    3. 필요한 경우 `giveFeedBack()`에서 워치 앱을 열고 앱이 foreground로 넘어오면 피드백 메시지를 보냄

* 안드로이드 앱에서 메시지를 보내기 전 워치 앱을 열고 메시지를 보내는 것까지 테스트 완료

* 가민 워치에서 Accel data는 보내지 말고, heart rate interval만 보내고 해당 데이터를 파싱해서 `ArrayList`에 넣는 방법도 고려
   
</div>
</details>

