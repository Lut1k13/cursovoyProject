package com.example.filescontrol.FilesControl;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий отслеживаемый файл.
 * Реализует интерфейс Dao<FileState>.
 */
public class ListenedFile implements Dao<FileState>{
    private File file;
    String incode;
    ObservableList<FileState> fileStates = FXCollections.observableArrayList();

    /**
     * Конструктор класса ListenedFile.
     * @param file файл, который будет отслеживаться
     * @throws IOException если возникает ошибка ввода-вывода
     */
    public ListenedFile(File file) throws IOException {
        this.file = file;
        BasicFileAttributes attr;
        Path path = Paths.get(file.getAbsolutePath());
        if(file.exists()) {
            attr = Files.readAttributes(path, BasicFileAttributes.class);

            Object fileKey = attr.creationTime();
            incode = fileKey.toString();
        }
        else incode="";
    }

    /**
     * Метод для получения кода файла.
     * @return код файла
     */
    public String getIncode() {
        return incode;
    }

    /**
     * Метод для установки нового файла для отслеживания.
     * @param file новый файл для отслеживания
     */
    public void setFile(File file)
    {
        this.file = file;
    }

    /**
     * Метод для получения отслеживаемого файла.
     * @return отслеживаемый файл
     */
    public File getFile() {
        return file;
    }

    /**
     * Метод для получения списка всех состояний файла.
     * @return список состояний файла
     */
    @Override
    public List<FileState> getAll() {
        return fileStates;
    }

    /**
     * Метод для сохранения нового состояния файла в список состояний.
     * @param t новое состояние файла
     */
    @Override
    public void save(FileState t) {
        Platform.runLater(() ->fileStates.add(t));
    }

    /**
     * Метод для обновления состояния файла (не используется в данном классе).
     * @param t состояние файла
     * @param params параметры состояния
     */
    @Override
    public void update(FileState t, String[] params) { }

    /**
     * Метод для удаления состояния файла (не используется в данном классе).
     * @param t состояние файла
     */
    @Override
    public void delete(FileState t) { }

    /**
     * Переопределенный метод toString для получения имени файла.
     * @return имя файла
     */
    public String toString()
    {
        return file.getName();
    }

}
