package gebruikers;

public interface GebruikerBeheer {
    boolean login(String gebruikersnaam, String wachtwoord);
    boolean registreer(String gebruikersnaam, String wachtwoord);
}



