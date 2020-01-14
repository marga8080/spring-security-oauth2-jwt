# OAuth2授权模式

## 1密码模式

> 将用户名,密码传过去,直接获取token

### 1.1 获取token

```shell
curl -u client1:secret http://localhost:8080/oauth/token -d grant_type=password -d username=admin -d password=123456
```

>oauth2 默认只支持post 如果需要get方式需要添加在`OAuth2AuthorizationServerConfig.java`中添加：
>
>```java
>endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
>```

返回：

```json
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImZvbyIsInJlYWQiLCJ3cml0ZSJdLCJuYW1lIjoi5byg5LiJIiwiaWQiOiIxIiwiZXhwIjoxNTQyMDA0MzEwLCJhdXRob3JpdGllcyI6WyJBQ1RVQVRPUiIsIlJPTEVfVVNFUiJdLCJqdGkiOiJmZmZhMjlhYy1jODNlLTQ3MzEtYWExMi0wYmU3MWQ2NzllY2EiLCJjbGllbnRfaWQiOiJjbGllbnQxIn0.kgMTz1dCcmzHd42ALRXEFAMgFeTRgN6alfYcvI8r-q75DaeC_f5bygf11c68-XB_X1oacq8rj5dbtHXuJRw71gf_OMjG1s8Qu_1OE-JN31BLys6nh0EE1xhSMo4zXW5FCKEkfYIEI5VE9VeHjHCFR7DjpQPsFxKY5NNj-Ru4t5oYYCyPOWhMWH_RWU68nJN1As9QIWAA0wIuJ6_k55J00mG6Z6p1geU5xRO6F-OXIghq5GkKsKPb5R2wIkDCN9XTU-bCIrIKh7o5Pq91ib_ctF9PoORKjGIx2MEfmUvDwKXpTmLwt9DLXmAJZ7nzdHE1jDHTGrjpciWC5kEOJ0gaWw",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImZvbyIsInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiJmZmZhMjlhYy1jODNlLTQ3MzEtYWExMi0wYmU3MWQ2NzllY2EiLCJuYW1lIjoi5byg5LiJIiwiaWQiOiIxIiwiZXhwIjoxNTQyMDQwMjc0LCJhdXRob3JpdGllcyI6WyJBQ1RVQVRPUiIsIlJPTEVfVVNFUiJdLCJqdGkiOiI2ZmNmODBmNS03ODg2LTRhNDctOGZiZS01NTkxNTFiNDQ5MWQiLCJjbGllbnRfaWQiOiJjbGllbnQxIn0.i9JUlSzM_uPz7kVPxhobq9TYq9dQZPtKZOMZVR2C0c4qpKkQrmZJmt0FnlnnlENaGYCXBkKFtYWUVSuZQHkHwT0AdwGYQCXluU0p4qZ2jyApRuGRExfWNVaK24lPy6dE-lPS2FG0zVtPv64XoF6ikQyErRo5zrcqfU3iO33-27RjB_UT5F77mCfPRRd6rLTn8gvsESY83Vg6kYcPYVj0-ERyBiuqMFIQxdTDcsHcmBIR7JQQDghu6ClXXHsoulaij7qOLwnMsHyu7gRkqGk2I8Bat56xWJgQNh8uiKo4CYbV10ntH3ZvhwnzUjXV_3791Bgc6C8xdjzl0TnmxAKzTQ",
    "expires_in": 35999,
    "scope": "foo read write",
    "jti": "fffa29ac-c83e-4731-aa12-0be71d679eca"
}
```

### 1.2 验证token

```shell
curl http://localhost:8080/oauth/check_token?token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImZvbyIsInJlYWQiLCJ3cml0ZSJdLCJuYW1lIjoi5byg5LiJIiwiaWQiOiIxIiwiZXhwIjoxNTQyMDM4ODY5LCJhdXRob3JpdGllcyI6WyJBQ1RVQVRPUiIsIlJPTEVfVVNFUiJdLCJqdGkiOiI5N2MzZWY2MC1jZTE0LTQ3ZmEtODlmOC04Y2VlYjhiZmI4ZGMiLCJjbGllbnRfaWQiOiJjbGllbnQxIn0.S-vExO1uhZs2lJEwTGWVT4XXlAEQZHfstEOKdaazshqW8ZEZgAaQi3IQLNgPJnONF33XAUnhZB2PzwVLVMkVo94q72YjbQeHIOtvtn1tWtAzE7NX3pXazjggDlFuEZkL9i4zs1aI35XQf1dlJWzElM6KbKFfxB9IScw6aZOLvRI4Ero6sHJV7H5RADaXAJGzYkRye4moSrYDfHHm_JDyc_MCHbzeRg8YbzRwwU99riKl_el-bp0HxOfu283_yOI6hBXG65lzvagkHFWtaDqwRPOKwfGToMcSjtZd-mW_k6sGLwKJT6tWXcBxcX9Wqo4ZjRfAIwbbpSb-taqNJ3_RIg
```

