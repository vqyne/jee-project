package controller;

import java.io.StringReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Athlete;
import model.Discipline;
import model.Genre;
import database.AthleteDAO;
import database.DisciplineDAO;

/**
 * Contrôleur de la classe Athlète
 * Permet de CRUD sur les athlètes
 */
@Path("/athlete-controller")
public class AthleteController {
	
	//initialisation des DAO nécessaires
	private AthleteDAO athleteDAO = new AthleteDAO();
	private DisciplineDAO disciplineDAO = new DisciplineDAO();

	/**
	 * Méthode permettant de faire le lien entre la vue et la base de données
	 * Retourne tous les athlètes présents en base de données
	 * Plusieurs paramètres possibles en cas de pagination ou de recherche par discipline
	 * @param nameDiscipline nom de la discipline si recherche par discipline
	 * @param limit nombre d'athlètes maximum à retourner (en cas de pagination)
	 * @param page page actuelle où se trouve l'utilisateur (pour ne pas retourner toujours les n (limit) premiers athlètes
	 * @return une liste d'athlètes au format JSON
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-athletes")
	public String getAthletes(@QueryParam("discipline") String nameDiscipline,@QueryParam("limit") String limit, @QueryParam("page") String page) {
		List<Athlete> athletes = null;
		if(nameDiscipline != null && nameDiscipline.length() > 0) {
			athletes = athleteDAO.findByDiscipline(nameDiscipline);
		} else {
			if(limit != null && page != null) {
				athletes = athleteDAO.findAllPagination(Integer.parseInt(limit),Integer.parseInt(page));
			} else {
				athletes = athleteDAO.findAll();
			}
		}
		if(athletes.isEmpty()) {
			athletes = new ArrayList<Athlete>();
			//Au cas où la base de données est vide : permet de tester si la connexion fonctionne néanmoins
			athletes.add(new Athlete(1,"DELAUNAY","Gurwan","France",new Date(),Genre.homme,new Discipline("Football")));
		}
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String json = gson.toJson(athletes);
		return json;
	}
	
	/**
	 * Méthode GET permettant de retourner les athlètes ayant un nom ressemblant à name passé en argument
	 * Cette méthode nous est utile pour notre fonctionnalité de recherche (barre de recherche)
	 * La méthode va appeler la méthode associée du DAO Athlète
	 * @param name nom complet ou partiel que l'on recherche dans notre barre de recherche d'athlètes
	 * @return la liste d'athlètes ayant un nom/prénom semblable à l'argument sous format JSON
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-athletes-name")
	public String getAthletesName(@QueryParam("name") String name) {
		List<Athlete> athletes = null;
		if(name != null && name.length() > 0) {
			athletes = athleteDAO.findByName(name);
		} 
		
		if(athletes == null || athletes.isEmpty()) {
			athletes = new ArrayList<Athlete>();
		}
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String json = gson.toJson(athletes);
		return json;
	}
	
	
	/**
	 * Méthode permettant d'appeler la fonction traitant le fichier CSV et d'insérer dans la base de données, les athlètes trouvés.
	 * @param csvInputStream contenu du fichier CSV sous format texte
	 * @return Réponse HTTP indiquant la réussite ou non de l'opération
	 */
    @POST
    @Path("/import-athletes-from-csv")
    public Response importAthletesFromCSV(String csvInputStream) {
        try {
        	this.csvToDatabase(csvInputStream);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Méthode privée appelée par importAthletesFromCSV
     * Cette méthode va convertir le contenu de notre fichier CSV en objets Athlète et les insérer dans la base de données
     * @param csv contenu du fichier CSV sous format texte
     */
    private void csvToDatabase(String csv) {
    	AthleteDAO athleteDAO = new AthleteDAO();
        try {
        	StringReader csvStream = new StringReader(csv);
            CSVReader reader = new CSVReaderBuilder(csvStream).withSkipLines(4).build(); // Skip the header line
            List<String[]> lines = reader.readAll();
            for (String[] line : lines) {
            	if(line.length < 3) {
            		break;
            	}
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
                
                String genreString = (line[4]).trim();
                Genre genre = Genre.valueOf(genreString.toLowerCase());
                String sport = line[5];
                
                Discipline discipline = disciplineDAO.findByString(sport.trim());
                
                if(discipline == null) {
                	System.out.println("Erreur : La discipline " + sport + " n'existe pas");
                }
                
                Athlete athlete = new Athlete(prenom,nom,pays,discipline);
                athlete.setGenre(genre);
                athlete.setBirthdate(birthdate);
                athleteDAO.addAthlete(athlete);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
