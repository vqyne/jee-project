package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import model.Athlete;
import model.Discipline;
import model.Genre;
import database.DisciplineDAO;

@Path("/discipline-controller")
public class DisciplineController {
	
	private DisciplineDAO disciplineDAO = new DisciplineDAO();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-disciplines")
	public String getDisciplines(@QueryParam("discipline") String nameDiscipline) {
		List<Discipline> disciplines = null;
		if(nameDiscipline != null && nameDiscipline.length() > 0) {
			
		} else {
			disciplines = disciplineDAO.findAll();
		}
		if(disciplines.isEmpty()) {
			disciplines = new ArrayList<Discipline>();
		}
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String json = gson.toJson(disciplines);
		return json;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/discipline-add/{name}/{accessible}")
	public boolean addDiscipline(@PathParam("name") String name, @PathParam("accessible") boolean accessible) {
		try {
			Discipline discipline = new Discipline(name, accessible);
			disciplineDAO.addDiscipline(discipline);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
