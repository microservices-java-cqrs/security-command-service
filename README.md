# SecurityCommandService

<p style='text-align: justify;'> Este es un microservicio desarrollado con Java en su versión 17 y springboot 3.0.4, se encarga de la gestión de usuario, roles, menús y permisos. El código sigue los principios <b>SOLID</b> y para el desarrollo presenta una arquitectura hexagonal, cabe resaltar que se implementa el patrón <b>CQRS</b> y como su nombre lo indica SecurityCommandService solo gestiona altas y bajas a una base de datos <b>postgres</b> version 13.10, tambien se hace uso de <b>Kafka</b> y <b>Maven</b> para el envío de mensajes por donde se envian los objetos para su respectiva sincronización en el microservicio <b>SecurityQueryService</b> y para construir el microservicio respectivamente. </p>

## Servidor de desarrollo

<p style='text-align: justify;'> Primero debemos clonar el código del <a href="https://github.com/microservices-java-cqrs/security-command-service">repositorio</a> y con una terminal instalar las dependencias</p>

```sh
mvn dependency:resolve
```

<p style='text-align: justify;'> A continuación debemos construir la dependencia <b>security-share</b> el cual es un proyecto maven que sirve como un utilitario para el microservicio. Clonar el código del <a href="https://github.com/microservices-java-cqrs/security-command-service">repositorio</a> y una vez dentro del proyecto ejecutamos el siguiente comando: </p>

```sh
mvn clean install
```

<p style='text-align: justify;'> Luego debemos construir nuestro microservicio, para eso nos ubicamos dentro del proyecto <b>security-command-service</b> y ejecutamos el siguiente comando:</p> 

```sh
mvn clean install
```

<p style='text-align: justify;'> Finalmente para levantar el microservicio ejecutamos el siguiente comando:</p>

```sh
mvn spring-boot:run
```

<p style='text-align: justify;'> Esto levantará un servidor de desarrollo localhost en el puerto 8081 </p> 