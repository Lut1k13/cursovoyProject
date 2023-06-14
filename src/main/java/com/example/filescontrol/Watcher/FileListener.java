package com.example.filescontrol.Watcher;

import java.io.IOException;
import java.util.EventListener;

/**
 * Интерфейс слушателя событий файлов.
 * Наследуется от интерфейса EventListener.
 * Содержит методы, которые вызываются при создании, изменении или удалении файла.
 */
public interface FileListener extends EventListener {

    /**
     * Метод, вызываемый при создании файла.
     * @param event событие файла
     * @throws IOException исключение ввода-вывода
     */
    void onCreated(FileEvent event) throws IOException;

    /**
     * Метод, вызываемый при изменении файла.
     * @param event событие файла
     * @throws IOException исключение ввода-вывода
     */
    void onModified(FileEvent event) throws IOException;

    /**
     * Метод, вызываемый при удалении файла.
     * @param event событие файла
     * @throws IOException исключение ввода-вывода
     */
    void onDeleted(FileEvent event) throws IOException;
}