返回：

```json
{
	"user_name": "admin",
	"scope": ["foo", "read", "write"],
	"name": "张三",
	"active": true,
	"id": "1",
	"exp": 1542041261,
	"authorities": ["ACTUATOR", "ROLE_USER"],
	"jti": "824b18db-2a85-4115-802a-d1ab54c24579",
	"client_id": "client1"
}
```

如果token过期了，返回如下：

```json
{
	"error": "invalid_token",
	"error_description": "Token has expired"
}
```

### 1.3. 刷新token

```shell
curl -u client1:secret http://localhost:8080/oauth/token -d grant_type=refresh_token -d refresh_token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6InRhbmd5eiIsInNjb3BlIjpbImJhciIsInJlYWQiXSwiYXRpIjoiZjQxNmE3NTItOTAzNy00MTRmLTlkMmYtMjVjZDdjNDNkMTUzIiwibmFtZSI6IuWUkOi_nOeFpyIsImlkIjo0LCJleHAiOjE1MTA4Mjk4MDMsImF1dGhvcml0aWVzIjpbIkFDVFVBVE9SIiwiUk9MRV9VU0VSIl0sImp0aSI6ImQ0NzU0ZDM2LThkZjItNGI0ZC05ZGFiLWJmZjdjNWFhMDhkMCIsImNsaWVudF9pZCI6ImNsaWVudDEifQ.aJWrWlSVgtp4nacSyWYzZl1BLkK9Yhl5yAMrZen13n4INcJPVoVibDsAv90YJis2IGMCCKNp7UhlWmSyj9AKgk5yx6t7I5FPk7JiaBo1O3K4h2b2Pl_OJ2QH5ni_LSBUlfHl6PEy9mCa1-9P6HbHSSyIKGRG0cz_jfcbK4eU4h-2t3q6h8FLyv5ZacG5PB0DimCMZkb1EBDQ8q1OR3aESentGOEr5GNrN1zm0metuRtmXTaUSvbtyQXyehQJjSvQbTSU4pyTCClZQ4l8Bj-kfmVNHB6FFGOG41itBKU--VlH7QpWYAHGpJnk96-GIf1RDJAK2lLRpx3y1EPXNIwtYA
```

返回：

