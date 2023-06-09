<h1 align="center"> Gestión Club Deportivos </h1>

 <p align="left">
   <img src="https://img.shields.io/badge/STATUS-EN%20DESAROLLO-green">
   </p>

## :hammer: Descripción General
La aplicación que he realizado, se realiza para dar servicio a los club deportivos, en una única herramienta puedan gestionar todo su club.
- Consiste en una aplicación web con acceso a BBDD relacional, el funcionamiento es el siguiente:
  - Se podrá registrar tanto los padres de los jugadores como los entrenadores del club.
  - Una vez registrado se les podrá asignar un rol, se disponen de tres tipos:
    - Usuario: Solo con acceso a dar de alta jugadores, rellenando el formulario con todos los datos seleccionados, también podrán consultar los resultados de los partidos y la crónica adjunta y solicitar paquetes de ropa para sus hijos.
    - Entrenadores: Añade al rol usuario, las funcionalidades de asignar equipos a jugadores, asignar dorsales, y crear partidos y sus crónicas correspondientes, listar todos los paquetes de ropa deportiva solicitados.
    - Admin: Dispone de todas las funcionales descritas y la opción de asignarlos los roles a los usuarios
- La aplicación está diseña para cubrir todas las necesidades de una correcta gestión de un club deportivo. 
Los padres al darse de alta como usuarios, podrán registrar a sus hijos para la práctica deportiva, tendrán la opción de comprar el material deportivo necesario.
- De esta forma la aplicación se nutrirá con los datos necesarios para que la gestión por parte de los entrenadores sea correcta, donde podrán ver los diversos equipos que disponen el club y los jugadores.
- En versiones posteriores se desea implementar un sistema de notificaciones bidireccional, para que los padres puedan avisar de la ausencia de sus hijos, y además puedan ser informados de los entrenamientos, las horas y lugares de los partidos.


## :hammer: Puntos Obligatorios

- `Funcionalidad 1`: El modelo de datos estará compuesto de, al menos, 5 clases y tendrán que existir relaciones entre ellas. Cada clase tendrá, al menos, 6 atributos (String, int, float, boolean y algún tipo para almacenar fechas). Cada clase tendrá, al menos, 2 atributos obligatorios y algún otro con algún tipo de restricción de formato/validación. -
- `Funcionalidad 2`: Se tendrá que poder realizar, el menos, las operaciones CRUD sobre cada una de las clases. Se controlarán, al menos, los errores 400, 404 y 500 -
- `Funcionalidad 3`: Añade opciones de filtrado para al menos una operación en cada clase en donde se puedan indicar hasta 3 campos diferentes (solo aplicable para operaciones GET) -
- `Funcionalidad 4`: Prepara una colección Postman que permita probar todas las operaciones desarrolladas -
- `Funcionalidad 5`: Configura en el proyecto la librería logback para que la aplicación web cuente con un log. Añade trazas en el código de forma que permita seguir el rastro de ejecución en el log (para todas las operaciones que se puedan realizar y también para los casos en los que se recojan errores) -

## :hammer: Puntos Extras
- `Funcionalidad 6`: Utiliza la herramienta Git (y GitHub) durante todo el desarrollo de la aplicación. Escribe el fichero README.md para explicar cómo poner en marcha el proyecto. Utiliza el gestor de Issues para los problemas/fallos que vayan surgiendo -
- `Funcionalidad 7`: Añade 3 operaciones que utilicen consultas SQL nativas para extraer la información de la base de datos -
- `Funcionalidad 8`:  -