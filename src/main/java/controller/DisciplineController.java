package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
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
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-disciplines-duration")
	public String getDisciplinesDuration() {
		List<Discipline> disciplines = null;
		disciplines = disciplineDAO.findTopFiveDisciplinesByDuration();
		if(disciplines.isEmpty()) {
			disciplines = new ArrayList<Discipline>();
		}
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String json = gson.toJson(disciplines);
		return json;
	}
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/discipline-add")
	public boolean addDiscipline(@FormParam("name") String name, @FormParam("accessible") String accessible) {
		boolean accessibleBooolean = false;
		
		if(accessible.equals("1")) {
			accessibleBooolean = true;
		}
		
		try {
			Discipline discipline = new Discipline(name, accessibleBooolean);
			disciplineDAO.addDiscipline(discipline);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/discipline-edit")
	public boolean editDiscipline(@FormParam("name") String name, @FormParam("newName") String newName) {
		try {
			disciplineDAO.editDiscipline(name,newName);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@DELETE
	@Path("/delete-discipline/{name}")
	public boolean deleteDiscipline(@PathParam("name") String name) {
	    try {
	        disciplineDAO.removeDiscipline(name);
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}
