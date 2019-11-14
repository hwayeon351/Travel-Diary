# Travel-Diary

Rayout/Activity
-------------------------------------------------------------------------------------
Intro Interface
* 캘린더
* 지도
* 일기피드

Map Interface
* 1시간 간격으로 트래킹 한 위치 지도에 표시 -> 노란색
* 맵 화면을 띄울시 현재 위치 지도에 표시 -> 카메라
* 현재위치 기록 -> 빨간색
* 노란핀/빨간핀 클릭시 기록아이콘, 휴지통아이콘 표시
* 노란핀/빨간핀에 기록 아이콘이 눌렸을 시 SaveDiary Interface로 이동
* Diary가 저장되면 노란핀/빨간핀을 파란핀으로 바뀜
* 파란핀의 휴지통 아이콘을 눌렀을 시 핀 삭제(빨간핀의 경우 Diary정보와 함께 삭제)
* 하루가 지나면 저장된 핀과 다이어리 정보가 캘린더에 저장되고 Map Interface는 초기화 된다.

AddDiary Interface
* EDIT 버튼 눌렀을 시 갤러리/카메라와 연동하여 사진 등록
* 메모
* 장소명
* 날짜/시간
* SAVE/BACK 버튼
    - SAVE 버튼 눌렀을 시 저장되었습니다 token발생 후 업데이트된 지도화면 띄우기
    - BACK 버튼 눌렀을 시 Map Interface로 되돌아가기

Calendar Interface
* 날짜 선택하면 그 날 저장된 핀 데이터가 반영된 지도 표시
* 핀 누르면 저장된 다이어리 띄움

Diary Interface
* 저장된 모든 다이어리가 피드 형식으로 띄워짐
* 각 피드는 날짜와 시간이 표기되어있음

DATABASE
---------------------------------------------------------------------------------------
DIARY
{date TEXT
 time TEXT
 img_path TEXT
 place TEXT
 memo TEXT
 PRIMARY KEY(date,time)}
