# 여행 다이어리 Android Application
 1시간 주기로 사용자의 위치를 기록하고, 해당 위치에 다이어리를 작성하는 여행자를 위한 애플리케이션</br>

<img src="./Images/travel_diary0.png" width="200px" height="400px" title="img" alt="img"></img>
<img src="./Images/travel_diary1.png" width="200px" height="400px" title="img" alt="img"></img>
<img src="./Images/travel_diary2.png" width="200px" height="400px" title="img" alt="img"></img></br>
<img src="./Images/travel_diary3.png" width="200px" height="400px" title="img" alt="img"></img>
<img src="./Images/travel_diary4.png" width="200px" height="400px" title="img" alt="img"></img>
<img src="./Images/travel_diary5.png" width="200px" height="400px" title="img" alt="img"></img></br>

## 주 기능

### 지도
* 1시간 간격으로 트래킹 한 위치 -> 노란색 마커로 표시
* 현재 위치 -> 빨간색 마커로 표시
* 다이어리 저장된 위치 -> 파란색 마커로 표시
* 노란핀/빨간핀 클릭시 -> 다이어리 기록 또는 삭제 기능 선택 가능

### 일기
* 갤러리/카메라와 연동하여 사진 등록
* 장소명, 날짜/시간, 메모를 작성하여 다이어리 저장
* 저장한 모든 다이어리를 피드 형식으로 확인

### 캘린더
* 날짜 선택 -> 해당 날짜에 저장된 마커를 지도에 띄움
* 특정 마커 클릭 -> 저장된 다이어리 확인 및 수정


## DATABASE
DIARY
{date TEXT
 time TEXT
 LAT TEXT
 LNG TEXT
 img_path TEXT
 place TEXT
 memo TEXT
 PRIMARY KEY(date,time)}
 
