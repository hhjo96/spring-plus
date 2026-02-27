# SPRING PLUS

## 필수 기능
LV1.
1. @Transactional(readonly=true) 인 경우 save 가 되지 않음.
2. 유저 엔티티에 nickname 추가 및 jwt에 nickname 추가
3. todo list조회시 수정일(범위), weather 조건 검색 기능 추가
- 작동 확인 캡쳐
![img.png](img.png) ![img_1.png](img_1.png)
4. 테스트: 예외가 발생하는 테스트에 isOk로 작성하여 200을 예측하였으나 400이 돌아와 실패하였음
```
.andExpect(status().isOk())
```
