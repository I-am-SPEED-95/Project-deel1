import gebruikers.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

class Page{
    public void loadPage(Scanner scanner){
        System.out.println("Loads the current page");
    }

    public String encodeReview(String review)
    {
        String encodedReview = review;
        encodedReview = encodedReview.replace("-", "@");
        encodedReview = encodedReview.replace("_", "-");
        encodedReview = encodedReview.replace(" ", "_");

        return encodedReview;
    }

    public String decodeReview(String encodedReview)
    {
        String decodedReview = encodedReview;

        decodedReview = decodedReview.replace("_", " ");
        decodedReview = decodedReview.replace("-", "_");
        decodedReview = decodedReview.replace("@", "-");

        return  decodedReview;
    }
}
class RankedPage extends Page {
    public void loadPage(Scanner scanner){
        //games inladen van gameloader
        GameLoader gameLoader = new GameLoader();
        //games als string[] in een nieuwe arraylist<String[]> stoppen
        ArrayList<String[]> gamesList = gameLoader.loadGames();

        //maakt nieuwe reviewLoaderOBJECT die REVIEWOBJECT terug geeft
        ReviewLoader reviewLoader = new ReviewLoader();
        //laad reviews in een nieuwe ArrayList<Review> die alleen review objecten aanneemt
        ArrayList<Review> reviews = reviewLoader.loadReviews();

        //maak een nieuwe arraylist die alleen GAME OBJECTEN mag innemen
        ArrayList<Game> rankedList = new ArrayList<>();

        //voegt alle reviews toe aan de juiste assigned game
        for(String[] s : gamesList){
            //gameList array uitlezen als string en overzetten naar Game Object
            Game game = new Game(Integer.valueOf(s[0]), s[1], s[2], Double.valueOf(s[3]), Double.valueOf(s[4]));

            //door reviews kijken welke bij de current game horen
            for(Review r : reviews){
                //zo ja: voeg de review toe aan de ReviewArray in Game
                if(r.getName().equals(game.getTitle())){
                    game.addReview(r);
                }
            }
            //Game object met reviews toevoegen aan nieuwe array
            rankedList.add(game);
        }

        //FILTER FUNCTIE (COEN PLS NIET NAAR MIJN SPAGHETTI-CODE KIJKEN TY)
        Set<String> genreList = new HashSet<String>();
        ArrayList<String> genre = new ArrayList<>();
        for(Game game : rankedList){
            genreList.add(game.getGenre());
        }
        for(String s : genreList){
            genre.add(s);
        }

        int count = 2;
        System.out.println("Filter by genre: \n1. None");
        for(String s : genre){
            System.out.println(count + ". " + s);
            count++;
        }

        String genreInput = scanner.nextLine();
        ArrayList<Game> games = new ArrayList<>();
        if(genreInput.equals("1")){
            //uhhh weet niet precies hoe dit werkt maar het sort de arraylist op basis van een object Field, in dit geval TotalGameScore(https://www.bezkoder.com/java-sort-arraylist-of-objects/)
            Collections.sort(rankedList, Comparator.comparing(Game::getTotalGameScore));
            //sorteert op laagste boven dus hier reverse ik de list
            Collections.reverse(rankedList);
            games = rankedList;
        } else if(Integer.valueOf(genreInput) - 2 < genre.size()) {
            String genreKeuze = genre.get(Integer.valueOf(genreInput) - 2);
            for(int i = 0; i < rankedList.size(); i++){
                if(rankedList.get(i).getGenre().equals(genreKeuze)){
                    games.add(rankedList.get(i));
                }
            }
        } else {
            System.out.println("Ongeldige Invoer");
        }

        //loops door filtered list en doet wat simple system.out.printf's
        for(Game g : games){
            System.out.printf("ID: %d\nTitle: %s\nGenre: %s\nTotal Score: %.2f\nPrice: %.2f\nDiscount: %.2f\n", g.getId(), g.getTitle(), g.getGenre(), g.getTotalGameScore(), g.getPrice(), g.getDiscountPrice());
            System.out.println();
            //print "no reviews available" als er geen reviews zijn gevonden
            if(g.getReviews().size() == 0){
                System.out.printf(
                        "┌────────────────────────────────────┐\n" +
                                "│ No Reviews Available               │\n" +
                                "├────────────────────────────────────┤\n" +
                                "│ Game Title: [%s]             \n" +
                                "│                                    │\n" +
                                "│ Sorry, there are no reviews        │\n" +
                                "│ available for this game yet.       │\n" +
                                "│                                    │\n" +
                                "│ Be the first to share your         │\n" +
                                "│ experience and thoughts about      │\n" +
                                "│ this game!                         │\n" +
                                "└────────────────────────────────────┘\n", g.getTitle());
            }
            for(Review r : g.getReviews()){
                System.out.printf(
                        "┌────────────────────────────────────┐\n" +
                                "│ Review van [%s]             \n" +
                                "├────────────────────────────────────┤\n" +
                                "│ Gameplay Score:  [%d]/10             \n" +
                                "│ Graphics Score:  [%d]/10             \n" +
                                "│ Storyline Score: [%d]/10            \n" +
                                "│                                    │\n" +
                                "│ Totale Score:    [%.1f]/10 \n" +
                                "├────────────────────────────────────┤\n" +
                                "│ Toelichting:                       │\n" +
                                "│ [%s]\n" +
                                "└────────────────────────────────────┘\n", r.getName(), r.getGameplayScore(), r.getGraphicsScore(), r.getStorylineScore(), r.getTotalScore(), r.getTekstReview());
                System.out.println();
            }
            System.out.println();
        }
    }
}

