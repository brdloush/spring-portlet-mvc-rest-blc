spring-portlet-mvc-rest-blc
==================

(inspired by <http://github.com/miamidade/spring-portlet-mvc.git>)

A minimal Spring Portlet MVC project using Maven.

* Java 1.6
* Portlet 2.0
* Spring Framework 3.1.1
* Annotation-based controller configuration
* Spring MVC
* Thymeleaf
* Jersey client communicating with local Broadleaf commerce REST API
* Ability to run the portlet in lightweight Pluto portal from maven

Usage
-----
First make sure that you're running BLC Demo site server (<https://github.com/BroadleafCommerce/DemoSite>) on <http://localhost:8080> and that you've enabled REST API in its web.xml. You simply need to add following line to into patchConfigLocation, just before the applicationContext-security:
```
/WEB-INF/applicationContext-rest-api.xml 
```

Then you can run the portlet app on port of your choice (such as 9999) like so:

```
$ git clone http://github.com/brdloush/spring-portlet-mvc-rest-blc.git
$ cd spring-portlet-mvc-rest-blc
$ mvn package
$ mvn -Djetty.port=9999 portlet-prototyping:run
```

Finally just access <http://localhost:9999/pluto/portal>

