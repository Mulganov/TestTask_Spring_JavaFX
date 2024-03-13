Add VM Options: --module-path "{адрес к папке: javafx-sdk-21.0.2\lib}" --add-modules javafx.controls,javafx.fxml
**Для работы JavaFX нужно добавить его модуль к VM опциям в run cofigurations**
![Screenshot_1](https://github.com/Mulganov/TestTask_Spring_JavaFX/assets/38687671/b9612cd4-e4e5-45f6-9a2a-8f7854122e13)

Модуль JavaFX я так закинул в корень проекта, что бы Вам не пришлось качать его отдельно с интернета, папка "javafx-sdk-21.0.2"

**Если хотите протестить программу без MySQL**
То нужно убрать весь конфиг с файла "application.properties"
![изображение](https://github.com/Mulganov/TestTask_Spring_JavaFX/assets/38687671/dfb6eb83-7c21-4ddd-bdc9-c75aedcfbe9a)

Без этого конфига будет работать встроенная БД в Спринга, которая хранится в оперативной памяти
