package com.example.filescontrol.Watcher;

import java.io.File;
import java.util.EventObject;

/**
 * Класс события файлов.
 * Наследуется от класса EventObject.
 * Содержит информацию о файле, на который произошло событие.
 */
public class FileEvent extends EventObject {
    /**
     * Конструктор класса.
     * @param source объект, который вызвал событие
     */
    public FileEvent(Object source) {
        super(source);
    }

    /**
     * Метод для получения файла, на который произошло событие.
     * @return файл, на который произошло событие
     */
    public File getFile() {
        return (File) getSource();
    }
}
