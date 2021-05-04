# 지하철 미션

## 1단계 기능 요구 사항

### 도메인
1. 역 
   - [x] 역 입력 기능
     - [x] [예외] 역 이름이 중복되면 예외
     - [x] [예외] 역 이름은 20자를 초과하면 예외
     - [x] 띄어쓰기가 2개가 연속되었을 경우 1개로 줄여주는 기능  
     - [ ] [예외] 영어, 한글, 숫자, 일부 특수문자만(띄어쓰기 1개, 괄호, ·)이외의 문자를 입력하면 예외
     - [x] 첫 글자와 마지막 글자에 공백이 들어가면 trim하는 기능
   - [ ] 역 삭제 기능
     - [ ] [예외] 노선에 등록되어 있는 역이라면 예외
2. 노선
   - [ ] 노선 생성 기능
     - [ ] [예외] 노선 명이 겹치면 예외
     - [ ] [예외] 노선 색상이 겹치면 예외
     - [ ] [예외] 노선 명이 20자를 넘어가면 예외
     - [ ] 노선 명이 띄어쓰기가 2개 이상일 경우 1개로 줄여주는 기능
     - [ ] [예외] 노선 명이 영어, 한글, 숫자, 일부 특수문자만(띄어쓰기 1개, 괄호, ·)이외의 문자를 입력하면 예외
     - [ ] 노선명에 첫 글자와 마지막 글자에 공백이 들어가면 trim하는 기능   
   - [ ] 노선 조회 기능
     - [ ] 개별 노선 조회 기능
       - [ ] id, 이름, 색상, 역목록을 반환해야한다.
     - [ ] 모든 노선 조회 기능
       - [ ] id, 이름, 색상, 역목록을 포함한 노선 목록을 반환해야한다.
   - [ ] 노선 수정 기능
     - [ ] 노선 이름을 수정할 수 있다.
        - [ ] [예외] 노선 생성기능의 노선명 유효성검사를 통과해야한다.
     - [ ] 노선 색상을 수정할 수 있다.
        - [ ] [예외] 노선 색상이 겹치면 예외
   - [ ] 노선 삭제 기능
    
### 테스트
1. End to End테스트를 구현해야 한다.
