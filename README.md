## WeMo (직장인 대상 모임 서비스)


### [WeMo 바로가기](https://we-mo.store)
[![image](https://github.com/user-attachments/assets/582458d2-0310-42b3-a7fa-52cd02ca7434)](https://we-mo.store)

## 목차
1. [서비스 소개](#-서비스-소개)
2. [주요 기능](#-주요-기능)
    - [모임 및 일정 탐색](#1-모임-및-일정-탐색)
    - [유저 기능](#2-유저-기능)
    - [마이페이지](3-마이페이지)
3. [프로젝트 기간 및 일정](#프로젝트-기간-및-일정)
4. [협업 및 커뮤니케이션](#협업-및-커뮤니케이션)
5. [프로젝트 환경](#%EF%B8%8F-프로젝트-환경)
6. [서비스 아키텍처](#-서비스-아키텍처)
7. [ERD](#%EF%B8%8F-erd)
8. [API 명세](#-api-명세)
9. [기술 선택 및 이유](#-기술-선택-및-이유)
10. [트러블 슈팅](#-트러블-슈팅)
11. [고민한 내용](#-고민한-내용)
12. [디렉토리 구조](#%EF%B8%8F-디렉토리-구조)

## 👫 서비스 소개
**"WeMo, ‘우리(We)’가 모여 더 즐거운 모임을 만들어가는 곳!"**
WeMo는 바쁜 일상 속에서도 쉽고 즐겁게 모임을 탐색하고 참여할 수 있는 서비스입니다.
유저는 다양한 테마의 모임을 개설하거나 참여할 수 있으며, 일정 후기를 공유하며 더 나은 경험을 만들어갈 수 있습니다.

<br>

## 🎯 주요 기능

### 1. **모임 및 일정 탐색**
- **모임 및 일정 목록 조회**:  
  다양한 모임을 카테고리별로 탐색할 수 있습니다. 모임 유형에 따라 정렬하거나 필터링 기능을 사용해 관심 있는 모임을 쉽게 찾을 수 있습니다.
- **중심 위치 기준 요청 반경 km 이내**:  
  사용자 중심의 위치 기반 검색으로, 요청하는 반경 이내의 모임을 조회할 수 있습니다. 이를 통해 사용자와 가까운 모임을 빠르게 찾을 수 있습니다.

### 2. **유저 기능**
- **모임 신청/취소**:  
  원하는 모임에 참여 신청하고, 필요 시 취소할 수 있습니다.
- **후기 작성**:  
  모임에 참여 후 후기를 남겨 다른 유저와 정보를 공유할 수 있습니다.
- **모임 및 일정 생성**:  
  자신만의 모임을 개설하고, 일정과 관련된 세부 사항을 설정하여 참여자들을 모집할 수 있습니다.
- **찜하기**:  
  관심 있는 모임이나 일정을 찜하여 나중에 다시 확인하거나 참여할 수 있습니다.

### 3. **마이페이지**
- **내 일정 확인**:  
  내가 참여한 모임과 일정을 한눈에 볼 수 있습니다.
- **내 활동 기록**:  
  모임 참여 내역, 후기 작성 내역 등을 확인할 수 있습니다.
- **프로필 관리**:  
  개인 정보를 수정하고, 자신의 활동 내역을 확인할 수 있습니다.
- **찜한 일정 목록 조회**:  
  찜한 모임 및 일정 목록을 확인하고, 이후 참여 여부를 결정할 수 있습니다.

<br>

## 프로젝트 기간 및 일정

### 🗓️ **프로젝트 기간** : 2024.12.23 ~ 2025.02.13

---

### 주요 일정

| 기간               | 작업 내용                        |
|------------------|--------------------------------|
| **12/23 ~ 01/06** | 기획 및 서비스 기능 정리         |
| **01/02 ~ 01/20** | 1차 MVP 개발                    |
| **01/04 ~ 01/07** | ERD 및 API 명세 작업            |
| **01/14**         | 백엔드 서버 배포                |
| **01/14 ~ 01/30** | 기능 수정 및 리팩토링           |
| **01/24 ~ 01/31** | 2차 기획 및 기능 논의           |
| **01/27**         | Github Actions CI 환경 구축    |
| **02/07**         | Blue-Green 배포 및 CD 파이프라인 도입 |
| **02/07 ~ 02/12** | 2차 MVP 개발                    |


<br>

## 협업 및 커뮤니케이션
<details>
<summary>Notion을 활용한 문서화</summary>
<div>
    <img width="1107" alt="Image" src="https://github.com/user-attachments/assets/b35ce9d5-85cd-4c13-a7ac-001127bab811" />
    <img width="1177" alt="Image" src="https://github.com/user-attachments/assets/8d299317-bf28-4289-b22c-579a8e1f8c29" />
</div>

</details>
<details>
<summary>Discord 를 활용한 소통</summary>
<div>
    <img width="1073" alt="Image" src="https://github.com/user-attachments/assets/181588d5-c93b-4623-ba81-a52cae9cd622" />
</div>
</details>

<br>

## 🛠️ 프로젝트 환경

| Category    | Stack                                                                                                        |     Version     |
|:-----------:|:------------------------------------------------------------------------------------------------------------:|:---------------:|
| Runtime     | ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)    |     JDK 17      |
| Framework   | ![Spring Boot](https://img.shields.io/badge/springboot-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) | Spring Boot 3.4 |
| Build Tool  | ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)    |   Gradle 8.12   |
| Database    | ![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)       |    MySQL 8.0    |
| Cache       | ![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)    |    Redis 7.4    |
| Environment | ![Docker](https://img.shields.io/badge/docker-2496ED.svg?style=for-the-badge&logo=docker&logoColor=white)   |   Docker 27.x   |
| Test        | ![JUnit](https://img.shields.io/badge/JUnit-25A162.svg?style=for-the-badge&logo=JUnit5&logoColor=white)     |     JUnit 5     |

<br>

## 🎨 서비스 아키텍처

<img width="800" alt="Image" src="https://github.com/user-attachments/assets/41459e9b-42bd-4b9f-b611-cac6d09edf10" />

<br>

## ⛓️ ERD

![Image](https://github.com/user-attachments/assets/44c8dc1e-bcde-452f-a32d-08f1693fca80)

<br>

## 🧾 API 명세

WeMo의 API 명세를 확인하려면 아래 링크를 참고해주세요.

- 🔗 [**Postman API 문서**](https://documenter.getpostman.com/view/20456478/2sAYXEEe1g)
- 🔗 [**Swagger 문서**](https://api.we-mo.store/swagger-ui/index.html#/)

위 두 가지 문서를 통해 API 요청과 응답 형식을 상세히 확인할 수 있습니다. 🚀

<br>

## ✅ 기술 선택 및 이유

- **SpringSecurity** - <a href="https://github.com/wanted-all-clear/tasty-track/wiki/SpringSecurity-%EB%8F%84%EC%9E%85"> WIKI 이동 </a>
- **AccessToken과 RefreshToken** - <a href="https://github.com/wanted-all-clear/tasty-track/wiki/AccessToken%EA%B3%BC-RefreshToken"> WIKI 이동 </a>

<br>

## 💥 트러블 슈팅

- **순환 참조 오류 문제** - <a href="https://github.com/fesi3/WeMo-BE/wiki/%EC%88%9C%ED%99%98-%EC%B0%B8%EC%A1%B0-%EC%98%A4%EB%A5%98-%EB%AC%B8%EC%A0%9C"> WIKI 이동 </a>
- **Querydsl Like 예약어 문제** - <a href="https://github.com/fesi3/WeMo-BE/wiki/Querydsl-Like-%EC%98%88%EC%95%BD%EC%96%B4-%EC%98%A4%EB%A5%98-%EB%AC%B8%EC%A0%9C"> WIKI 이동 </a>

<br>

## 🤔 고민한 내용

- **이미지 업로드 방식** - <a href="https://github.com/fesi3/WeMo-BE/wiki/%EC%9D%B4%EB%AF%B8%EC%A7%80-%EC%97%85%EB%A1%9C%EB%93%9C-%EB%B0%A9%EC%8B%9D-%EA%B3%A0%EB%AF%BC"> WIKI 이동 </a>
- **토큰 쿠키 만료 시간 설정** - <a href="https://github.com/fesi3/WeMo-BE/wiki/%EC%BF%A0%ED%82%A4-%EB%A7%8C%EB%A3%8C-%EC%8B%9C%EA%B0%84-%EC%84%A4%EC%A0%95-%EB%B0%A9%EB%B2%95"> WIKI 이동 </a>

<br>

## 🗂️ 디렉토리 구조
<details><summary>직관적인 구조 파악과 관리를 위해 <b>도메인형 구조</b>를 채택하였습니다. <b>(더보기)</b></summary>

<h3>🗂️ 전체 구조</h3>

```
src
├── main
│   ├── java
│   │   └── com
│   │       └── wemo
│   │           └── backend
│   │               ├── WeMoApplication.java
│   │               ├── domain
│   │               │   ├── attendance
│   │               │   │   ├── entity
│   │               │   │   │   └── Attendance.java
│   │               │   │   ├── repository
│   │               │   │   │   └── AttendanceRepository.java
│   │               │   │   └── service
│   │               │   │       ├── AttendanceReader.java
│   │               │   │       ├── AttendanceReaderImpl.java
│   │               │   │       ├── AttendanceStore.java
│   │               │   │       └── AttendanceStoreImpl.java
│   │               │   ├── auth
│   │               │   │   ├── IpUtils.java
│   │               │   │   ├── JwtAuthenticationFilter.java
│   │               │   │   ├── JwtTokenProvider.java
│   │               │   │   ├── UserAuth.java
│   │               │   │   ├── UserAuthImpl.java
│   │               │   │   ├── UserDetailsImpl.java
│   │               │   │   ├── UserDetailsServiceImpl.java
│   │               │   │   └── token
│   │               │   │       ├── entity
│   │               │   │       │   └── RefreshToken.java
│   │               │   │       ├── repository
│   │               │   │       │   └── RefreshTokenRedisRepository.java
│   │               │   │       └── service
│   │               │   │           ├── AccessTokenManager.java
│   │               │   │           ├── JwtTokenUtils.java
│   │               │   │           ├── RefreshTokenCleanupScheduler.java
│   │               │   │           └── RefreshTokenManager.java
│   │               │   ├── category
│   │               │   │   ├── entity
│   │               │   │   │   └── Category.java
│   │               │   │   └── repository
│   │               │   │       └── CategoryRepository.java
│   │               │   ├── comm
│   │               │   │   ├── CommUtilService.java
│   │               │   │   └── CommUtilServiceImpl.java
│   │               │   ├── image
│   │               │   │   ├── controller
│   │               │   │   │   └── ImageController.java
│   │               │   │   ├── dto
│   │               │   │   │   └── PresignedUrlResponse.java
│   │               │   │   ├── entity
│   │               │   │   │   └── Image.java
│   │               │   │   ├── repository
│   │               │   │   │   └── ImageRepository.java
│   │               │   │   └── service
│   │               │   │       ├── ImageReader.java
│   │               │   │       ├── ImageReaderImpl.java
│   │               │   │       ├── ImageStore.java
│   │               │   │       ├── ImageStoreImpl.java
│   │               │   │       └── S3Service.java
│   │               │   ├── lightning
│   │               │   │   ├── controller
│   │               │   │   │   └── LightningController.java
│   │               │   │   ├── dto
│   │               │   │   │   ├── LightningCursorPagingResponse.java
│   │               │   │   │   ├── LightningDetailResponse.java
│   │               │   │   │   ├── LightningListResponse.java
│   │               │   │   │   ├── LightningRequest.java
│   │               │   │   │   └── LightningResponse.java
│   │               │   │   ├── entity
│   │               │   │   │   ├── DateType.java
│   │               │   │   │   ├── Lightning.java
│   │               │   │   │   └── LightningType.java
│   │               │   │   ├── repository
│   │               │   │   │   ├── LightningRepository.java
│   │               │   │   │   ├── LightningTypeRepository.java
│   │               │   │   │   └── querydsl
│   │               │   │   │       ├── LightningCursorQueryDsl.java
│   │               │   │   │       └── LightningCursorQueryDslImpl.java
│   │               │   │   └── service
│   │               │   │       ├── LightningReader.java
│   │               │   │       ├── LightningReaderImpl.java
│   │               │   │       ├── LightningService.java
│   │               │   │       ├── LightningServiceImpl.java
│   │               │   │       ├── LightningStore.java
│   │               │   │       ├── LightningStoreImpl.java
│   │               │   │       ├── LightningTypeReader.java
│   │               │   │       └── LightningTypeReaderImpl.java
│   │               │   ├── lightningJoin
│   │               │   │   ├── controller
│   │               │   │   │   └── LightningJoinController.java
│   │               │   │   ├── entity
│   │               │   │   │   └── LightningJoin.java
│   │               │   │   ├── repository
│   │               │   │   │   └── LightningJoinRepository.java
│   │               │   │   └── service
│   │               │   │       ├── LightningJoinReader.java
│   │               │   │       ├── LightningJoinReaderImpl.java
│   │               │   │       ├── LightningJoinService.java
│   │               │   │       ├── LightningJoinServiceImpl.java
│   │               │   │       ├── LightningJoinStore.java
│   │               │   │       └── LightningJoinStoreImpl.java
│   │               │   ├── like
│   │               │   │   ├── controller
│   │               │   │   │   └── LikeController.java
│   │               │   │   ├── entity
│   │               │   │   │   └── Likes.java
│   │               │   │   ├── repository
│   │               │   │   │   └── LikeRepository.java
│   │               │   │   └── service
│   │               │   │       ├── LikeReader.java
│   │               │   │       ├── LikeReaderImpl.java
│   │               │   │       ├── LikeService.java
│   │               │   │       ├── LikeServiceImpl.java
│   │               │   │       ├── LikeStore.java
│   │               │   │       └── LikeStoreImpl.java
│   │               │   ├── meeting
│   │               │   │   ├── controller
│   │               │   │   │   └── MeetingController.java
│   │               │   │   ├── dto
│   │               │   │   │   ├── MeetingCreateRequest.java
│   │               │   │   │   ├── MeetingCreateResponse.java
│   │               │   │   │   ├── MeetingCursorPagingResponse.java
│   │               │   │   │   ├── MeetingDetailResponse.java
│   │               │   │   │   ├── MeetingInfoResponse.java
│   │               │   │   │   ├── MeetingListPlanListResponse.java
│   │               │   │   │   ├── MeetingListResponse.java
│   │               │   │   │   ├── MeetingListResponseV2.java
│   │               │   │   │   ├── MeetingMemberPagingResponse.java
│   │               │   │   │   ├── MeetingPlanListResponse.java
│   │               │   │   │   ├── MeetingPlanPagingResponse.java
│   │               │   │   │   ├── MeetingReviewListResponse.java
│   │               │   │   │   ├── MeetingReviewPagingResponse.java
│   │               │   │   │   └── MeetingUpdateRequest.java
│   │               │   │   ├── entity
│   │               │   │   │   └── Meeting.java
│   │               │   │   ├── repository
│   │               │   │   │   ├── MeetingQueryDsl.java
│   │               │   │   │   ├── MeetingQueryDslImpl.java
│   │               │   │   │   └── MeetingRepository.java
│   │               │   │   └── service
│   │               │   │       ├── MeetingReader.java
│   │               │   │       ├── MeetingReaderImpl.java
│   │               │   │       ├── MeetingService.java
│   │               │   │       ├── MeetingServiceImpl.java
│   │               │   │       ├── MeetingStore.java
│   │               │   │       ├── MeetingStoreImpl.java
│   │               │   │       ├── MeetingUtilService.java
│   │               │   │       └── MeetingUtilServiceImpl.java
│   │               │   ├── meetingMember
│   │               │   │   ├── entity
│   │               │   │   │   └── MeetingMember.java
│   │               │   │   ├── repository
│   │               │   │   │   └── MeetingMemberRepository.java
│   │               │   │   └── service
│   │               │   │       ├── MeetingMemberReader.java
│   │               │   │       ├── MeetingMemberReaderImpl.java
│   │               │   │       ├── MeetingMemberStore.java
│   │               │   │       └── MeetingMemberStoreImpl.java
│   │               │   ├── oauth
│   │               │   │   ├── controller
│   │               │   │   │   └── Oauth2Controller.java
│   │               │   │   └── service
│   │               │   │       ├── Oauth2Service.java
│   │               │   │       └── Oauth2ServiceImpl.java
│   │               │   ├── plan
│   │               │   │   ├── controller
│   │               │   │   │   └── PlanController.java
│   │               │   │   ├── dto
│   │               │   │   │   ├── PlanCreateRequest.java
│   │               │   │   │   ├── PlanCreateResponse.java
│   │               │   │   │   ├── PlanCursorPagingResponse.java
│   │               │   │   │   ├── PlanDetailResponse.java
│   │               │   │   │   ├── PlanListInfo.java
│   │               │   │   │   └── PlanListResponse.java
│   │               │   │   ├── entity
│   │               │   │   │   └── Plan.java
│   │               │   │   ├── repository
│   │               │   │   │   ├── PlanRepository.java
│   │               │   │   │   └── querydsl
│   │               │   │   │       ├── PlanCursorQueryDsl.java
│   │               │   │   │       ├── PlanCursorQueryDslImpl.java
│   │               │   │   │       ├── PlanQueryDsl.java
│   │               │   │   │       └── PlanQueryDslImpl.java
│   │               │   │   └── service
│   │               │   │       ├── PlanReader.java
│   │               │   │       ├── PlanReaderImpl.java
│   │               │   │       ├── PlanService.java
│   │               │   │       ├── PlanServiceImpl.java
│   │               │   │       ├── PlanStore.java
│   │               │   │       └── PlanStoreImpl.java
│   │               │   ├── region
│   │               │   │   ├── controller
│   │               │   │   │   └── RegionController.java
│   │               │   │   ├── dto
│   │               │   │   │   ├── DistrictListInfo.java
│   │               │   │   │   ├── DistrictListResponse.java
│   │               │   │   │   ├── ProvinceListInfo.java
│   │               │   │   │   ├── ProvinceListResponse.java
│   │               │   │   │   ├── RegionDistrictListInfo.java
│   │               │   │   │   ├── RegionListResponse.java
│   │               │   │   │   └── RegionProvinceListInfo.java
│   │               │   │   ├── entity
│   │               │   │   │   ├── District.java
│   │               │   │   │   └── Province.java
│   │               │   │   ├── repository
│   │               │   │   │   ├── DistrictRepository.java
│   │               │   │   │   └── ProvinceRepository.java
│   │               │   │   └── service
│   │               │   │       ├── RegionReader.java
│   │               │   │       ├── RegionReaderImpl.java
│   │               │   │       ├── RegionService.java
│   │               │   │       ├── RegionServiceImpl.java
│   │               │   │       ├── RegionStore.java
│   │               │   │       └── RegionStoreImpl.java
│   │               │   ├── review
│   │               │   │   ├── controller
│   │               │   │   │   └── ReviewController.java
│   │               │   │   ├── dto
│   │               │   │   │   ├── ReviewCreateRequest.java
│   │               │   │   │   ├── ReviewCreateResponse.java
│   │               │   │   │   ├── ReviewDetailResponse.java
│   │               │   │   │   ├── ReviewListInfo.java
│   │               │   │   │   ├── ReviewListResponse.java
│   │               │   │   │   └── ReviewPagingResponse.java
│   │               │   │   ├── entity
│   │               │   │   │   └── Review.java
│   │               │   │   ├── repository
│   │               │   │   │   ├── ReviewRepository.java
│   │               │   │   │   └── querydsl
│   │               │   │   │       ├── ReviewQueryDsl.java
│   │               │   │   │       └── ReviewQueryDslImpl.java
│   │               │   │   └── service
│   │               │   │       ├── ReviewReader.java
│   │               │   │       ├── ReviewReaderImpl.java
│   │               │   │       ├── ReviewService.java
│   │               │   │       ├── ReviewServiceImpl.java
│   │               │   │       ├── ReviewStore.java
│   │               │   │       └── ReviewStoreImpl.java
│   │               │   └── user
│   │               │       ├── controller
│   │               │       │   ├── TokenController.java
│   │               │       │   ├── UserController.java
│   │               │       │   └── UserDashboardController.java
│   │               │       ├── dto
│   │               │       │   ├── AdditionalDataRequest.java
│   │               │       │   ├── EmailCheckRequest.java
│   │               │       │   ├── SigninRequest.java
│   │               │       │   ├── UserCreateRequest.java
│   │               │       │   ├── UserInfoResponse.java
│   │               │       │   ├── UserListInfo.java
│   │               │       │   ├── UserMeetingListResponse.java
│   │               │       │   ├── UserMeetingPagingResponse.java
│   │               │       │   ├── UserPlanListForCalendar.java
│   │               │       │   ├── UserPlanListResponse.java
│   │               │       │   ├── UserPlanListResponseForCalendar.java
│   │               │       │   ├── UserPlanPagingResponse.java
│   │               │       │   ├── UserPlanReviewableListResponse.java
│   │               │       │   ├── UserReviewListResponse.java
│   │               │       │   ├── UserReviewPagingResponse.java
│   │               │       │   ├── UserUpdateRequest.java
│   │               │       │   └── UserUpdateResponse.java
│   │               │       ├── entity
│   │               │       │   ├── LoginType.java
│   │               │       │   └── User.java
│   │               │       ├── repository
│   │               │       │   ├── UserRepository.java
│   │               │       │   └── querydsl
│   │               │       │       ├── UserQueryDsl.java
│   │               │       │       └── UserQueryDslImpl.java
│   │               │       └── service
│   │               │           ├── UserReader.java
│   │               │           ├── UserReaderImpl.java
│   │               │           ├── UserService.java
│   │               │           ├── UserServiceImpl.java
│   │               │           ├── UserStore.java
│   │               │           └── UserStoreImpl.java
│   │               └── global
│   │                   ├── config
│   │                   │   ├── RedisConfig.java
│   │                   │   ├── S3Config.java
│   │                   │   ├── SecurityConfig.java
│   │                   │   ├── SwaggerConfig.java
│   │                   │   └── WebClientConfig.java
│   │                   ├── entity
│   │                   │   └── Timestamped.java
│   │                   ├── exception
│   │                   │   ├── CustomException.java
│   │                   │   ├── ErrorCode.java
│   │                   │   └── handler
│   │                   │       └── GlobalExceptionHandler.java
│   │                   └── response
│   │                       ├── ErrorResponse.java
│   │                       └── SuccessResponse.java
│   └── resources
│       ├── application-prod.yml
│       ├── application.yml
│       ├── banner.txt
│       └── category.sql
└── test
    ├── java
    │   └── com
    │       └── wemo
    │           └── backend
    │               ├── WeMoApplicationTests.java
    │               └── domain
    │                   └── user
    │                       └── service
    │                           └── UserServiceImplTest.java
    └── resources
        └── application-test.yml
   
```
</details>

<br>
