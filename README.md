library-sample-project
======================

Sample project for virtual library web application with Spring and Hibernate.

Project of a sample web application written with Spring and Hibernate. For now it's only a version beta, without some of features implemented (like caching).

Also, some of TODOs remain : 

1) JSP (layout) : 
- books listing by categories http://localhost:8080/categories/Affaires
- one book page : http://localhost:8080/books/Affaires/PL/6
- do form template for all forms : http://localhost:8080/account/modify

2) Routing : 
- make routing system based on XML files, like routers in Zend Framework or Symfony

3) Controllers : 
- redirect where exception is catched

4) Cache : 
- add browser cache
- implement EhCache

5) Backoffice : 
- implement all CRUD features 

6) CSRF : 
- error on borrowing action (no CSRF token attached to URL) :  http://localhost:8080/borrowing/do/12

7) URL's format : 
- http://localhost:8080/categories/Po%C3%A8sie : accentuated letters are present in urls : add column with title written to URL format at the database

8) Project cleaning : 
- remove some of unused JARs 
- remove all unused files under /META-INF/spring

9) Maven : 
- replace ANT deployment by Maven


You can find more informations about developping of Spring applications on this page : 
http://www.bart-konieczny.com/fr/spring-par-pratique (available actually only in French)
