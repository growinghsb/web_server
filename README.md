# 웹 실습  
* 요구사항 0    
  Socket 통신 구현     
  Socket 열어두고,    
  클라이언트 요청 대기.   
  TCP/IP 계층 통신     

  * main 메서드에서 서버 소켓을 열어두고, 연결을 기다린다.   
  
  * 연결이 되면(즉 url을 통해 8080 포트로 요청이 오면)   
    해당 서버는 inputStream으로 요청을 읽고,     
    outputStream으로 요청에 대한 응답을 하는 것이다.   
    
  * 현재 이 소스는 연결이 되면, 요청을 읽고, 응답하는 부분은     
    스레드로 구현이 되어 있기 때문에 accept()가 실행되고,     
    스레드를 start() 한 뒤, 요청을 읽어 응답 메세지를 만들고    
    응답을 하게 된다.   
    
  * 핵심은 main 실행 시을 서버를 열어두고 기다린다는 점,    
    기다리고 있다가 내가 url로 localhost:8080으로   
    접속하면, 즉 클라이언트로부터 요청이 오면     
    accept() 메서드가 실행되어 요청에 응답하기 위한     
    로직을 스레드로 수행하게 된다.   
    
  * 그리고 요청에 응답하는 로직은 inputStream으로               
    해당 요청을 읽어들이고, outputStream으로            
    해당 요청에 대한 응답을 진행하게 된다.                  
                  
  * 요청에 대한 응답은 크게는 헤더와 바디로 나눌 수 있다.             
    시작라인은 http 버전과 응답코드, 짧은 단어로 이루어진다.            
    그 밑에 헤더는 contentType과 Content-length로 이뤄진다.            
             
  * 그리고 아래는 http body가 들어가는데 여기에 정적 데이터가 들어가게 된다.                    
***           
* 요구사항 1      
  클라이언트의 요청을 받으면     
  요청 메시지의 첫 번째 줄을    
  추출해 요청에 맞는 html 파일로    
  요청에 대한 응답 처리
      
  * 클라이언트의 요청이 byte 단위로 InputStream으로 들어오는데     
   이를 읽어들여 요청에 맞는 응답을 처리하는 것    
   
  * http 요청 메세지 첫 줄에 메서드(GET, POST)와    
   요청 url, http 스팩이 들어있다.     
   
  * 요청 url을 추출해 거기에 맞는 정적 데이터를 선택해 서빙한다.    
 
  * 즉 우리가 크롬 url 창에 주소를 치면, 어떤 서버든    
   해당 url을 분석해서 거기에 알맞는 데이터를 보내주는 것이다.   
   
  * 그리고 html과 css, javascript 등 화면을 보여주는 요소들 역시    
   모든 요청을 url에 할 수 없으니, ui를 만들어 url 요청을 편하게     
   할 수 있도록 지원해 주는 것이다.    
   
  * 결국 모든 요청은 url 정보를 기반으로 서버에 요청하게 된다.   

  * 이를 수신한 서버는 url 정보 + 여러 메시지를 조합해 요청한 쪽으로   
   알맞는 응답을 하게 된다.   
   
  * 왜 byte로 데이터를 주고 받는가?    
   * 기본적으로 모든 데이터는 byte 단위로 송수신되어 진다.     
   * http 요청 메시지 역시 byte로 변환되어 inputStream을 타고   
     들어오게 된다.   
   * 그래서 inputStream 안에 있는 byte로 나뉜 데이터를 BufferedReader로    
     한 번에 한 줄씩, 문장 단위로 읽어 온 것이다.   
***
* 요구사항 2    
  회원가입 시 GET으로 데이터 전송     
  GET으로 전송 시 url에 키=값 형태로    
  & 구분자를 통해 전송.    
  요청 처리 시 첫 줄에 url과 같이   
  데이터 들어옴.
    
  * GET으로 데이터를 전송하면,      
    url에 키 - 밸류 형태로 & 구분자를 통해    
    http 헤더에 실려 전송된다.   
   
  * 대부분 GET 메서드는 조회에 사용되고,   
   데이터의 전송이나 수정은 POST를 사용한다.  
   
  * GET 요청의 특징은 url에 데이터가 같이    
    전송된다는 점이다. url을 통해 어떤 요청이   
    서버로 전송 됬는지 알 수 있다.    