```shell
{
	"access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImZvbyIsInJlYWQiLCJ3cml0ZSJdLCJuYW1lIjoi5byg5LiJIiwiaWQiOiIxIiwiZXhwIjoxNTQyMDQxNTM1LCJhdXRob3JpdGllcyI6WyJBQ1RVQVRPUiIsIlJPTEVfVVNFUiJdLCJqdGkiOiI2ODM0MzM1NS05MDBkLTRlM2YtOWY4ZS1iMWUzNTdjYmVhOGMiLCJjbGllbnRfaWQiOiJjbGllbnQxIn0.NQl-YTIG6RKy1XxkKJ7E0ps3u0e1i8oORwHzh3HN_thhvxJFa7Hg16yIJHsN9ilQ6uMXDbzZrEl44u1YcVA6BIw-q_uJn4h9fZvm6ZvuAFAdXu0M_MNsTLki5LxnwrNkNYtRfv_bwlqGt_1pHtJ71tN3uAEZ9O0NbTqZ_OyMMyCfGz-TgzatGAVHk5X_qebTloX-ATfHx1h9nHlw4Xp01pUeHKqDaK-k71JHfCsqL0uTQqTqdB6tdkqRGG1HuBVTVaDbV28EfsM-DGn9VwGqGKHgjSR_tgZL1BTPfj2sKSV6Gk8MRh4LPUcklwFnBi6sYmCTMB44MqbIk5MetnMToQ",
	"token_type": "bearer",
	"refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImZvbyIsInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiI2ODM0MzM1NS05MDBkLTRlM2YtOWY4ZS1iMWUzNTdjYmVhOGMiLCJuYW1lIjoi5byg5LiJIiwiaWQiOiIxIiwiZXhwIjoxNTQyMDQwMjc0LCJhdXRob3JpdGllcyI6WyJBQ1RVQVRPUiIsIlJPTEVfVVNFUiJdLCJqdGkiOiI2ZmNmODBmNS03ODg2LTRhNDctOGZiZS01NTkxNTFiNDQ5MWQiLCJjbGllbnRfaWQiOiJjbGllbnQxIn0.aeAEC_r5ydobSPdAn0_JjSbeYPBgCkAXMhuDbPyTsJRWtRy5mzBJwLjtb9YvT5v3LYk5ic0MUTBSBElFVESNu2ACAI7BaSLVDvt2zeYiIQh_YQok9jBWFi_vpsTn5lrhZUx119vru0W9IlM-RTkxN2t-l98LA_vU1tzzMgO63GrFMQs4-N8SeebebTUTe6IkuVJ0w-rVgNTNEmw13ZU3f3eI4a5LpD6eYGJU4VRBkAu-OL-7t_aUp33K1xca1BVhzYqMALpBsoy5TcHX6fHrCOUSY7EPYyG5Z3n2bNpKQK3sQ2byIN8XfAQFfYNtXcLCo-acelhODRJ9HwwBSx0JPQ",
	"expires_in": 35999,
	"scope": "foo read write",
	"jti": "68343355-900d-4e3f-9f8e-b1e357cbea8c"
}
```

## 2授权码模式

> 即先登录获取code,再获取token

### 2.1访问授权页面

```http
http://localhost:8080/oauth/authorize?client_id=client1&response_type=code&redirect_uri=http://www.baidu.com&state=3fsdoi
```