class ReviewPage extends Page {
    public void loadPage(Scanner scanner){
        //games inladen van Class GameLoader
        GameLoader gameLoader = new GameLoader();
        //games in een nieuwe arraylist stoppen
        ArrayList<String[]> gamesList = gameLoader.loadGames();

        for(String[] s : gamesList){
            System.out.println(s[0] + ". " + s[1]);
        }
        int id = scanner.nextInt();


        System.out.println("Gameplay Score: \n");
        int gameplayScore = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Graphics Score: \n");
        int graphicsScore = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Storyline Score: \n");
        int storylineScore = scanner.nextInt();
        scanner.nextLine();

        double res = (gameplayScore + graphicsScore + storylineScore) / 3;

        System.out.println("Beschrijving: ");
        String beschrijving = scanner.nextLine();
        beschrijving = encodeReview(beschrijving);

        //User input opslaan klaarrrrr!

        Review review = new Review(id, gamesList.get(id - 1)[1], gameplayScore, graphicsScore, storylineScore, res, beschrijving);

        //review object aan ReviewLoader geven
        ReviewLoader reviewLoader = new ReviewLoader();
        reviewLoader.writeReview(review);

        //Enquete functionaliteit
        //vragen of de gebruiker enquete wilt invullen
        System.out.println("Enquête invullen?(y/n): \n");
        String enqueteKeuze = scanner.nextLine();

        if(enqueteKeuze.equals("y"))
        {
            Enquete enquete = new Enquete();
            enquete.loadVragen();
            enquete.stelVragen();
        }
    }

}

class UitverkoopPage extends Page {
    public void loadPage(Scanner scanner){
        GameLoader gameLoader = new GameLoader();
        ArrayList<String[]> games = gameLoader.loadGames();
        System.out.println("Lijst van afgeprijsde games: \n");
        for(String[] game : games){
            if(Double.valueOf(game[4]) - Double.valueOf(game[3]) == 0.0){
                System.out.printf("%s | Prijs: $%s > Free!%n", game[1], ("\u001B[9m" + game[3] + "\u001B[0m"));
            }
            else if(Double.valueOf(game[4]) < Double.valueOf(game[3]) && Double.valueOf(game[4]) != 0){
                System.out.printf("%s | Prijs: $%s > $%.2f%n", game[1], ("\u001B[9m" + game[3] + "\u001B[0m"), (Double.valueOf(game[3]) - Double.valueOf(game[4])));
            }
        }
    }
}


class ManagerPage extends Page {
    private Scanner scanner;
    private EnqueteLoader enqueteLoader;

    public ManagerPage(Scanner scanner) {
        this.scanner = scanner;
        this.enqueteLoader = new EnqueteLoader();  // Zorg ervoor dat EnqueteLoader correct is geïmplementeerd
    }

    private int promptForInt(String prompt, int min, int max) {
        int input;
        do {
            System.out.println(prompt);
            while (!scanner.hasNextInt()) {
                System.out.println("Voer alstublieft een geldig nummer in!");
                scanner.next(); // Verwijder de foutieve invoer
            }
            input = scanner.nextInt();
            scanner.nextLine(); // Clear the newline
            if (input < min || input > max) {
                System.out.println("Selecteer een geldige optie tussen " + min + " en " + max + ".");
            }
        } while (input < min || input > max);
        return input;
    }

