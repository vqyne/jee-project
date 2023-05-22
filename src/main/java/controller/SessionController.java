package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Application;

import model.Session;
import model.Site;
import model.TypeSession;
import model.Discipline;
import model.CategorieSession;
import model.CategorieSite;
import database.DisciplineDAO;
import database.SiteDAO;
import database.SessionDAO;

import java.time.LocalTime;

@Path("/session-controller")
public class SessionController {
	
	private SessionDAO sessionDAO = new SessionDAO();
	private DisciplineDAO disciplineDAO = new DisciplineDAO();
	private SiteDAO siteDAO = new SiteDAO();
	
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

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/session-add")
	public Response addSession(
	        @FormParam("code") String sessionCode,
	        @FormParam("date") String sessionDate,
	        @FormParam("fromHour") String sessionFromHour,
	        @FormParam("toHour") String sessionToHour,
	        @FormParam("discipline") String discipline,
	        @FormParam("site") String site,
	        @FormParam("description") String sessionDescription,
	        @FormParam("type") String sessionType,
	        @FormParam("category") String sessionCategory
	) {

	    int ret = -1;
	    try {
	        Discipline dis = disciplineDAO.findByString(discipline);
	        Site sit = siteDAO.findById(Integer.parseInt(site));
	        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	        Date date = dateFormat.parse(sessionDate);

	        String[] splitFromHour = sessionFromHour.split(":");
	        String[] splitToHour = sessionToHour.split(":");
	        if(splitToHour.length != 2 || splitFromHour.length != 2) {
		        return Response.status(Response.Status.BAD_REQUEST).build();
	        }

	        LocalTime from = LocalTime.of(Integer.parseInt(splitFromHour[0]), Integer.parseInt(splitFromHour[1]));
	        LocalTime to = LocalTime.of(Integer.parseInt(splitToHour[0]),Integer.parseInt(splitToHour[1]));
	        Session session = new Session(
	                sessionCode,
	                date,
	                from,
	                to,
	                dis,
	                sit,
	                sessionDescription,
	                TypeSession.valueOf(sessionType),
	                CategorieSession.valueOf(sessionCategory)
	        );

	        ret = sessionDAO.addSession(session);

	        if (ret == 0) {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	        } else if (ret == 2) {
	            return Response.status(Response.Status.CONFLICT).build();
	        } else {
	        	JsonObject responseJson = new JsonObject();
	            responseJson.addProperty("status", "success");
	            responseJson.addProperty("message", "Session ajoutée avec succès");

	            String jsonString = responseJson.toString();

	            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	    }
	}



	
}
