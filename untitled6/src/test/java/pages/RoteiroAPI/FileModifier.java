package pages.RoteiroAPI;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileModifier {

    public static void main(String[] args) {
        String inputFilePath = "C:\\Users\\zesch\\Downloads\\Average temperatures for selected c.txt";
        String outputFilePath = "C:\\Users\\zesch\\Downloads\\arquivo_modificado.txt";

        List<String> lines = readFromFile(inputFilePath);

        // Modificar as linhas para remover o conteúdo entre parênteses
        for (int i = 0; i < lines.size(); i++) {
            lines.set(i, modifyLine(lines.get(i)));
            lines.set(i, modifyLine2(lines.get(i)));
        }

        writeToFile(outputFilePath, lines);
    }

    public static List<String> readFromFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void writeToFile(String filePath, List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String modifyLine(String line) {
        // Remove o conteúdo entre parênteses, incluindo os próprios parênteses

        return line.replaceAll("\\(.*?\\)", "");
    }

    public static String modifyLine2(String line){
        return line.replace(".", ",");
    }
}
