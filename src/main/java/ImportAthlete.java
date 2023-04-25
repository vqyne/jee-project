import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.List;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import model.Athlete;
import model.Discipline;
import model.Genre;
import database.DisciplineDAO;
import database.AthleteDAO;

public class ImportAthlete {
	
    public static void importAthletesFromCSV(String csvFilePath) {
    	AthleteDAO athleteDAO = new AthleteDAO();
        try {
            CSVReader reader = new CSVReader(new FileReader(csvFilePath));
            String[] header = reader.readNext();
            List<String[]> lines = reader.readAll();
            for (String[] line : lines) {
                String nom = line[0];
                String prenom = line[1];
                String pays = line[2];
                
                String dateNaissance = line[3];
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date birthdate = null;
                
                try {
                	birthdate = dateFormat.parse(dateNaissance);
                } catch(ParseException e) {
                	e.printStackTrace();
                }
                
                String genreString = line[4];
                Genre genre = Genre.valueOf(genreString.toLowerCase());
                String sport = line[5];
                
                Discipline discipline = getDiscipline(sport);
                
                Athlete athlete = new Athlete(prenom,nom,pays,discipline);
                athlete.setGenre(genre);
                athlete.setBirthdate(birthdate);
                athleteDAO.addAthlete(athlete);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Discipline getDiscipline(String sport) {
    	DisciplineDAO disciplineDAO = new DisciplineDAO();
    	return disciplineDAO.findByString(sport);
    }
    
}
