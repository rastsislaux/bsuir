package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Main {

    /* Работа со строками. Чтение исходной строки осуществлять из текстового файла,
    для результирующей строки применить стандартный алгоритм шифрования и записать
    в файл. При выполнении следующих заданий для вывода результатов создавать
    новую директорию и файл средствами класса File.
    30. Вывести в заданном тексте все слова, расположив их в алфавитном порядке.
     */

    public final static String path = "input.txt";

    public static String readFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        StringBuilder sb = new StringBuilder();

        String line = br.readLine();
        while (line != null) {
            sb.append(line)
              .append(" ");
            line = br.readLine();
        }
        br.close();
        return sb.toString();
    }

    public static String encode(String text) {
        return Base64.getEncoder()
                     .encodeToString(
                             text.getBytes(StandardCharsets.UTF_8)
                     );
    }

    public static void writeToFile(String path, String text) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write(text);
        bw.close();
    }

    public static void swap (String[] array, int i, int j) {
        String temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public static String sortWords(String text) {

        String[] words = text.split("[ .,:;]");
        for (int i = 0; i < words.length; i++) {
            for (int j = i + 1; j < words.length; j++) {
                if (words[i].compareTo(words[j]) > 0) {
                    swap(words, i, j);
                }
            }
        }
        return String.join(" ", words).strip();

    }

    public static void main(String[] args) {

        String fileContent;
        try { fileContent = readFile(path); }
        catch (IOException e) {
            System.out.println("Error while reading a file.");
            return;
        }

        System.out.println("Original content:\n" +
                fileContent);

        fileContent = sortWords(fileContent);
        System.out.println(
                "Sorted by alphabet:\n" +
                sortWords(fileContent));


        File dir = new File("result");
        if (dir.mkdir()) {
            System.out.println("Directory created successfully.");
        } else {
            System.out.println("Directory already exists.");
        }

        String encoded = encode(fileContent);
        try { writeToFile("result/output.txt", encoded); }
        catch (IOException e) {
            System.out.println("Failed to write to file.");
        }

    }
}
