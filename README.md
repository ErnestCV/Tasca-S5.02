# Tasca-S5.02
Joc de Daus

## Instruccions

Perquè funcioni correctament el programa, primer cal inicialitzar les bases de dades de MongoDB i MySQL.

### MongoDB

Des de la shell de mongo: 

```
use jocdaus
db.roles.insertMany([{name: "ROLE_USER"}, {name: "ROLE_ADMIN"}])
```

### MySQL

Cal executar l'script de creació de la base de dades que es troba a src/main/resources

## Recull d'enllaços útils

### Security / JWT
https://dzone.com/articles/securing-spring-boot-microservices-with-json-web-t

https://auth0.com/blog/spring-boot-authorization-tutorial-secure-an-api-java/

https://www.youtube.com/watch?v=L9oWG6aj_U8

https://www.bezkoder.com/spring-boot-jwt-authentication/

https://www.bezkoder.com/spring-boot-jwt-auth-mongodb/

https://www.baeldung.com/spring-security-method-security

### Sorting / Pagination
https://www.baeldung.com/spring-data-sorting

https://www.baeldung.com/spring-data-jpa-pagination-sorting

https://www.baeldung.com/rest-api-pagination-in-spring

https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-seven-pagination/

https://reflectoring.io/spring-boot-paging/

https://www.bezkoder.com/react-spring-boot-pagination/

https://www.bezkoder.com/spring-boot-pagination-filter-jpa-pageable/

https://www.bezkoder.com/spring-boot-mongodb-pagination/

https://stackabuse.com/spring-data-mongodb-guide-to-the-query-annotation/

### Testing
https://phauer.com/2016/testing-restful-services-java-best-practices/

https://phauer.com/2019/modern-best-practices-testing-java/

https://github.com/phauer/blog-related/tree/master/testingrestservice

https://github.com/amigoscode/software-testing

https://stackabuse.com/guide-to-unit-testing-spring-boot-rest-apis/

https://github.com/hamvocke/spring-testing

https://github.com/bygui86/spring-testing

https://github.com/smustafa/embedded-mongo-example

https://github.com/Pio-Trek/Spring-Rest-API-Unit-Test 

https://stackoverflow.com/questions/15203485/spring-test-security-how-to-mock-authentication

https://stackoverflow.com/questions/45241566/spring-boot-unit-tests-with-jwt-token-security

https://stackoverflow.com/questions/41824885/use-withmockuser-with-springboottest-inside-an-oauth2-resource-server-applic/44502136#44502136

https://medium.com/free-code-camp/unit-testing-services-endpoints-and-repositories-in-spring-boot-4b7d9dc2b772

https://blog.devgenius.io/spring-boot-deep-dive-on-unit-testing-92bbdf549594

https://codefiction.net/unit-testing-crud-endpoints-of-a-spring-boot-java-web-service-api/

https://www.javaguides.net/2022/03/spring-boot-unit-testing-service-layer.html

https://howtodoinjava.com/junit/how-to-unit-test-spring-security-authentication-with-junit/

https://reflectoring.io/spring-boot-web-controller-test/

https://www.bezkoder.com/spring-boot-webmvctest/

https://github.com/rwinch/gs-spring-security-3.2

### Front (react)
https://dzone.com/articles/integrating-spring-boot-and-react-with-spring-secu-1

https://www.bezkoder.com/spring-boot-react-jwt-auth/#Implementation-2

https://www.bezkoder.com/react-hooks-redux-login-registration-example/

https://www.bezkoder.com/react-jwt-auth/#Project_Structure

https://www.bezkoder.com/integrate-reactjs-spring-boot/

https://www.bezkoder.com/react-crud-web-api/

https://www.bezkoder.com/react-spring-boot-crud/

https://codepen.io/Calleb/pen/GRxjNga

https://www.youtube.com/watch?v=KDHuVnsJl9U

https://www.bezkoder.com/react-pagination-hooks/

https://www.bezkoder.com/react-hooks-redux-crud/

