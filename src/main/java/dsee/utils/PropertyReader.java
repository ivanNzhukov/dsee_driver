package dsee.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class PropertyReader {

    public static List<File> read() throws Exception {
        List<File> fileList = null;

        try {
            List<String> allLines = Files.readAllLines(Paths.get(getLocation() + "\\filepath.prop"));
            if (!allLines.isEmpty()) {
                fileList = new LinkedList<>();
                for (String allLine : allLines) {
                    try {
                        String line = allLine.trim();
                        if (line.length() > 0) {
                            fileList.add(new File(allLine));
                        }
                    } catch (Exception e) {
                        throw new Exception("Не удалось добавить файл " + allLine + "\n");
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException("Can not find filepath.prop in the folder " + Paths.get(getLocation() + "\\filepath.prop") + "\n");
        }
        return fileList;
    }


    private static Path getLocation() {
        URL startupUrl = PropertyReader.class.getProtectionDomain().getCodeSource().getLocation();
        Path path;
        try {
            path = Paths.get(startupUrl.toURI());
        } catch (Exception e) {
            try {
                path = Paths.get(new URL(startupUrl.getPath()).getPath());
            } catch (Exception ipe) {
                path = Paths.get(startupUrl.getPath());
            }
        }
        path = path.getParent();
        return path;
    }
}
