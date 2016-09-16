Настройки:
1){project_dir}/src/main/resources/log4j.xml
1.1)<param name="file" value="C:/log/all.log" /> - настроить свои каталоги

2){project_dir}/src/main/resources/hibernate.cfg.xml
2.1)<property name="connection.url">jdbc:sqlite:C:/test/test.db</property> - SQLite настроить свою базу - путь до нее, тестовую можно взять из корня проекта test.db

3){project_dir}/src/main/webapp/WEB-INF/web.xml
3.1)passwordFilePath=C:/test/passwords.txt - указать свою файл с ключами, тестовый можно взять из корня проекта passwords.txt

4)тестовые значения

пользлватель	пароль
kirill			123
anna			456

5)есть два сервиса:
5.1){project_url}/rest/login POST - авторизцет пользователя, на входе и выходе JSON, также возвращает HEADER = AUTH_TOKEN
который понадобится для работы с остальными сервисами, без него другие сервисы не смогут обрабатываться из-за Spring Security
5.2){project_url}/rest/example GET - простой пример запроса который будет проверсятся Spring Security и выдавать простую строку