# # springboot_cms

### 1. 개요 : SpringBoot + RestAPI를 적용하여 CMS 개발 목적

### 2. 기술 : SpringBoot + SpringSecurity + oauth2 + jwt 사용
<br>


## # 인증서 생성
```
1. 키스토어 생성(비밀키와 공개키 정보를 담고 있음)  
> keytool -genkeypair -alias server_private -keyalg RSA -keypass 123456 -keystore server.jks -storepass 123456 -validity 365

2. 공개키 정보 확인(openssl 필요)  
> keytool -list -rfc --keystore server.jks | openssl x509 -inform pem -pubkey

3. 공개키인증서 추출(참고)  
> keytool -export -alias server_private -keystore server.jks -rfc -file trustServer.cer

4. 키스토어 생성 정보 확인(참고)  
> keytool -list -v -keystore server.jks
```