    @Override
    public void loadPage(Scanner scanner) {
        int actie;
        do {
            System.out.println("Welkom Manager! Kies een actie:");
            System.out.println("1. Toon ingevulde enquêtes per week");
            System.out.println("2. Toon enquête evaluaties");
            System.out.println("3. Voeg een andere manager toe");
            System.out.println("4. Uitloggen");

            actie = promptForInt("Voer uw keuze in: ", 1, 4);

            switch (actie) {
                case 1:
                    toonEnquetesPerWeek();
                    break;
                case 2:
                    toonEnqueteEvaluaties();
                    break;
                case 3:
                    voegManagerToe();
                    break;
                case 4:
                    System.out.println("Uitloggen...");
                    return;
                default:
                    System.out.println("Ongeldige keuze.");
            }
        } while (actie != 4);
    }

    private void toonEnquetesPerWeek() {
        List<File> enquetes = enqueteLoader.getEnquetesFromLastWeek();
        System.out.println("Aantal ingevulde enquêtes in de afgelopen week: " + enquetes.size());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        for (File file : enquetes) {
            System.out.println(file.getName() + " - Laatste wijziging: " + sdf.format(new Date(file.lastModified())));
        }
    }

    private void toonEnqueteEvaluaties() {
        List<File> evaluaties = enqueteLoader.getEvaluatedEnquetes();
        if (evaluaties.isEmpty()) {
            System.out.println("Er zijn geen geëvalueerde enquêtes beschikbaar.");
            return;
        }

        System.out.println("Beschikbare geëvalueerde enquêtes:");
        for (int i = 0; i < evaluaties.size(); i++) {
            System.out.println((i + 1) + ". " + evaluaties.get(i).getName());
        }

        int keuze;
        do {
            keuze = promptForInt("Kies een evaluatie om te bekijken of 0 om terug te gaan:", 0, evaluaties.size());
            if (keuze > 0) {
                File gekozenEvaluatie = evaluaties.get(keuze - 1);
                printFileContents(gekozenEvaluatie);
            } else if (keuze == 0) {
                System.out.println("Terug naar het Menu.");
            }
        } while (keuze != 0);
    }

    private void printFileContents(File file) {
        System.out.println("Inhoud van de enquete:");
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fout bij het openen van het bestand: " + e.getMessage());
        }
    }

    private void voegManagerToe() {
        System.out.println("Nieuwe manager gebruikersnaam:");
        String username = scanner.nextLine();
        System.out.println("Nieuwe manager wachtwoord:");
        String password = scanner.nextLine();

        Manager nieuweManager = new Manager(username, password);
        if (nieuweManager.registreer(username, password)) {
            System.out.println("Nieuwe manager succesvol toegevoegd.");
        } else {
            System.out.println("Fout bij het toevoegen van nieuwe manager.");
        }
    }

}


class GamesBeheer extends Page {
    public void loadPage(Scanner scanner) {

        //import gameloader voor functionaliteiten je snapt het wel
        GameLoader gameLoader = new GameLoader();


            System.out.println("Games beheer Menu:\n1. Game toevoegen\n2. Game verwijderen\n3. Prijzen aanpassen");
            String keuze = scanner.nextLine();
            if (keuze.equals("1")) {
                System.out.println("Game Title: \n");
                String title = scanner.nextLine();
                title = encodeReview(title);
                System.out.println("Genre: \n");
                String genre = scanner.nextLine();
                System.out.println("Prijs: \n");
                String prijs = scanner.nextLine();
                System.out.println("Kortingsprijs: \n");
                String korting = scanner.nextLine();
                //user input pakken en in een nieuwe game object stoppen
                Game g = new Game(title, genre, Double.valueOf(prijs), Double.valueOf(korting));
                //die game object doorgeven aan gameloader object die het vervolgend append aan het games.txt text bestand
                gameLoader.writeGame(g);
            } else if (keuze.equals("2")) {
                ArrayList<String[]> games = gameLoader.loadGames();
                System.out.println("Welke game wilt u verwijderen?");
                for (String[] s : games) {
                    System.out.printf("%s. %s\n", s[0], s[1]);
                }
                String verwijderInput = scanner.nextLine();
                String rmGame = games.get(Integer.valueOf(verwijderInput) - 1)[1];

                //verwijderen van reviews van geslecteerde game
                ReviewLoader reviewLoader = new ReviewLoader();
                ArrayList<Review> reviews = reviewLoader.loadReviews();
                ArrayList<Review> newReviews = new ArrayList<>();

                for(int i = 0; i < reviews.size(); i++){
                    if(!reviews.get(i).getName().equals(rmGame)){
                        newReviews.add(reviews.get(i));
                    }
                }

                reviewLoader.deleteReview(newReviews);

                //verwijderd game met index de user input
                games.remove(Integer.valueOf(verwijderInput) - 1);
                //geeft games lijst door aan gameloader
                gameLoader.removeGame(games);

            } else if (keuze.equals("3")) {
                //game list pakken uit gameloader
                ArrayList<String[]> games = gameLoader.loadGames();
                System.out.println("Welke game prijs wilt u aanpassen?:\n");
                //games showen plus ID(index) voor de user om te selecteren
                for (String[] s : games) {
                    System.out.printf("%s. %s Prijs: %s\n", s[0], s[1], s[3]);
                }
                //user input pakken bla bla bla...
                String input = scanner.nextLine();
                //user input voor nieuwe prijs zZzZzZzZzZz....
                System.out.println("Nieuwe Prijs?: \n");
                String newPriceInput = scanner.nextLine();
                System.out.println("Nieuwe Kortings Prijs?: \n");
                String newKortingInput = scanner.nextLine();
                for (String[] s : games) {
                    //prijs veranderen in games lijst
                    if (s[0].equals(input)) {
                        s[3] = newPriceInput;
                        s[4] = newKortingInput;
                    }
                }
                //lijst doorgeven je weet het at this point wel doei 👋👋👋
                gameLoader.setGamePrice(games);
            }
    }
}

