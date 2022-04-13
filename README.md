# Garmin Project
   
## First Project
[Garmin Watch] WatchUI Test App
   
## DataFieldTest
[Garmin Watch fr55] Data Field Test App: Total Step을 표시해주는 앱  
TODO: Data Field를 실제 기기에서 Test하는 방법

## SampleWidget
[Garmin Watch fr55] Widget Test App: fr55에서 위젯 항목에 접속할 경우, Sample이라는 Widget이 나타남  
아직 Widget의 기능은 없으므로 접속은 불가  

## HeartRateTest
[Garmin Watch fr55] 현재 Heart Rate를 디버그 콘솔에 출력해주는 Test Devcie app  
에뮬레이터로 실행하는 경우, 컴파일 후 에뮬레이터가 실행되면 *Simulation → FIT Data → Simulate Data*를 실행해주면 됨

## ConnectIQ Android SDK
[Garmin watch fr55] 휴대폰에서 간단한 텍스트를 시계로 보내거나 시계에서 간단한 텍스트 혹은 현재 심박수를 Transmit()함수로 보냄

## How to use monkey barrel

[(Garmin Developer: Shareable Libraries)](https://developer.garmin.com/connect-iq/core-topics/shareable-libraries/)

**예시: [Log monkey](https://github.com/garmin/connectiq-apps/tree/master/barrels/LogMonkey) 사용하는 방법** 

### 1. Barrel을 Export하는 방법
Manifest file이 앱에 의해 Support되는 모든 제품을 표시하는지 확인한다.  
VS Code 커맨드 창에 *Monkey C: Export Project*를 입력하여 *.barrel* 파일을 생성한다.

### 2. 사용하려는 Barrel을 내 프로젝트에 Include하는 방법

#### 2-1. Barrel을 내 프로젝트에 추가하기

(1) Visual Studio Code에서 *Ctrl + Shift + P*를 눌러 **Configure Monkey Barrles**를 선택해준다.  
(2) **Add Monkey Barrel**을 선택한다.  
(3) 만약 Precompiled *.barrel*파일이 있다면 해당 파일을 선택하고, 아니라면 Barrel 프로젝트의 *.jungle* 파일을 선택한다.  

후자의 경우, Barrel을 추가하려던 프로젝트에 Barrels.jungle 파일이 추가된다.
```
LogMonkey = [C:\Users\...\LogMonkey\monkey.jungle]
base.barrelPath = $(base.barrelPath);$(LogMonkey)
```
또한, manifest.xml 파일에 다음과 같은 내용이 추가된다.
```
<iq:barrels>
    <iq:depends name="LogMonkey" version="0.0.0"/>
</iq:barrels>
```
#### 2-2. Monkey Barrel을 사용하기
이제 Barrel을 성공적으로 include하였다. 사용하려는 *.mc* 코드 파일에서 다음과 같이 입력해주자.
```
using LogMonkey as Log;
...

class Sample{
    ...
    function sampleFunction(){
        //디버그 콘솔 출력
		//System.println("mStepsSessionCorrected: " + mStepsSessionCorrected.toString());
		//System.println("mStepsLapCorrected: " + mStepsLapCorrected.toString() + "\n");

		//Log Monkey barrel을 추가하여 Log를 디버그 콘솔에 출력
		Log.Debug.logVariable("FitContributions.mc/compute():", "mStepsSessionCorrected", mStepsSessionCorrected);
		Log.Debug.logVariable("FitContributions.mc/compute():", "mStepsLapCorrected", mStepsLapCorrected);
    }
}
```