![](https://marga8080.github.io/httpdoc/image/spring-oauth2/spring-oauth2-001.png)
出现登录界面后，输入用户名密码。

**注意, 如果每次登陆时输入的用户名不一样,那么Spring Security会认为是不同的用户,因此访问/token/authorize会再次显示授权页面。如果用户名一致, 则只需要授权一次**

### 2.2用户选择是否授权

![](https://marga8080.github.io/httpdoc/image/spring-oauth2/spring-oauth2-002.png)

对每一个scope选择授权（Approve）或拒绝（Deny），并点击授权（Authorize）按钮之后，浏览器就会重定向到百度，并带上code参数：

![](https://marga8080.github.io/httpdoc/image/spring-oauth2/spring-oauth2-003.png)

### 2.3根据code获取token

![code-token](https://marga8080.github.io/httpdoc/image/spring-oauth2/code-token.png)

返回：

```json
{
	"access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6InRhbmd5eiIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJuYW1lIjoi5ZSQ6L-c54WnIiwiaWQiOjQsImV4cCI6MTUxMDg5NzgwMiwiYXV0aG9yaXRpZXMiOlsiQUNUVUFUT1IiLCJST0xFX1VTRVIiXSwianRpIjoiNDJiYjQxYWUtNTc5YS00OWQxLWE5Y2YtYWQ1ODMxYmM5OGM5IiwiY2xpZW50X2lkIjoiY2xpZW50MSJ9.X3zxxQDpi8NbiQg7HoL4sDd9tKm9u436W6vyfaUHmiatrNaUxkSOTN-SWlEDPsrhIj1OgTZAdWayu-atPx2W1-gw2PJyqfMj12dZMO73DZ326uGwJMJvUz04cxs2nuPCxU8eSWugO9QbgMu17mVauODQZ-_-BaPjEy4JZ46MP_9I7VXnz8ssmiKfqgzDYCF0BeKmdbuYTJoJhlsldTjZzQJ0wztEe4B9F8uwYgBNUDBrDqo6s7sUeIlsTz-hmAs8FO_EbIbP5WeUwp8qwS8nuiRYlt4U0b9MQxyfLRufsyPyhAQOSvy3O_XtHhVwvwipxe5X-E8iVkcBNIA7IQQ5nQ",
	"token_type": "bearer",
	"refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6InRhbmd5eiIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJhdGkiOiI0MmJiNDFhZS01NzlhLTQ5ZDEtYTljZi1hZDU4MzFiYzk4YzkiLCJuYW1lIjoi5ZSQ6L-c54WnIiwiaWQiOjQsImV4cCI6MTUxMDkwMDgwMiwiYXV0aG9yaXRpZXMiOlsiQUNUVUFUT1IiLCJST0xFX1VTRVIiXSwianRpIjoiMDVmZWE5NmUtZjFkNC00M2YwLWFhZjItZjcyY2NjY2Y2YjYyIiwiY2xpZW50X2lkIjoiY2xpZW50MSJ9.g4klujbliwpIwCpVCmq9LFYahqq2t2um9ZeTBcy1hG5xZGpbQdNLV84pHT6nwJciE3fytN8jjQDvypa0_Ms2RaQ2NYKWq9nV9lh606MJ1dHUVsNdPfLiV1O1PK8ueu5MI88AAcxuvnCyLZDJfHzqcWhr6NV1p3DYZ_QlXwZhbV7ynhQRsiAdCFrSbyMaj2f4A4h5FSoEyIZFkicvDNmaDVImBTxy41yNAs0VTi1UOYDOXrH0gMGskR3xxQayjH_t8iqZGdejbfiuflIpioTLoyB84Gbo3TU4N64lHMJ0e5AGcRrwwnIRqBdwcYxecKIW-Znj-sD2_SdDJFMjlps_sg",
	"expires_in": 599,
	"scope": "read write",
	"jti": "42bb41ae-579a-49d1-a9cf-ad5831bc98c9"
}
```

如果code已经被使用了，放回：

```json
{
    "error": "invalid_grant",
    "error_description": "Invalid authorization code: NZ5JrN"
}
```

## implicit 简化模式

> 在redirect_uri 的Hash传递token; Auth客户端运行在浏览器中,如JS,Flash

```http
http://localhost:8080/oauth/authorize?client_id=client2&response_type=token&redirect_uri=http://www.baidu.com
```

出现登录页面后，输入用户密码，跳转到redirect_uri页面并在连接上带上token

![](https://marga8080.github.io/httpdoc/image/spring-oauth2/uri-token.png)

```http
https://www.baidu.com/#access_token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSIsImZvbyIsImJhciJdLCJuYW1lIjoi5byg5LiJIiwiaWQiOiIxIiwiZXhwIjoxNTQyMDEwODUzLCJhdXRob3JpdGllcyI6WyJBQ1RVQVRPUiIsIlJPTEVfVVNFUiJdLCJqdGkiOiJjMzVlYmY0ZC05ZDFiLTQyYTUtOGY0ZS04ZjdjZjM1NDI0YjYiLCJjbGllbnRfaWQiOiJjbGllbnQyIn0.e0cGQheRsQrcyUXzF8BXkM6wHw5Zm9VkRexKR89EmDGwZWqagGAFbpTjEnxqKIzywakgGqqAMyQPzXQQdoB2_HnDEyGOpi9hrYXutfC6Td0hAPSdXTthhQRrK4YQfuqaK914HC8trOEikneNuj0SmeZtwOnXO8NxgunSz1dq_6dvqF4JY43pTX6EzLw5sE1kYS39ThpLNd0O7AERliVMNsoIBSZ24mF1MkYmvm93XGVD-IBzktb-p2Vh123W6aKk7fY0_-bIK40yEDEDfwe-zCxkLUPhitijVNLSajcZNKrudlUjkKYPnAyqRmlUQVBi77Rcq6waecaSy-t_b_zgTA&token_type=bearer&expires_in=3599&scope=read%20write%20foo%20bar&jti=c35ebf4d-9d1b-42a5-8f4e-8f7cf35424b6
```





# RSA 证书

> 参考：
>
> https://beku8.wordpress.com/2015/03/31/configuring-spring-oauth2-with-jwt-asymmetric-rsa-keypair/

## 生成JKS文件

```shell
keytool -genkeypair -alias lwuums -keyalg RSA -keypass uumspass -keystore lwuums.jks -storepass uumspass
```
## 导出cer

```shell
keytool -export -keystore lwuums.jks -alias mytestkey -file lwuums.cer
```

## 导出公钥

```shell
keytool -list -rfc --keystore lwuums.jks | openssl x509 -inform der -in lwuums.cer -pubkey -noout
```

