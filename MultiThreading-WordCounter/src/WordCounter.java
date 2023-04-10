import com.sun.jmx.snmp.Timestamp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
//import java.sql.Time;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordCounter implements Runnable {
    // The following are the ONLY variables we will modify for grading.
    // The rest of your code must run with no changes.
    public static final Path FOLDER_OF_TEXT_FILES =
            Paths.get("input"); // path to the folder where input text files are located
    public static final Path WORD_COUNT_TABLE_FILE = Paths.get("FolderText\\wordCounter.txt"); // path to the output plain-text (.txt) file
    public static final int NUMBER_OF_THREADS = 49; // max. number of threads to spawn
    private static List<Map<String, Integer>> mapList = new ArrayList<>();
    private static List<Path> files = new ArrayList<>();
    private Path path;


    //private static List<String> documents = new ArrayList<>();

    public WordCounter( Path p )
    {
        this.path = p;
    }

    public synchronized static List<Path> filesList()
    {
        List<Path> files = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(FOLDER_OF_TEXT_FILES)) {
            files = paths.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(files);

        return files;
    }

    @Override
    public void run()
    {
        Map<String, Integer> map = new TreeMap<>();
        try {
            List<String> data = Files.readAllLines(path);
            String str = "";
            for (int i = 0; i < data.size(); i++) {
                if (i >= 1) {
                    str += " " + data.get(i);
                } else {
                    str += data.get(i);
                }
            }
            String[] arrToString = str.split(" ");

            for (String s : arrToString) {
                if (s.contains(":") || s.contains(".") || s.contains(";")
                        || s.contains(",") || s.contains("?") || s.contains("!")) {
                    s = s.substring(0, s.length() - 1);
                }

                s = s.toLowerCase();
                if (map.containsKey(s)) {
                    map.put(s, map.get(s) + 1);
                } else {
                    map.putIfAbsent(s, 1);
                }
            }
            //System.out.println("task complete");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mapList.add( map);
        //System.out.println(map + "line 74");
    }

    public static List<Map<String, Integer>> reading()
    {
        files = filesList();

        int threadnumber = NUMBER_OF_THREADS;
        if( threadnumber == 0)
        {
            threadnumber = 1;
        }
        else if( NUMBER_OF_THREADS > files.size())
        {
            threadnumber = files.size();
        }

        ExecutorService pool = Executors.newFixedThreadPool(threadnumber);

        for(int i = 0; i < files.size(); i ++)
        {
            Runnable r1 = new WordCounter(files.get(i));
            pool.execute(r1);
        }
        //System.out.println(mapList + "line 97");
        pool.shutdown();
        while(!pool.isTerminated()){}
        //System.out.println(mapList + "line 101");

        return mapList;
    }

    public static void writing()
    {
        List<Map<String, Integer>> mapList = reading();
        Set<String> keys = new TreeSet<String>();
        List<String> fileNames = new ArrayList<>();

        for (Map<String, Integer> m : mapList) {
            for (String k : m.keySet()) {
                keys.add(k);
            }
        }

        for (Path p : filesList()) {
            String s = p.toString();
            String s1 = s.substring(
                    p.toString().lastIndexOf('\\') + 1);
            fileNames.add(s1);
        }
        fileNames.add("total");

        try {
            if (Files.exists(WORD_COUNT_TABLE_FILE)) {
                System.out.println("File already exists");
            } else {
                Path donePath = Files.createFile(WORD_COUNT_TABLE_FILE);

                String header = "                ";
                for (String s : fileNames) {
                    header += String.format("%-15s|", s);
                }

                Files.write(donePath, (header + "\n").getBytes(),
                        StandardOpenOption.APPEND);

                for (String s : keys) {
                    String subtext = String.format("%-15s|", s);
                    int total = 0;
                    for (Map<String, Integer> m : mapList) {
                        int current = 0;
                        if (m.containsKey(s)) {
                            current = m.get(s);
                        }
                        subtext = subtext + String.format("%-15d|", current);
                        total += current;
                    }
                    subtext = subtext + String.format("%-15d|", total);
                    Files.write(donePath, (subtext + "\n").getBytes(),
                            StandardOpenOption.APPEND);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String... args)
    {
        writing();
    }
}