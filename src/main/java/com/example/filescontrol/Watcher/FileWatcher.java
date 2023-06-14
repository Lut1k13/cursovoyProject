package com.example.filescontrol.Watcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Класс, представляющий наблюдателя за файлами.
 * Реализует интерфейс Runnable.
 * Позволяет отслеживать создание, изменение и удаление файлов в заданной директории.
 * При наступлении событий уведомляет зарегистрированных слушателей.
 */
public class FileWatcher implements Runnable {
    /**
     * Список зарегистрированных слушателей событий файлов.
     */
    protected List<FileListener> listeners = new ArrayList<>();
    /**
     * Директория, за которой осуществляется наблюдение.
     */
    protected final File folder;
    /**
     * Список объектов WatchService, используемых для наблюдения за файлами.
     */
    protected static final List<WatchService> watchServices = new ArrayList<>();
    /**
     * Конструктор класса.
     * @param folder директория, за которой необходимо наблюдать
     */
    public FileWatcher(File folder) {
        this.folder = folder;
    }
    /**
     * Метод для получения директории, за которой осуществляется наблюдение.
     * @return директория, за которой осуществляется наблюдение
     */
    public File getFolder() {
        return folder;
    }

    /**
     * Метод для запуска наблюдения за файлами.
     * Если директория существует, создается и запускается новый поток.
     */
    public void watch() {
        if (folder.exists()) {
            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }
    }

    /**
     * Метод для запуска потока, который будет наблюдать за файлами.
     * @throws InterruptedException исключение прерывания потока
     * @throws IOException исключение ввода-вывода
     * @throws ClosedWatchServiceException исключение закрытого сервиса наблюдения
     */
    @Override
    public void run() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Path path = Paths.get(folder.getAbsolutePath());
            path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
            watchServices.add(watchService);
            boolean poll = true;
            while (poll) {
                poll = pollEvents(watchService);
            }
        } catch (IOException | InterruptedException | ClosedWatchServiceException e) {
            Thread.currentThread().interrupt();

        }
    }

    /**
     * Метод для обработки событий, произошедших с файлами.
     * @param watchService объект WatchService для наблюдения за файлами
     * @return true, если события были успешно обработаны, false - в противном случае
     * @throws InterruptedException исключение прерывания потока
     * @throws IOException исключение ввода-вывода
     */
    protected boolean pollEvents(WatchService watchService) throws InterruptedException, IOException {
        WatchKey key = watchService.take();
        Path path = (Path) key.watchable();
        for (WatchEvent<?> event : key.pollEvents()) {
            Thread.sleep(100);
            notifyListeners(event.kind(), path.resolve((Path) event.context()).toFile());
        }
        return key.reset();
    }

    /**
     * Метод для уведомления слушателей о произошедших событиях с файлами.
     * @param kind тип события
     * @param file файл, с которым произошло событие
     * @throws IOException исключение ввода-вывода
     * @throws InterruptedException исключение прерывания потока
     */
    protected void notifyListeners(WatchEvent.Kind<?> kind, File file) throws IOException, InterruptedException {
        FileEvent event = new FileEvent(file);
        if (kind == ENTRY_CREATE) {
            for (FileListener listener : listeners) {
                listener.onCreated(event);
            }
            if (file.isDirectory()) {
                new FileWatcher(file).setListeners(listeners).watch();
            }
        }
        else if (kind == ENTRY_MODIFY) {
            for (FileListener listener : listeners) {
                listener.onModified(event);
            }
        }
        else if (kind == ENTRY_DELETE) {

            for (FileListener listener : listeners) {
                listener.onDeleted(event);
            }
        }
    }

    /**
     * Метод для добавления нового слушателя событий файлов.
     * @param listener слушатель событий файлов
     * @return объект FileWatcher для цепочного вызова методов
     */
    public FileWatcher addListener(FileListener listener) {
        listeners.add(listener);
        return this;
    }

    /**
     * Метод для удаления слушателя событий файлов.
     * @param listener слушатель событий файлов
     * @return объект FileWatcher для цепочного вызова методов
     */
    public FileWatcher removeListener(FileListener listener) {
        listeners.remove(listener);
        return this;
    }

    /**
     * Метод для получения списка зарегистрированных слушателей событий файлов.
     * @return список зарегистрированных слушателей событий файлов
     */
    public List<FileListener> getListeners() {
        return listeners;
    }

    /**
     * Метод для установки списка слушателей событий файлов.
     * @param listeners список слушателей событий файлов
     * @return объект FileWatcher для цепочного вызова методов
     */
    public FileWatcher setListeners(List<FileListener> listeners) {
        this.listeners = listeners;
        return this;
    }

    /**
     * Метод для получения списка объектов WatchService, используемых для наблюдения за файлами.
     * @return список объектов WatchService, используемых для наблюдения за файлами
     */
    public static List<WatchService> getWatchServices() {
        return Collections.unmodifiableList(watchServices);
    }
}
