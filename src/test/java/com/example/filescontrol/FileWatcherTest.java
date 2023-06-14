package com.example.filescontrol;

import com.example.filescontrol.FilesControl.ListenedFiles;
import com.example.filescontrol.Watcher.FileAdapter;
import com.example.filescontrol.Watcher.FileEvent;
import com.example.filescontrol.Watcher.FileWatcher;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Класс для тестирования класса FileWatcher.
 */
public class FileWatcherTest {
    /**
     * Метод для тестирования работы класса FileWatcher.
     * @throws IOException если произошла ошибка ввода-вывода
     * @throws InterruptedException если поток был прерван во время ожидания
     */
    @Test
    public void test() throws IOException, InterruptedException {
        // Создание объекта-папки
        File folder = new File("src/test/resources");
        // Создание объекта-словаря
        final Map<String, String> map = new HashMap<>();
        // Создание объекта-наблюдателя за папкой
        FileWatcher watcher = new FileWatcher(folder);
        // Добавление слушателя событий изменения файлов в папке
        watcher.addListener(new FileAdapter(new ListenedFiles()) {
            public void onCreated(FileEvent event) {
                map.put("file.created", event.getFile().getName());
            }
            public void onModified(FileEvent event) {
                map.put("file.modified", event.getFile().getName());
            }
            public void onDeleted(FileEvent event) {
                map.put("file.deleted", event.getFile().getName());
            }
        }).watch();
        // Проверка количества слушателей
        assertEquals(1, watcher.getListeners().size());
        // Ожидание 2 секунды
        wait(2000);
        // Создание файла в папке
        File file = new File(folder + "/test.txt");
        try(FileWriter writer = new FileWriter(file)) {
            writer.write("Some String");
        }
        wait(2000);
        file.delete();
        wait(2000);
        // Проверка наличия событий создания и изменения файла в словаре
        assertEquals(file.getName(), map.get("file.created"));
        assertEquals(file.getName(), map.get("file.modified"));
    }

        /**
         * Метод для ожидания указанного количества миллисекунд.
         * @param time количество миллисекунд для ожидания
         * @throws InterruptedException если поток был прерван во время ожидания
         */
    public void wait(int time) throws InterruptedException {
        Thread.sleep(time);
    }
}
