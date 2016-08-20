# SimpleBarrier
쓸데 없는 플러그인입니다.<br>
투명 배리어를 생성해줍니다!<br>
배리어 밖에서 안으로 진입이 불가합니다. 단, 안에서 밖으로 또는 안에서 안으로는 자유롭습니다.<br>
기본적으로 오피는 배리어를 무시합니다.<br>

배리어 플러그인에 포함된 명령은 이렇게 있습니다. 기본적으로는 오피만 사용 가능합니다.<br>

    /barrier pos1 - 월드에딧식 배리어 영역 선택
    /barrier pos2 - 월드에딧식 배리어 영역 선택
    /barrier add <이름> - 배리어 설치
    /barrier del <이름> - 배리어 제거
    /barrier delall - 배리어 모두 제거
    /barrier list - 배리어 리스트 덤프; 구현되지 않았습니다.

배리어는 다음 경로에 저장됩니다.<br>
`plugins/SimpleBarrier/barriers`<br>
배리어들은 자바의 serialization 프로토콜을 채택하여 파일에 저장되고 있습니다.

또한 퍼미션은 두가지가 있습니다.<br>
`barrier.exec` 퍼미션은 모든 배리어 관련 명령어에 접근 가능하도록 합니다.<br>
`barrier.thrw` 퍼미션은 배리어를 무시할수 있게 합니다.<br>

피드백: boomingsky@naver.com<br>
라이센스: ~~따위 없다~~
