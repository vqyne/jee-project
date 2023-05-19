package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import utils.LocalTimeAdapter; // Import the LocalTimeAdapter class
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import model.Session;
import model.Site;
import model.TypeSession;
import model.Discipline;
import model.CategorieSession;
import model.CategorieSite;
import database.DisciplineDAO;
import database.SessionDAO;

import java.time.LocalTime;

@Path("/session-controller")
public class SessionController {
	
	private SessionDAO sessionDAO = new SessionDAO();
	private DisciplineDAO disciplineDAO = new DisciplineDAO();
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-sessions")
	public String getSessions(@QueryParam("discipline") String nameDiscipline) {
		List<Session> sessions = null;
		
		if(nameDiscipline != null && nameDiscipline.length() > 0) {
			Discipline dis = disciplineDAO.findByString(nameDiscipline);
			sessions = sessionDAO.findByDiscipline(dis);
		} else {
			sessions = sessionDAO.findAll();
		}
		if(sessions.isEmpty()) {
			System.out.println("Base vide");
			sessions = new ArrayList<Session>();
		}
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter()); // Register the LocalTimeAdapter
		Gson gson = builder.create();
		String json = gson.toJson(sessions);
		return json;
	}
		
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-categories")
	public String getCategories() {
	    List<CategorieSession> categories = Arrays.asList(CategorieSession.values());
	    
	    Gson gson = new Gson();
	    String json = gson.toJson(categories);
	    
	    return json;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-types")
	public String getTypes() {
	    List<TypeSession> types = Arrays.asList(TypeSession.values());
	    
	    Gson gson = new Gson();
	    String json = gson.toJson(types);
	    
	    return json;
	}
	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-sessions-code")
	public String getSessionsCode(@QueryParam("code") String code) {
		List<Session> sessions = null;
		if(code != null && code.length() > 0) {
			sessions = sessionDAO.findByCode(code);
		} 
		
		Gson gson = new GsonBuilder()
		        .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
		        .create();		
		String json = gson.toJson(sessions);
		return json;
	}
	
	/*@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/session-add")
	public boolean addSession(
	        @FormParam("code") String sessionCode,
	        @FormParam("date") Date sessionDate,
	        @FormParam("fromHour") LocalTime sessionFromHour,
	        @FormParam("toHour") LocalTime sessionToHour,
	        @FormParam("discipline") String discipline,
	        @FormParam("site") String site,
	        @FormParam("description") String sessionDescription,
	        @FormParam("type") String sessionType,
	        @FormParam("category") String sessionCategory
	) {
	    try {
	        Session session = new Session(
	                sessionCode,
	                sessionDate,
	                sessionFromHour,
	                sessionToHour,
	                Discipline.valueOf(discipline),
	                Site.valueOf(site),
	                sessionDescription,
	                TypeSession.valueOf(sessionType),
	                CategorieSession.valueOf(sessionCategory)
	        );
	        sessionDAO.addSession(session);
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}*/

	
}
