package com.adelean.elasticsearch.zip;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipExtractor {

    /**
     * Unzips a ZIP file to the specified destination directory.
     *
     * @param zipFilePath   the path to the ZIP file
     * @param destDirectory the directory to unzip to
     * @throws IOException if an I/O error occurs
     */
    public static void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) destDir.mkdirs();

        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();
                if (entry.isDirectory()) {
                    new File(filePath).mkdirs();
                } else {
                    extractFile(zipIn, filePath);
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        } catch (IOException e) {
            System.err.println("Error unzipping file: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Extracts a file from the ZIP input stream to the specified file path.
     *
     * @param zipIn    the ZIP input stream
     * @param filePath the file path to extract to
     * @throws IOException if an I/O error occurs
     */
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        File file = new File(filePath);
        new File(file.getParent()).mkdirs();

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = zipIn.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.err.println("Error extracting file: " + e.getMessage());
            throw e;
        }
    }
}