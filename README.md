#MyStudy
see https://github.com/forsrc/MyStudy
see http://git.oschina.net/forsrc/MyStudy

* Server: Springmvc(REST) + Spring4 + Hibernate5 + cxf(REST) + Activiti5 + ActiveMQ5 + Rdis (NoSql DB) + MySQL + Ehcache + Freemaker...
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
   
##### Demo
* Demo user's username/password: ``` admin/123456 ```

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

### 4. Redis
Redis is an open source (BSD licensed), in-memory data structure store, used as database, cache and message broker. It supports data structures such as strings, hashes, lists, sets, sorted sets with range queries, bitmaps, hyperloglogs and geospatial indexes with radius queries. Redis has built-in replication, Lua scripting, LRU eviction, transactions and different levels of on-disk persistence, and provides high availability via Redis Sentinel and automatic partitioning with Redis Cluster.

* Download ``` redis-3.2.1.tar.gz ```
* $ ``` cd redis-3.2.1 ```
* $ ``` make && make install```
* $ ``` cd src```
* $ ``` redis-server ../src/redis.conf ```

#### see http://redis.io/


----

### Springmvc4

* REST(Representational State Transfer) api
    + GET    http://192.168.1.2/springmvc/v1.0/user     --> list users
    + GET    http://192.168.1.2/springmvc/v1.0/user/1   --> read the user that id is 1
    + POST   http://192.168.1.2/springmvc/v1.0/user     --> create an user
    + PUT/PATCH    http://192.168.1.2/springmvc/v1.0/user/1   --> update the user that id is 1
    + DELETE http://192.168.1.2/springmvc/v1.0/user/1   --> delete the user that id is 1
        
        
---


## License

Apache License
Version 2.0, January 2004
http://www.apache.org/licenses/


## Mail
forsrc@163.com or litianzhi@gmail.com