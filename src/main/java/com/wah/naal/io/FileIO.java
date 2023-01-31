package com.wah.naal.io;

import com.wah.naal.Application;
import com.wah.naal.exception.FileIOException;
import com.wah.naal.generator.MotionFileGenerator;
import com.wah.naal.model.motionfile.motion.Motion;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents input-output operations.
 * This class is singleton
 */
public class FileIO {

    private static FileIO instance;

    private MotionFileGenerator motionFileGenerator = MotionFileGenerator.getInstance();

    private static final Logger logger = LoggerFactory.getLogger(FileIO.class);

    private FileIO(){}

    /**
     * Returns instance of {@link FileIO}
     * @return instance of {@link FileIO}
     */
    public static synchronized FileIO getInstance() {
        if (Objects.isNull(instance)){
            instance = new FileIO();
        }
        return instance;
    }

    /**
     * Reads all {@link Motion} files from provided directory as byte list
     * @param path path to be scanned
     * @return all {@link Motion} files from scanned folder
     */
    public List<List<Byte>> loadAllMotions(String path) {
        File folder = new File(path);
        File[] files = folder.listFiles();
        if (files == null) {
            return new ArrayList<>();
        }

        return Arrays.stream(files)
                .filter(file -> file.getName().endsWith(".mot"))
                .map(file -> {
                    try {
                        return FileUtils.readFileToByteArray(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(array -> {
                    List<Byte> list = new LinkedList<>();
                    for (byte sourceByte : array) {
                        list.add(sourceByte);
                    }
                    return list;
                }).collect(Collectors.toList());
    }

    /**
     * Reads all motions files from application's run directory as byte list
     * @return all motions files from application's run directory
     */
    public List<List<Byte>> loadAllMotions() {
        String path = getCurrentPath();
        return loadAllMotions(path);
    }

    /**
     * Reads all bvh files from provided directory as map where key is file name and value is file data
     * @param path path to be scanned
     * @return all bvh files from scanned folder
     */
    public Map<String, String> loadAllBvh(String path) {
        Map<String, String> result = new HashMap<>();
        File folder = new File(path);
        File[] files = folder.listFiles();
        if (files == null) {
            return new HashMap<>();
        }

        Arrays.stream(files)
                .filter(file -> file.getName().endsWith(".bvh"))
                .forEach(file -> {
                    try {
                        result.put(file.getName(), Files.readString(file.toPath(), StandardCharsets.UTF_8));
                    } catch (IOException ignored) { }
                });
        return result;
    }

    /**
     * Reads all bvh files from application's run directory as map where key is file name and value is file data
     * @return all bvh files from application's run directory
     */
    public Map<String, String> loadAllBvh() {
        String path = getCurrentPath();
        return loadAllBvh(path);
    }

    /**
     * Reads bones map
     * @return bones map
     */
    public Map<Integer, Integer> loadBonesMap() {
        Map<Integer, Integer> result = new HashMap<>();
        File mapFile = new File(getCurrentPath() + "map.txt");
        try {
            FileUtils.readLines(mapFile, StandardCharsets.UTF_8).forEach(line -> {
                String[] data = line.split(": ");
                try {
                    result.put(Integer.parseInt(data[1]), Integer.parseInt(data[0]));
                } catch (NumberFormatException e) {
                    logger.warn("mapping " + Arrays.toString(data) + " is invalid and will be skipped");
                }
            });
        } catch (FileNotFoundException e) {
            throw new FileIOException("map.txt file is not present");
        } catch (IOException e) {
            throw new FileIOException("IO error occurred");
        }
        return result;
    }

    /**
     * Saves motion to application's run directory
     * @param motion motion to be saved
     * @param fileName name of file
     */
    public void saveMotion(Motion motion, String fileName) {
        String path = getCurrentPath();

        try {
            File directory = new File(path);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(path + fileName.replaceAll("\\..*", "") + ".mot"); //todo regex is questionable
            FileUtils.writeByteArrayToFile(file, motionFileGenerator.generateAsByteArray(motion));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Reads all bvh files from path to folder as map where key is file name and value is file data
     * @param path path to be scanned
     * @return all bvh files from scanned folder
     */
    public Map<String, String> loadAllRawAnimationFiles(String path) {
        Map<String, String> result = new HashMap<>();
        File folder = new File(path);
        File[] files = folder.listFiles();
        if (files == null) {
            return new HashMap<>();
        }

        Arrays.stream(files)
                .filter(file -> file.getName().endsWith(".ran"))
                .forEach(file -> {
                    try {
                        result.put(file.getName(), Files.readString(file.toPath(), StandardCharsets.UTF_8));
                    } catch (IOException ignored) { }
                });
        return result;
    }

    /**
     * Reads all bvh files from application's run directory as map where key is file name and value is file data
     * @return all bvh files from application's run directory
     */
    public Map<String, String> loadAllRawAnimationFiles() {
        return loadAllRawAnimationFiles(getCurrentPath());
    }

    private String getCurrentPath() {
        String path = Application.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        return URLDecoder.decode(path, StandardCharsets.UTF_8)
                .replaceAll(new File(path).getName(), "");
    }
}
