package com.example.filescontrol.Watcher;

import com.example.filescontrol.FilesControl.FileState;
import com.example.filescontrol.FilesControl.ListenedFiles;
import com.example.filescontrol.FilesControl.ListenedFile;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Адаптер для обработки событий файлов.
 * Реализует интерфейс FileListener.
 */
public class FileAdapter implements FileListener {
    ListenedFiles listenedFiles;
    boolean tempfile=false;
    boolean edited=false;

    /**
     * Конструктор класса.
     * @param listenedFiles объект класса ListenedFiles для хранения информации о прослушиваемых файлах
     */
    public FileAdapter(ListenedFiles listenedFiles)
    {
        this.listenedFiles = listenedFiles;
    }

    /**
     * Метод, вызываемый при создании файла.
     * Если файл не является временным и еще не был добавлен в список прослушиваемых файлов,
     * то он добавляется в список прослушиваемых файлов.
     * @param event событие создания файла
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    public void onCreated(FileEvent event) throws IOException {
        if(!event.getFile().getName().contains("~")) {
            if(!tempfile) {
                if (listenedFiles.get(event.getFile()) == null)
                    listenedFiles.save(new ListenedFile(event.getFile()));
            }
            else tempfile=false;
        }
        else tempfile=true;
    }

    /**
     * Метод, вызываемый при изменении файла.
     * Если файл не является временным, то для него создается объект состояния файла и сохраняется в объекте класса ListenedFile.
     * @param event событие изменения файла
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    public void onModified(FileEvent event) throws IOException {
        if(!event.getFile().getName().contains("~")) {
            if(!edited) {
                ListenedFile file = listenedFiles.get(event.getFile());
                if (!file.getFile().getName().equals(event.getFile().getName()))
                    file.setFile(event.getFile());
                saveState(file, false);
            }
            edited=!edited;
        }
        else tempfile=true;
    }

    /**
     * Метод, вызываемый при удалении файла.
     * Если файл не является временным, то для него создается объект состояния файла с причиной "файл удален" и сохраняется в объекте класса ListenedFile.
     * @param event событие удаления файла
     * @throws IOException если произошла ошибка ввода-вывода
     */
    @Override
    public void onDeleted(FileEvent event) throws IOException {
        if(!event.getFile().getName().contains("~")) {
            if(!tempfile) {
                ListenedFile file = listenedFiles.get(event.getFile());
                saveState(file, true);
            }
            else tempfile=false;
        }
        else tempfile=true;
    }

    /**
     * Метод для сохранения объекта состояния файла в объекте класса ListenedFile.
     * @param listenedFile объект класса ListenedFile, для которого сохраняется объект состояния файла
     * @param delete флаг, указывающий на то, был ли файл удален
     * @throws IOException если произошла ошибка ввода-вывода
     */
    public void saveState(ListenedFile listenedFile,boolean delete) throws IOException {
        File file = listenedFile.getFile();
        String own="";
        String cause = "файл удален";
        if((file.getName().contains(".doc") || file.getName().contains(".docx")) && !delete) {
            InputStream input = new FileInputStream(file);
            XWPFDocument document = new XWPFDocument(input);
            POIXMLProperties prop = document.getProperties();
            int simbols = prop.getExtendedProperties().getCharacters();
            String author = prop.getCoreProperties().getLastModifiedByUser();
            document.close();
            input.close();
                cause = "Количество символов изменено на: "+simbols;
            listenedFile.save(new FileState.FileStateBuilder(file.getName(),
                    LocalDateTime.ofInstant(new Date(file.lastModified()).toInstant(), ZoneId.systemDefault()),
                    author).setCause(cause).build());
        }
        else{
            if(!delete) {
                FileOwnerAttributeView ownerView = Files.getFileAttributeView(
                        Paths.get(file.getAbsolutePath()), FileOwnerAttributeView.class);
                UserPrincipal owner = ownerView.getOwner();
                own = owner.getName();
                listenedFile.save(new FileState.FileStateBuilder(file.getName(),
                        LocalDateTime.ofInstant(new Date(file.lastModified()).toInstant(), ZoneId.systemDefault()),
                        own).build());
            }
            else {
                if(!listenedFile.getAll().isEmpty())
                    own = listenedFile.getAll().get(0).getAuthor();
                listenedFile.save(new FileState.FileStateBuilder(file.getName(),
                        LocalDateTime.ofInstant(new Date(file.lastModified()).toInstant(), ZoneId.systemDefault()),
                        own).setCause(cause).build());
            }
        }
    }
}
