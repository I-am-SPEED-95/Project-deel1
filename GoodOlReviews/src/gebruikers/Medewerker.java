package gebruikers;

import java.io.*;
import java.util.Scanner;

public class Medewerker extends Gebruiker implements GebruikerBeheer{

    private static final String FILE_PATH = "src/gebruikers/MedewerkerLijst.txt";

    public Medewerker(String gebruikersnaam, String wachtwoord) {
        super(gebruikersnaam, wachtwoord);
    }

    @Override
    public boolean login(String gebruikersnaam, String wachtwoord) {
        // Lees uit het bestand en controleer de gebruikersnaam en het wachtwoord
        try (Scanner scanner = new Scanner(new File(FILE_PATH))) {
            while (scanner.hasNextLine()) {
                String[] credentials = scanner.nextLine().split(":");
                if (credentials[0].equals(gebruikersnaam) && credentials[1].equals(wachtwoord)) {
                    System.out.println("Werknemer ingelogd: " + gebruikersnaam);
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Het bestand kon niet gevonden worden.");
        }
        return false;
    }

    @Override
    public boolean registreer(String gebruikersnaam, String wachtwoord) {
        // Schrijf naar het bestand
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(gebruikersnaam + ":" + wachtwoord);
            return true;
        } catch (IOException e) {
            System.out.println("Er is een fout opgetreden bij het schrijven naar het bestand.");
            return false;
        }
    }
}

