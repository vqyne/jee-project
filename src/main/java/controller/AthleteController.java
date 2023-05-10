package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import model.Athlete;
import model.Discipline;
import model.Genre;
import database.AthleteDAO;

@Path("/athlete-controller")
public class AthleteController {
	
	private AthleteDAO athleteDAO = new AthleteDAO();

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
	
}
