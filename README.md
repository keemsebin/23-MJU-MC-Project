# 23-MJU-MC-Project

**splashActivity**

<img src="https://github.com/keemsebin/23-MJU-MC-Project/assets/108217858/11c1500d-1bfb-48d4-8e42-492e075352c7" width=30%>

**onboardingActivity**
- 시간에 맞게 푸쉬알림 서비스를 제공한다. 

<img src="https://github.com/keemsebin/23-MJU-MC-Project/assets/108217858/84ff3145-6794-4c46-92bb-a66c5e7ac7ec" width=30%>

<img src="https://github.com/keemsebin/23-MJU-MC-Project/assets/108217858/bd8aa849-d18a-4918-ae41-b2f4e5e87915" width=30%>


**mainActivity**
- 해당 날짜에 기록이 되어 있으면 표시를 해준다.
<img src="https://github.com/keemsebin/23-MJU-MC-Project/assets/108217858/1fed7027-2d95-4437-9d27-f594e361db0f" width=30%>

<img src="https://github.com/keemsebin/23-MJU-MC-Project/assets/108217858/29fa3318-19b9-45c7-bfb8-2beaf21e7c59" width=30%>


**cameraActivity**

<img src="https://github.com/keemsebin/23-MJU-MC-Project/assets/108217858/ca4f77db-2cc3-43a1-b1b7-13979201b6b6" width=30%>

<img src="https://github.com/keemsebin/23-MJU-MC-Project/assets/108217858/5ce968cf-5b05-4bab-9236-2dc54e2cc79b" width=30%>


**WriteActivity**

<img src="https://github.com/keemsebin/23-MJU-MC-Project/assets/108217858/790f335f-3ccd-4e62-8335-25099722170a" width=30%>

**myActivity**
- 오늘의 한마디에 따라 감정을 분석하여 이모지로 나타내준다. 

<img src="https://github.com/keemsebin/23-MJU-MC-Project/assets/108217858/d3b45152-df86-4284-b025-c3c63e97466f" width=30%>

<img src="https://github.com/keemsebin/23-MJU-MC-Project/assets/108217858/11106a43-6b83-4b24-a682-5218aa71083d" width=30%>


### **Dependencies**

---

**Calendarview**

```kotlin
implementation 'com.github.prolificinteractive:material-calendarview:2.0.1'
```

**CameraX**

```kotlin
def camerax_version = "1.0.0-rc03"
implementation "androidx.camera:camera-core:${camerax_version}"
implementation "androidx.camera:camera-camera2:${camerax_version}"
implementation "androidx.camera:camera-lifecycle:${camerax_version}"
implementation "androidx.camera:camera-view:1.0.0-alpha22"
implementation "androidx.camera:camera-extensions:1.0.0-alpha22"
```

**Naver Clova Sentiment API**

```kotlin
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
```
