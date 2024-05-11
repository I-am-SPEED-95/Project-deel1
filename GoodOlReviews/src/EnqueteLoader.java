import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class EnqueteLoader {
    private static final String DIRECTORY_PATH = "src/enqueteLijst/";

    public List<File> getAvailableEnquetes() {
        File folder = new File(DIRECTORY_PATH);
        File[] listOfFiles = folder.listFiles();
        List<File> availableFiles = new ArrayList<>();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && !file.getName().contains("-")) {
                    availableFiles.add(file);
                }
            }
        }
        return availableFiles;
    }

    public void writeEnquete(Enquete enquete) {
        // Gebruik de getNextEnqueteId methode om een uniek ID te krijgen
        int nextId = getNextEnqueteId();
        enquete.setId(nextId); // Stel het ID van de enquete in

        String fileName = "enquete" + nextId + ".txt";
        File file = new File(DIRECTORY_PATH + fileName);
        try (FileWriter writer = new FileWriter(file, false)) { // false to overwrite
            for (EnqueteVraag ev : enquete.getVragen()) {
                if (ev.getAntwoordGegeven() != null) {
                    writer.write(ev.getVraag() + " : " + ev.formatAntwoordGegeven() + "\n");
                }
            }
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMedewerkerEvaluation(String fileName, String medewerkerNaam, String evaluatie, int cijfer, String actie) {
        File oldFile = new File(DIRECTORY_PATH + fileName);
        File newFile = new File(DIRECTORY_PATH + fileName.replace(".txt", "-" + medewerkerNaam + ".txt"));

        try (Scanner scanner = new Scanner(oldFile);
             FileWriter writer = new FileWriter(newFile, false)) {  // Open new file for writing
            while (scanner.hasNextLine()) {
                writer.write(scanner.nextLine() + "\n");  // Copy old content to new file
            }
            // Add new evaluation data
            writer.write("Evaluatie van: " + medewerkerNaam + "\n");
            writer.write("Zijn evaluatie: " + evaluatie + "\n");
            writer.write("Cijfer voor deze enquête: " + cijfer + "\n");
            writer.write("Actie vereist: " + actie + "\n");
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Delete the old file if new file is successfully written
        if (newFile.exists() && oldFile.delete()) {
            System.out.println("Oude enquête verwijderd: " + oldFile.getName() + " en nieuwe opgeslagen.");
        } else {
            System.out.println("Fout bij het verwijderen van de oude enquête: " + oldFile.getName());
        }
    }

    public int getNextEnqueteId() {
        File folder = new File(DIRECTORY_PATH);
        File[] listOfFiles = folder.listFiles();
        int highestId = 0;

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().startsWith("enquete") && !file.getName().contains("-")) {
                    try {
                        // Extract the numeric part of the filename
                        String idStr = file.getName().replace("enquete", "").replace(".txt", "");
                        int id = Integer.parseInt(idStr);
                        if (id > highestId) {
                            highestId = id;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ongeldig bestandsformaat: " + file.getName());
                    }
                }
            }
        }
        return highestId + 1;
    }

    public List<File> getEnquetesFromLastWeek() {
        File folder = new File(DIRECTORY_PATH);
        File[] listOfFiles = folder.listFiles();
        List<File> recentFiles = new ArrayList<>();

        if (listOfFiles != null) {
            Date now = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            Date oneWeekAgo = calendar.getTime();

            for (File file : listOfFiles) {
                if (file.isFile() && file.lastModified() >= oneWeekAgo.getTime()) {
                    recentFiles.add(file);
                }
            }
        }
        return recentFiles;
    }

    public List<File> getEvaluatedEnquetes() {
        File folder = new File(DIRECTORY_PATH);
        File[] listOfFiles = folder.listFiles();
        List<File> evaluatedFiles = new ArrayList<>();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().contains("-")) {
                    evaluatedFiles.add(file);
                }
            }
        }
        return evaluatedFiles;
    }
}
