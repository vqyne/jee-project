package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import utils.LocalTimeAdapter; // Import the LocalTimeAdapter class

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
			sessions.add(new Session("BAS01", 
				    new Date(), 
				    LocalTime.of(14, 0), 
				    LocalTime.of(16, 0), 
				    new Discipline("Basketball"), 
				    new Site("AccorHotels Arena", "Paris", CategorieSite.stade),
				    "Basketball",
				    TypeSession.medailles,
				    CategorieSession.femme));

				sessions.add(new Session("JUD01", 
					new Date(), 
				    LocalTime.of(9, 0), 
				    LocalTime.of(11, 0), 
				    new Discipline("Judo"), 
				    new Site("Palais Omnisports", "Paris", CategorieSite.salle),
				    "Judo",
				    TypeSession.medailles,
				    CategorieSession.homme));

				sessions.add(new Session("TEN01", 
					new Date(), 
				    LocalTime.of(13, 30), 
				    LocalTime.of(15, 30), 
				    new Discipline("Tennis"), 
				    new Site("Stade Roland Garros", "Paris", CategorieSite.stade),
				    "Tennis",
				    TypeSession.medailles,
				    CategorieSession.femme));

				sessions.add(new Session("WEI01", 
					new Date(), 
				    LocalTime.of(10, 0), 
				    LocalTime.of(12, 0), 
				    new Discipline("Haltérophilie"), 
				    new Site("Gymnase Marcel Cerdan", "Saint Denis", CategorieSite.gymnase),
				    "Haltérophilie",
				    TypeSession.medailles,
				    CategorieSession.homme));

				sessions.add(new Session("FEN01", 
					new Date(), 
				    LocalTime.of(15, 30), 
				    LocalTime.of(17, 30), 
				    new Discipline("Escalade"), 
				    new Site("Le Dôme", "Paris", CategorieSite.salle),
				    "Escalade",
				    TypeSession.medailles,
				    CategorieSession.femme));

				sessions.add(new Session("GOL01", 
					new Date(), 
				    LocalTime.of(11, 0), 
				    LocalTime.of(13, 0), 
				    new Discipline("Golf"), 
				    new Site("Golf National", "Guyancourt", CategorieSite.golf),
				    "Golf",
				    TypeSession.medailles,
				    CategorieSession.homme));

				sessions.add(new Session("HAN01", 
					new Date(), 
				    LocalTime.of(16, 30), 
				    LocalTime.of(18, 30), 
				    new Discipline("Handball"), 
				    new Site("Palais des Sports", "Paris", CategorieSite.salle),
				    "Handball",
				    TypeSession.medailles,
				    CategorieSession.femme));

		}
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter()); // Register the LocalTimeAdapter
		Gson gson = builder.create();
		String json = gson.toJson(sessions);
		return json;
	}
	
}