***
* 요구사항 3      
  POST로 회원가입 정보 전달해서    
  http 바디에서 데이터를 꺼내 저장한다.    
  
  * 원인을 알았으면, 빠르게 해결하기 위해      
    행동하자. 알면서도 가만히 있지 말자.   
    
  * 삽질 역시 시간을 정해 두고 해야 한다.    
  
  * 마냥 낭비하는 습관을 없애자.   
    빠르게 문제를 해결할 수 있는 방법을   
    동원해서 문제를 해결하고, 추후에 리팩토링 하자.    
    
  * 일단 구현에 초점을 맞추고, 반드시 돌아와  
    리팩토링 하자. 요구사항은 충족해야 한다.     
   
  * POST는 헤더에는 아무런 데이터가 없다.    
    헤더 이후 한 줄 공백 후 데이터가 담겨 온다.    
  
  * BufferedReader의 경우 한 줄 읽기와, 문자 하나 읽기가 있는데    
    문자를 하나씩 읽을 경우엔 다 읽으면 버퍼링이 없다.     
  
  * 하지만 한 줄 씩 읽을 땐 버퍼링이 발생하는데...정확한 원인은     
    모르겠다. 내가 잘못한건지, 아니면 무한정 기다리고 있는건지.   
    한 줄 씩 읽을 땐 유의하자.     
### 요구사항 3번까지 진행한 현재 웹 흐름     
1. 처음에 Socket을 통해 8080 port를 열어두고 기다린다.    

2. 내가 localhost:8080을 브라우저에 요청한다.   

3. 8080을 열어둔 내가 만든 Socket 서버가 응답하면서     
   스레드를 실행시킨다. 해당 스레드를 통해    
   클라이언트의 요청을 받을 수 있고, 알맞는 응답을 한다.   
   
4. 회원가입 버튼을 누르는 행동 역시 서버에게 요청한 것이다.    
   서버는 회원가입 버튼을 눌렀을 때 이동하는 html 파일을     
   outputStream을 통해 byte로 변환해 응답한다.   
   
5. 브라우저는 이를 해석해 클라이언트에게 보여주게 된다.   

6. 회원가입을 하기 위해 데이터를 입력하고 클라이언트는 다시    
   버튼을 눌러 요청을 하게 된다.   
   
7. 이 때 요청은 POST로 처리된다. 그렇게 되면 url에는 데이터가    
   표시되지 않고, http body에 데이터가 담겨 서버에 전송된다.   
   
8. http 헤더에 POST 메서드를 확인하고, 데이터를 축출한다.   

9. 해당 데이터를 저장한다.    
***
* 요구사항 4     
  회원가입 시 따로 보여줄 화면이 없으니    
  302 상태 코드를 전송 후 응답 역시    
  index.html로 응답한다.   
  
  * 상태코드는 1xx ~ 5xx까지 있다.   

  * 각각의 상태코드는 응답 시 상태를 의미하는데   
    1xx는 처리중이라는 코드, 2xx는 응답 성공시,    
    3xx는 리다이렉트를 의미하는 코드, 4xx는 클라이언트의   
    잘못된 요청을 의미한다. 5xx는 서버의 문제로 정상적인    
    응답이 불가능 할 때 보이는 상태코드이다.   
    
  * 서버는 응답 시 현재 상태에 알맞는 상태코드를   
    클라이언트에 보낼 의무가 있다? 보내야 한다?   
    
  * mvc 방식에서는 개발자가 직접 응답을 작성하지 않지만    
    restAPI 방식에서는 개발자가 직접 응답을 데이터와     
    메시지를 작성하기 때문에 작성 방법이나, 코드는 알고    
    있으면 좋을 듯 하다.     
***    
* 요구사항 5    
  로그인 구현 후 로그인 시    
  해당 유저의 쿠키값을 응답헤더에   
  넣어 전송하는 것이다.     
  
  * html form 태그에서 action으로 요청하는 값은      
    url이다. 해서 html을 어떤 파일을 보내든       
    거기에 적은 값이 url에 남게 된다.        
    
  * 내가 보내는 html 파일이 url에 남는것이 아니라    
    요청한 값이 그대로 url에 남기 때문에 get일 때,    
    post일 때, 그리고 다른 http 메서드를 사용할 때    
    url이 같아도 된다. 메서드로 구분하기 때문.      
    
  * 거기에 요청이 들어오면, http 메서드로 나누고,    
    그 다음 안에 있는 값을 꺼내 사용하는 것이다.    
    
  * 쿠키는 헤더에 Set-Cookie: 쿠키이름=쿠키값    
    이렇게 문자열로 쿠키값을 클라이언트에 전송하면    
    클라이언트는 이 값을 보관 하면서 요청을 할 때 마다    
    해당 쿠키 값을 보내게 된다.   
    
  * 그럼 서버에서는 쿠키가 필요하면 이 값을 사용하고,    
    아니면 무시하는 쪽으로 해서 쿠키를 사용한다.      
***    
