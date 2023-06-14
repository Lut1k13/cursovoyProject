package com.example.filescontrol.FilesControl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс, представляющий состояние файла.
 * Используется для хранения информации о названии файла, дате последнего изменения, авторе изменений и причине изменений.
 * Для создания объектов класса используется паттерн Builder.
 */
public class FileState {
    /**
     * Название файла.
     */
    String title;

    /**
     * Дата последнего изменения файла.
     */
    LocalDateTime edited;

    /**
     * Автор последних изменений файла.
     */
    String author;

    /**
     * Причина изменений файла.
     */
    String cause;

    /**
     * Идентификатор файла.
     */
    int id;

    /**
     * Конструктор класса.
     * Создает объект FileState с заданными параметрами.
     * @param fileStateBuilder объект класса FileStateBuilder, содержащий параметры для создания объекта FileState
     */
    public FileState(FileStateBuilder fileStateBuilder)
    {
        title = fileStateBuilder.title;
        edited = fileStateBuilder.edited;
        author = fileStateBuilder.author;
        cause = fileStateBuilder.cause;
        id = fileStateBuilder.id;
    }

    /**
     * Метод для получения строкового представления объекта FileState.
     * @return строковое представление объекта FileState
     */
    public String toString()
    {
        return title + ": " + edited.format(DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm:ss"));
    }

    /**
     * Метод для получения даты последнего изменения файла.
     * @return дата последнего изменения файла
     */
    public LocalDateTime getEdited() {
        return edited;
    }

    /**
     * Метод для получения автора последних изменений файла.
     * @return автор последних изменений файла
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Метод для получения названия файла.
     * @return название файла
     */
    public String getTitle() {
        return title;
    }


    /**
     * Метод для получения причины изменений файла.
     * @return причина изменений файла
     */
    public String getCause() {
        return cause;
    }

    /**
     * Внутренний класс, представляющий Builder для класса FileState.
     * Используется для создания объектов FileState с заданными параметрами.
     */
    public static class FileStateBuilder
    {
        /**
         * Название файла.
         */
        String title;

        /**
         * Дата последнего изменения файла.
         */
        LocalDateTime edited;

        /**
         * Автор последних изменений файла.
         */
        String author;

        /**
         * Причина изменений файла.
         */
        String cause = "";

        /**
         * Идентификатор файла.
         */
        int id;

        /**
         * Конструктор класса.
         * Создает объект FileStateBuilder с заданными параметрами.
         * @param title название файла
         * @param edited дата последнего изменения файла
         * @param author автор последних изменений файла
         */
        public FileStateBuilder(String title, LocalDateTime edited, String author)
        {
            this.title = title;
            this.edited = edited;
            this.author = author;
        }

        /**
         * Метод для установки причины изменений файла.
         * @param cause причина изменений файла
         * @return объект FileStateBuilder для цепочного вызова методов
         */
        public FileStateBuilder setCause(String cause) {
            this.cause = cause;
            return this;
        }

        /**
         * Метод для установки идентификатора файла.
         * @param id идентификатор файла
         * @return объект FileStateBuilder для цепочного вызова методов
         */
        public FileStateBuilder setId(int id) {
            this.id = id;
            return this;
        }

        /**
         * Метод для создания объекта FileState с заданными параметрами.
         * @return объект FileState с заданными параметрами
         */
        public FileState build() {
            return new FileState(this);
        }
    }
}
