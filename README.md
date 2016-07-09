#MyStudy
see https://github.com/forsrc/MyStudy
see http://git.oschina.net/forsrc/MyStudy

* Server: Springmvc(REST) + Spring4 + Hibernate5 + cxf(REST) + Activiti5 + ActiveMQ5 + ...
* Clinet: cordova(Android/Blackberry 10/iOS/OS X/Ubuntu/Windows/WP8)
          HTML + CSS + JavaScript + JQuery + cordova + bootstrap + bootstrap-material-design + ...


* MyStudy
    - ....|-- src
    - ....|.....|-- main
    - ....|.....|.....|-- doc ........................ ( markdown doc, plantuml file )
    - ....|.....|.....|-- java ....................... ( java )
    - ....|.....|.....|-- resources ............... ( XML, properties file )
    - ....|.....|.....|-- webapp
    - ....|.....|.....|.......|-- MyStudy ........ ( Apache Cordova project )
    - ....|.....|.....|.......|.......|-- www ...... ( HTML/CSS/JavaScript, image/fonts file )
    - ....|.....|.....|.......|-- WEB-INF
    - ....|.....|.....|.......|.......|-- lib .......... ( jar )
    - ....|.....|-- test
    - ....|-- tools
    - ....|.....|-- cobertura-2.1.1

----

##  setup

### 1. git

* git clone https://github.com/forsrc/MyStudy.git

### 2. Apache Cordova
Apache Cordova is an open-source mobile development framework. It allows you to use standard web technologies - HTML5, CSS3, and JavaScript for cross-platform development. Applications execute within wrappers targeted to each platform, and rely on standards-compliant API bindings to access each device's capabilities such as sensors, data, network status, etc.

* Use Apache Cordova if you are:

    * a mobile developer and want to extend an application across more than one platform, without having to re-implement it with each platform's language and tool set.
    * a web developer and want to deploy a web app that's packaged for distribution in various app store portals.
    * a mobile developer interested in mixing native application components with a WebView (special browser window) that can access device-level APIs, or if you want to develop a plugin interface between native and WebView components.


* ``` npm install -g cordova ```
* ``` $ cd src/main/webapp/MyStudy ```
  ``` $ cordova run browser ``` or ``` $ cordova run android ``` or ``` $ cordova run ios ```

#### see http://cordova.apache.org/

### 3. cobertura
Cobertura is a free Java tool that calculates the percentage of code accessed by tests. It can be used to identify which parts of your Java program are lacking test coverage. It is based on jcoverage.

* ``` $ cd MyStudy ```
  ``` $ ant coverage ```

#### see https://sourceforge.net/projects/cobertura/

### 4. Plantuml
PlantUML is a component that allows to quickly write :

* Sequence diagram,
* Usecase diagram,
* Class diagram,
* Activity diagram, (here is the new syntax),
* Component diagram,
* State diagram,
* Deployment diagram,
* Object diagram.
* wireframe graphical interface

Diagrams are defined using a simple and intuitive language. ( [see PlantUML Language Reference Guide](http://plantuml.com/PlantUML_Language_Reference_Guide.pdf) ).

You must have Java installed on your machine, and optionally [Graphviz](http://www.graphviz.org/) software which are used for all diagrams but sequence diagrams and activity beta diagrams.


#### see http://plantuml.com/
#### see http://www.graphviz.org/
----

### Springmvc4

* REST(Representational State Transfer) api
    + GET    http://192.168.1.2/springmvc/v1.0/user     --> list users
    + GET    http://192.168.1.2/springmvc/v1.0/user/1   --> read the user that id is 1
    + POST   http://192.168.1.2/springmvc/v1.0/user     --> create an user
    + PUT/PATCH    http://192.168.1.2/springmvc/v1.0/user/1   --> update the user that id is 1
    + DELETE http://192.168.1.2/springmvc/v1.0/user/1   --> delete the user that id is 1
* HTTP status codes    
    + 1XX - informational
        - 100 Continue
        - 101 Switching Protocols
        - 102 Processing
    + 2XX - success
        - 200 OK
        - 201 Created
        - 202 Accepted
        - 203 Non-Authoritative Information (since HTTP/1.1)
        - 204 No Content
        - 205 Reset Content
        - 206 Partial Content
        - 207 Multi-Status
        - 208 Already Reported
        - 226 IM Used       
    + 3XX - redirection
        - 301 Moved Permanently
        - 302 Found
        - 303 See Other (since HTTP/1.1)
        - 304 Not Modified
        - 305 Use Proxy (since HTTP/1.1)
        - 306 Switch Proxy
        - 307 Temporary Redirect (since HTTP/1.1)
        - 308 Permanent Redirect 
    + 4XX - client error
        - 400 Bad Request
        - 401 Unauthorized
        - 402 Payment Required
        - 403 Forbidden
        - 404 Not Found
        - 405 Method Not Allowed
        - 406 Not Acceptable
        - 407 Proxy Authentication Required
        - 408 Request Timeout
        - 409 Conflict
        - 410 Gone
        - 411 Length Required
        - 412 Precondition Failed
        - 413 Payload Too Large
        - 414 URI Too Long
        - 415 Unsupported Media Type
        - 416 Range Not Satisfiable 
        - 417 Expectation Failed
        - 418 I'm a teapot 
        - 421 Misdirected Request
        - 422 Unprocessable Entity
        - 423 Locked
        - 424 Failed Dependency
        - 426 Upgrade Required
        - 428 Precondition Required
        - 429 Too Many Requests
        - 431 Request Header Fields Too Large
        - 451 Unavailable For Legal Reasons  
    + 5XX - server error
        - 500 Internal Server Error
        - 501 Not Implemented
        - 502 Bad Gateway
        - 503 Service Unavailable
        - 504 Gateway Timeout
        - 505 HTTP Version Not Supported
        - 506 Variant Also Negotiates
        - 507 Insufficient Storage 
        - 508 Loop Detected
        - 510 Not Extended
        - 511 Network Authentication Required
        
        
---


## License

Apache License
Version 2.0, January 2004
http://www.apache.org/licenses/


## Mail
forsrc@163.com or litianzhi@gmail.com