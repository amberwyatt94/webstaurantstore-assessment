package com.assessment.utils;

import java.io.*;

public class FileHelper {

    //To read full text from given file available under Inpufiles folder
    public static String readFile(String fileName) {
        String text = "";
        String finalText = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            while ((text = br.readLine()) != null) {
                if (text.trim() != "")
                    finalText = finalText + text + "\n";
            }

            return finalText;
        } catch (Exception e) {
            return null;
        }
    }

    public static void createFolder(String folderName) {
        if (!new File(folderName).exists())
            new File(folderName).mkdirs();
    }

    public static void createFile(String filePath, String fileText) {
        //filePath = getNextFileName(filePath);
        try {
            if (!new File(filePath).exists())
                new File(filePath).createNewFile();

            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false));
            bw.write(fileText);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getNextFileName(String filePath) {
        String ext = filePath.substring(filePath.lastIndexOf('.'));
        int i = 0;
        while (new File(filePath).exists()) {
            if (i == 0)
                filePath = filePath.replace(ext, ++i + ext);
            else
                filePath = filePath.replace(i + ext, ++i + ext);
        }
        return filePath;
    }
}