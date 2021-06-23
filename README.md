#Microaservice to shortent link**

##Prerequisites
1. Docker is installed.
2. Java 8 or higher is installed.
3. Maven installed.

##Run instructions

1. Run Hazelcast in Docker 
```
docker run -e HZ_NETWORK_PUBLICADDRESS=127.0.0.1:5701 -p 5701:5701 hazelcast/hazelcast:4.2
```
2. Run application
 * goto with cloned directory
 * run Maven command:
```
mvn spring-boot:run
```
3. Send link shortening request
 * Open URL in browser: http://localhost:8080/swagger-ui.html
 * Try POST method with body:
```
{
  "url": "facebook.com"
}
```

4. Send link unshortening request
 * Open URL in browser: http://localhost:8080/swagger-ui.html
 * Try GET method with this parameter id=b
(just one letter b).
