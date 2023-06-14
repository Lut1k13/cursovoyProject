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
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Класс, представляющий список отслеживаемых файлов.
 * Реализует интерфейс Dao<ListenedFile>.
 */
public class ListenedFiles implements Dao<ListenedFile>{
    ObservableList<ListenedFile> listenedFiles = FXCollections.observableArrayList();

    /**
     * Метод для получения отслеживаемого файла по его объекту.
     * @param file объект файла
     * @return отслеживаемый файл
     * @throws IOException если возникает ошибка ввода-вывода
     */
    public ListenedFile get(File file) throws IOException {
        if(file.exists()) {
            for (ListenedFile element : listenedFiles)
                if (element.getIncode().equals(getIncode(file)) && !Objects.equals(element.getIncode(), "") && !Objects.equals(getIncode(file), ""))
                    return element;
        }
        else {
            for (ListenedFile element1 : listenedFiles)
                if (element1.getFile().getAbsolutePath().equals(file.getAbsolutePath()))
                    return element1;
        }
        return null;
    }

    /**
     * Метод для получения кода файла.
     * @param file файл, код которого необходимо получить
     * @return код файла
     * @throws IOException если возникает ошибка ввода-вывода
     */
    public String getIncode(File file) throws IOException {
        if(file.exists()) {
            BasicFileAttributes attr;
            Path path = Paths.get(file.getAbsolutePath());

            attr = Files.readAttributes(path, BasicFileAttributes.class);

            Object fileKey = attr.creationTime();
            return fileKey.toString();
        }
        return "";
    }

    /**
     * Метод для получения списка всех отслеживаемых файлов.
     * @return список отслеживаемых файлов
     */
    @Override
    public List<ListenedFile> getAll() {
        return listenedFiles;
    }

    /**
     * Метод для сохранения нового отслеживаемого файла в список отслеживаемых файлов.
     * @param t новый отслеживаемый файл
     */
    @Override
    public void save(ListenedFile t) {
        Platform.runLater(() ->{
            listenedFiles.add(t);
            try {
                createdState(t);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Метод для создания нового состояния файла и сохранения его в список состояний файла.
     * @param listenedFile отслеживаемый файл, для которого необходимо создать новое состояние
     * @throws IOException если возникает ошибка ввода-вывода
     */
    private void createdState(ListenedFile listenedFile) throws IOException {
        File file = listenedFile.getFile();
        String cause = "файл создан";
        String own = "";
        if (listenedFile.getFile().exists()) {
            FileOwnerAttributeView ownerView = Files.getFileAttributeView(
                    Paths.get(file.getAbsolutePath()), FileOwnerAttributeView.class);
            UserPrincipal owner = ownerView.getOwner();
            own = owner.getName();
        }
        listenedFile.save(new FileState.FileStateBuilder(file.getName(),
                        LocalDateTime.ofInstant(new Date(file.lastModified()).toInstant(), ZoneId.systemDefault()),
                        own).setCause(cause).build());
    }

    /**
     * Метод для обновления состояния файла (не используется в данном классе).
     * @param t состояние файла
     * @param params параметры состояния
     */
    @Override
    public void update(ListenedFile t, String[] params) {

    }

    /**
     * Метод для удаления состояния файла (не используется в данном классе).
     * @param t состояние файла
     */
    @Override
    public void delete(ListenedFile t) {

    }
}
