import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileReaderUtil {

    private static final int MAX_RECORDS = 10000;

    public static MovieRecord[] readCorpus(String folderPath) {

        MovieRecord[] records = new MovieRecord[MAX_RECORDS];
        int count = 0;

        File folder = new File(folderPath);

        if (!folder.exists()) {
            System.out.println("Corpus folder not found: " + folderPath);
            return new MovieRecord[0];
        }

        count = readFolder(folder, records, count);

        MovieRecord[] result = new MovieRecord[count];

        for (int i = 0; i < count; i++) {
            result[i] = records[i];
        }

        return result;
    }

    private static int readFolder(
            File folder,
            MovieRecord[] records,
            int count) {

        File[] files = folder.listFiles();

        if (files == null) {
            return count;
        }

        for (int i = 0; i < files.length; i++) {

            File file = files[i];

            if (file.isDirectory()) {
                count = readFolder(file, records, count);
            }

            else if (file.getName().toLowerCase().endsWith(".txt")) {

                count = readFile(file, records, count);

            }

            if (count >= records.length) {
                break;
            }
        }

        return count;
    }

    private static int readFile(
            File file,
            MovieRecord[] records,
            int count) {

        try {

            BufferedReader br = new BufferedReader(
                    new FileReader(file)
            );

            String line;

            while ((line = br.readLine()) != null) {

                line = line.trim();

                if (line.length() == 0) {
                    continue;
                }

                MovieRecord movie = parseLine(line);

                if (movie != null && count < records.length) {
                    records[count] = movie;
                    count++;
                }
            }

            br.close();

        } catch (IOException e) {

            System.out.println(
                    "Error reading file: " + file.getName()
            );
        }

        return count;
    }

    private static MovieRecord parseLine(String line) {

        String[] parts = line.split("\\|");

        if (parts.length < 4) {
            return null;
        }

        String title = parts[0].trim();
        String actors = parts[1].trim();
        String genre = parts[2].trim();
        String keywords = parts[3].trim();

        return new MovieRecord(
                title,
                actors,
                genre,
                keywords
        );
    }
}
