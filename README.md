# Vision board

Фулстек приложение для создания досок визуализации и обмена идеями.

### **Приложение состоит из**:
- **Фронтенда:** две HTML-страницы с простым и интуитивно понятным интерфейсом.

- **Бэкенда:** реализован на Java с использованием Spring Framework, обеспечивает создание, хранение и управление досками визуализации.

### **Основные возможности:**
- Создание и редактирование досок визуализации.
- Возможность делиться своими идеями с другими пользователями.
- Возможность общаться, оставлять комментарии и лайки.
- Поиск интересующих постов по тегам.
- Удобный интерфейс для взаимодействия с контентом.

### **Стек технологий:**
#### Java 21, Maven, Spring Framework, Hibernate, REST API, Lombok, Slf4j, HTML, Apache Tomcat.


### **Инструкция по сборке**:

1.Установить необходимые инструменты:

    - Java 21
    - Maven
    - Apache Tomcat

2.Перейти в корневую директорию проекта.

3.Выполнить команду для сборки проекта:
    **mvn clean install**


### **Размещение архива**:

1. Переместить архив `vision-board.war` в директорию `webapps` вашего сервера Tomcat.  

2. Перейти в директорию /apache-tomcat-11.0.3/bin/

3. Выполнить команду **./startup.sh**


### **Доступ к приложению**:

После завершения развертывания перейдите по ссылке:
     - [http://localhost:8080/ision-board/feed](http://localhost:8080/ision-board/feed)


