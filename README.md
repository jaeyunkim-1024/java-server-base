# 목적
* Server 기본적인 환경 구축 프로젝트
---
# 환경
```
- SpringBoot : 3.3.4
- JDK : 17
- Mysql : 9.0.1
- Redis : 7.2.6 ( 비밀번호 설정 X )
- Kafka : 3.9.0
```

---
# 기본 적용 라이브러리
* Auth : SpringSecurity, JWT, SMTP, Redis
* Datasource : JPA(HIKARI CP) + Mybatis
* Pub/Sub : Apache Kafka
* DotEnv

# Func
* 회원가입/로그인/로그아웃
* 이메일 인증/로그인 이력
* 관리자 - 토큰 만료

---

## System Design

![Image](https://github.com/user-attachments/assets/577c1998-72a4-4800-be8e-895fbe630dbe)






