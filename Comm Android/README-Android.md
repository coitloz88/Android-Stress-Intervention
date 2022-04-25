## import monkeybrains-sdk-release.aar 
### Error
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

---
## Now...
* Comm Android: [ArrayTransmit](https://github.com/coitloz88/Garmin-Project/tree/master/ArrayTransmit) 앱에서 `transmit`하는 정보를 받아옴

---

## TODO
1. 안드로이드에서 Garmin Watch로 메시지 보내는 기능 및 화면은 없어도 괜찮을 듯
2. 대화상자에서 Watch에서 받은 메시지를 표시하는 것이 아니라 다른 activity가 실행되며 받은 메시지를 보여주기  
    혹은 기존의 `activity_device.xml`의 구성에 1.을 적용할 것
    ```
    빈 화면에서 새로운 메시지를 받을 때마다 이전 메시지를 지우지 않고 새롭게 메시지를 업데이트  
    (기존 메시지 += 새로운 메시지)
    ```
3. 프로젝트 쪼개기... 