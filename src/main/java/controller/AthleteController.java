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

@Path("/athlete-controller")
public class AthleteController {
	
	private AthleteDAO athleteDAO = new AthleteDAO();
	private DisciplineDAO disciplineDAO = new DisciplineDAO();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-athletes")
	public String getAthletes(@QueryParam("discipline") String nameDiscipline) {
		List<Athlete> athletes = null;
		if(nameDiscipline != null && nameDiscipline.length() > 0) {
			athletes = athleteDAO.findByDiscipline(nameDiscipline);
		} else {
			athletes = athleteDAO.findAll();
		}
		if(athletes.isEmpty()) {
			athletes = new ArrayList<Athlete>();
			athletes.add(new Athlete(1,"DELAUNAY","Gurwan","France",new Date(),Genre.homme,new Discipline("Football")));
		}
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String json = gson.toJson(athletes);
		return json;
	}
	
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
