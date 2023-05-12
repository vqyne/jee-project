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
import model.Session;
import model.Site;
import model.TypeSession;
import model.Discipline;
import model.CategorieSession;
import model.CategorieSite;
import database.DisciplineDAO;
import database.SessionDAO;

import java.time.LocalDate;
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
			sessions = new ArrayList<Session>();
			sessions.add(new Session("ESC01", 
					new Date(), 
					LocalTime.of(14,0), 
					LocalTime.of(16,0), new Discipline("Escrime"), 
					new Site("Stade de France", "Saint Denis", CategorieSite.stade),
					"Escrime",
					TypeSession.medailles,
					CategorieSession.homme));
		}
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String json = gson.toJson(sessions);
		return json;
	}
	
}