class MedewerkerPage extends Page {
    private String medewerkerNaam;
    private Scanner scanner;
    private EnqueteLoader enqueteLoader;

    public MedewerkerPage(String medewerkerNaam, Scanner scanner) {
        this.medewerkerNaam = medewerkerNaam;
        this.scanner = scanner;
        this.enqueteLoader = new EnqueteLoader();  // Zorg ervoor dat EnqueteLoader correct is geïmplementeerd
    }

    @Override
    public void loadPage(Scanner scanner) {
        int actie;
        do {
            System.out.println("Welkom Medewerker! Kies een actie:");
            System.out.println("1. Enquête beoordelen");
            System.out.println("2. Games beheren");
            System.out.println("3. Uitloggen");

            actie = promptForInt("Voer uw keuze in: ", 1, 3);

            switch (actie) {
                case 1:
                    beoordeelEnquete();
                    break;
                case 2:
                    GamesBeheer gamesBeheer = new GamesBeheer();
                    gamesBeheer.loadPage(scanner);
                    break;
                case 3:
                    System.out.println("Uitloggen...");
                    return;
                default:
                    System.out.println("Ongeldige keuze.");
            }
        } while (actie != 3);
    }

    private void beoordeelEnquete() {
        List<File> availableEnquetes = enqueteLoader.getAvailableEnquetes();

        if (availableEnquetes.isEmpty()) {
            System.out.println("Er zijn geen beschikbare enquêtes om te beoordelen.");
            return;
        }

        System.out.println("Beschikbare enquêtes:");
        for (int i = 0; i < availableEnquetes.size(); i++) {
            System.out.println((i + 1) + ". " + availableEnquetes.get(i).getName());
        }

        int keuze = promptForInt("Kies een enquête om te beoordelen door het nummer in te voeren:", 1, availableEnquetes.size());

        File gekozenEnquete = availableEnquetes.get(keuze - 1);
        printFileContents(gekozenEnquete);

        System.out.println("Voer uw evaluatie in:");
        String evaluatie = scanner.nextLine();

        int cijfer = promptForInt("Geef een cijfer voor deze enquête (1 tot 10):", 1, 10);

        System.out.println("Actie vereist:\n1. Geen actie\n2. Terugbellen\n3. Overleggen met manager");
        int actieKeuze = promptForInt("Kies een actie:", 1, 3);
        String[] acties = {"Geen actie", "Terugbellen", "Overleggen met manager"};
        String actie = acties[actieKeuze - 1];

        enqueteLoader.addMedewerkerEvaluation(gekozenEnquete.getName(), medewerkerNaam, evaluatie, cijfer, actie);

        System.out.println("Uw evaluatie is opgeslagen.");
    }



