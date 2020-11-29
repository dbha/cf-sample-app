
##### blue-green Test using CF CLI     
##### https://docs.cloudfoundry.org/devguide/deploy-apps/blue-green.html

##### 배포 manifest.yml 확인
     cat manifest.yml
    ---
    applications:
    - name: myapp-blue
      instances: 2
      memory: 1G
      random-route: true
      path: target/myapp-test-0.0.1-SNAPSHOT.jar
      
##### Step 1: Push an App
     cf push blue -n demo-time
    Deprecation warning: Use of the '--hostname' command-line flag option is deprecated in favor of the 'routes' property in the manifest. Please see https://docs.cloudfoundry.org/devguide/deploy-apps/manifest-attributes.html#routes for usage information. The '--hostname' command-line flag option will be removed in the future.
    
    Manifest에서 admin(으)로 dbha-org 조직/my-space 영역에 푸시 중...
    Manifest 파일 /Users/dbha/IdeaProjects/spring-cloud-service-sample/myapp-test-backup/manifest.yml 사용
    앱 정보를 가져오는 중...
    이러한 속성의 앱 작성 중...
    + 이름:       blue
      경로:       /Users/dbha/IdeaProjects/spring-cloud-service-sample/myapp-test-backup/target/myapp-test-0.0.1-SNAPSHOT.jar
    + 인스턴스:   2
    + 메모리:     1G
      라우트:
    +   demo-time.cfapps.mytest.example.com
    
     cf a
    admin(으)로 dbha-org 조직/my-space 영역의 앱 가져오는 중...
    확인
    
    이름          요청된 상태   인스턴스   메모리   디스크   URL
    blue          started       2/2        1G       1G       demo-time.cfapps.mytest.example.com
    cf-demo       started       1/1        768M     1G       cf-demo-terrific-bandicoot-ep.cfapps.mytest.example.com
    cook          started       1/1        5G       1G       cook.cfapps.mytest.example.com
    dbha-tomcat   started       1/1        1G       1G       dbha-tomcat.cfapps.mytest.example.com
    
    - App 테스트
    http://demo-time.cfapps.mytest.example.com/
    Hello Spring

##### Step 2: Update App and Push
##### 소스 수정 : dbha Spring ! 출력

     cf push green -n demo-time-temp
    Deprecation warning: Use of the '--hostname' command-line flag option is deprecated in favor of the 'routes' property in the manifest. Please see https://docs.cloudfoundry.org/devguide/deploy-apps/manifest-attributes.html#routes for usage information. The '--hostname' command-line flag option will be removed in the future.
    
    Manifest에서 admin(으)로 dbha-org 조직/my-space 영역에 푸시 중...
    Manifest 파일 /Users/dbha/IdeaProjects/spring-cloud-service-sample/myapp-test-backup/manifest.yml 사용
    앱 정보를 가져오는 중...
    이러한 속성으로 앱 업데이트 중...
      이름:             green
      경로:             /Users/dbha/IdeaProjects/spring-cloud-service-sample/myapp-test-backup/target/myapp-test-0.0.1-SNAPSHOT.jar
      디스크 할당량:    1G
      상태 검사 유형:   port
      인스턴스:         2
      메모리:           1G
      스택:             cflinuxfs3
      라우트:
        demo-time-temp.cfapps.mytest.example.com

     cf a
    admin(으)로 dbha-org 조직/my-space 영역의 앱 가져오는 중...
    확인
    
    이름          요청된 상태   인스턴스   메모리   디스크   URL
    cf-demo       started       1/1        768M     1G       cf-demo-terrific-bandicoot-ep.cfapps.mytest.example.com
    dbha-tomcat   started       1/1        1G       1G       dbha-tomcat.cfapps.mytest.example.com
    cook          started       1/1        5G       1G       cook.cfapps.mytest.example.com
    blue          started       2/2        1G       1G       demo-time.cfapps.mytest.example.com
    green         started       2/2        1G       1G       demo-time-temp.cfapps.mytest.example.com
    
    - 배포 App 테스트
    http://demo-time-temp.cfapps.mytest.example.com/
    dbha Spring!!
    
##### Step 3: Map Original Route to Green
    Now that both apps are up and running, switch the router so all incoming requests go to the Green app and the Blue app. 
    Do this by mapping the original URL route (demo-time.example.com) to the Green app using the cf map-route command.
    
    ubuntu@ubuntu-210:~/buildpack$ cf map-route -h
    NAME:
       map-route - Add a url route to an app
    
    USAGE:
       Map an HTTP route:
          cf map-route APP_NAME DOMAIN [--hostname HOSTNAME] [--path PATH]
    
       Map a TCP route:
          cf map-route APP_NAME DOMAIN (--port PORT | --random-port)
    
    EXAMPLES:
       cf map-route my-app example.com                              # example.com
       cf map-route my-app example.com --hostname myhost            # myhost.example.com
       cf map-route my-app example.com --hostname myhost --path foo # myhost.example.com/foo
       cf map-route my-app example.com --port 5000                  # example.com:5000
    
    OPTIONS:
       --hostname, -n      Hostname for the HTTP route (required for shared domains)
       --path              Path for the HTTP route
       --port              Port for the TCP route
       --random-port       Create a random port for the TCP route
       
      cf map-route green cfapps.mytest.example.com -n demo-time
    admin(으)로 dbha-org 조직/my-space 영역의 demo-time.cfapps.mytest.example.com 라우트 작성 중...
    확인
    demo-time.cfapps.mytest.example.com 라우트가 이미 있음
    admin(으)로 dbha-org 조직/my-space 영역의 green 앱에 demo-time.cfapps.mytest.example.com 라우트 추가 중...
    확인
    
     cf a
    admin(으)로 dbha-org 조직/my-space 영역의 앱 가져오는 중...
    확인
    
    이름          요청된 상태   인스턴스   메모리   디스크   URL
    cf-demo       started       1/1        768M     1G       cf-demo-terrific-bandicoot-ep.cfapps.mytest.example.com
    dbha-tomcat   started       1/1        1G       1G       dbha-tomcat.cfapps.mytest.example.com
    cook          started       1/1        5G       1G       cook.cfapps.mytest.example.com
    blue          started       2/2        1G       1G       demo-time.cfapps.mytest.example.com
    green         started       2/2        1G       1G       demo-time.cfapps.mytest.example.com, demo-time-temp.cfapps.mytest.example.com
    
    - App 테스트
    http://demo-time.cfapps.mytest.example.com/
    Hello Spring or dbha Spring!! Request 할때마다 한번씩 동시에 나옴
    
##### Step 5: Remove Temporary Route to Green

     cf unmap-route green cfapps.mytest.example.com -n demo-time-temp
    admin(으)로 dbha-org 조직/my-space 영역의 green 앱에서 demo-time-temp.cfapps.mytest.example.com 라우트 제거 중...
    확인
    dbha ~/IdeaProjects/spring-cloud-service-sample/myapp-test-backup   master ±
    
     cf a
    admin(으)로 dbha-org 조직/my-space 영역의 앱 가져오는 중...
    확인
    
    이름          요청된 상태   인스턴스   메모리   디스크   URL
    cf-demo       started       1/1        768M     1G       cf-demo-terrific-bandicoot-ep.cfapps.mytest.example.com
    dbha-tomcat   started       1/1        1G       1G       dbha-tomcat.cfapps.mytest.example.com
    cook          started       1/1        5G       1G       cook.cfapps.mytest.example.com
    blue          started       2/2        1G       1G
    green         started       2/2        1G       1G       demo-time.cfapps.mytest.example.com