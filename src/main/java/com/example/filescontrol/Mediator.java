package com.example.filescontrol;

import com.example.filescontrol.FilesControl.User;
import com.example.filescontrol.Watcher.FileWatcher;
import javafx.scene.control.Alert;

/**
 * Класс-посредник для отображения информации о программе.
 */
public class Mediator {
    /**
     * Метод для отображения информации о программе.
     * @param user текущий пользователь
     * @param watcher объект-наблюдатель за папкой
     */
    public static void showInfo(User user, FileWatcher watcher) {
        // Создание объекта диалогового окна
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Информация о программе");
        alert.setTitle("Справка");
        // Получение имени отслеживаемой папки
        String folder= "";
        if(watcher!=null)
            folder = ". Отслеживаемая папка -"+watcher.getFolder().getName();
        // Задание текста сообщения в диалоговом окне
        alert.setContentText("Текущий пользователь-"+user.getName()+folder+". Данная программа предназначена для отслеживания изменений в файлах внутри выбранной пользователем папки.\nДля начала работы используйте меню 'Файл' -> 'Выбор папки для отслеживания'.\nПосле выбора директории в левом меню появится список файлов в папке, в среднем - список состояний файла, справа - параметры состояния");
        alert.show();
    }
}