    private void printFileContents(File file) {
        System.out.println("Inhoud van de enquête:");
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fout bij het openen van het bestand: " + e.getMessage());
        }
    }

    private int promptForInt(String prompt, int min, int max) {
        int input;
        do {
            System.out.println(prompt);
            while (!scanner.hasNextInt()) {
                System.out.println("Voer alstublieft een geldig nummer in!");
                scanner.next(); // Verwijder de foutieve invoer
            }
            input = scanner.nextInt();
            scanner.nextLine(); // Clear the newline
            if (input < min || input > max) {
                System.out.println("Selecteer een geldige optie tussen " + min + " en " + max + ".");
            }
        } while (input < min || input > max);
        return input;
    }
}

class LoginPage extends Page {
    public void loadPage(Scanner scanner) {
        System.out.println("U bent:\n1. Medewerker\n2. Manager\n3. Terug");
        String roleChoice = scanner.nextLine();

        switch (roleChoice) {
            case "1":
                medewerkerLogin(scanner);
                break;
            case "2":
                managerLogin(scanner);
                break;
            case "3":
                return;  // Terug naar het AppMenu
            default:
                System.out.println("Ongeldige keuze. Probeer opnieuw.");
                loadPage(scanner);
        }
    }

    private void medewerkerLogin(Scanner scanner) {
        System.out.println("1. Inloggen\n2. Registreren\n3. Terug");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                String medewerkerNaam = login(scanner, false);
                if (medewerkerNaam != null) {
                    new MedewerkerPage(medewerkerNaam, scanner).loadPage(scanner);
                }
                break;
            case "2":
                medewerkerRegistreren(scanner);
                break;
            case "3":
                return;
            default:
                System.out.println("Ongeldige keuze. Probeer opnieuw.");
                medewerkerLogin(scanner);
        }
    }




    private void managerLogin(Scanner scanner) {
        System.out.println("1. Inloggen\n2. Terug");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                String managerNaam = login(scanner, true);
                if (managerNaam != null) {
                    new ManagerPage(scanner).loadPage(scanner); // Zorg ervoor dat managerNaam wordt doorgegeven
                }
                break;
            case "2":
                return;
            default:
                System.out.println("Ongeldige keuze. Probeer opnieuw.");
                managerLogin(scanner);
        }
    }

    private String login(Scanner scanner, boolean isManager) {
        System.out.println("Gebruikersnaam:");
        String username = scanner.nextLine();
        System.out.println("Wachtwoord:");
        String password = scanner.nextLine();

        GebruikerBeheer gebruikerBeheer = isManager ? new Manager(username, password) : new Medewerker(username, password);
        if (gebruikerBeheer.login(username, password)) {
            System.out.println("Succesvol ingelogd als " + (isManager ? "Manager." : "Medewerker."));
            return username;  // Geef de gebruikersnaam terug
        } else {
            System.out.println("Inloggegevens zijn onjuist.");
            return null;
        }
    }

    private void medewerkerRegistreren(Scanner scanner) {
        System.out.println("Gebruikersnaam:");
        String username = scanner.nextLine();
        System.out.println("Wachtwoord:");
        String password = scanner.nextLine();
        System.out.println("Manager code:");
        String managerCode = scanner.nextLine();

        if (!managerCode.equals("Admin123")) {
            System.out.println("Ongeldige manager code.");
            return;
        }

        Medewerker medewerker = new Medewerker(username, password);
        if (medewerker.registreer(username, password)) {
            System.out.println("Medewerker succesvol geregistreerd.");
        } else {
            System.out.println("Registratie mislukt.");
        }
    }
}


public class AppMenu {
    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        while (true) {
            displayMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    RankedPage rankedPage = new RankedPage();
                    rankedPage.loadPage(scanner);
                    break;
                case "2":
                    ReviewPage reviewPage = new ReviewPage();
                    reviewPage.loadPage(scanner);
                    break;
                case "3":
                    UitverkoopPage uitverkoopPage = new UitverkoopPage();
                    uitverkoopPage.loadPage(scanner);
                    break;
                case "4":
                    LoginPage loginPage = new LoginPage();
                    loginPage.loadPage(scanner);  // Gebruik hier een instantie van LoginPage
                    break;
                case "5":
                    System.out.println("Sluiten...");
                    return;
                default:
                    System.out.println("Verkeerde keuze. Probeer opnieuw");
                    break;
            }
        }
    }

    private void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Ranglijst");
        System.out.println("2. Review Schrijven");
        System.out.println("3. Uitverkooplijst");
        System.out.println("4. Inloggen");
        System.out.println("5. Sluiten");
        System.out.print("Kies een optie: \n");
    }
}
