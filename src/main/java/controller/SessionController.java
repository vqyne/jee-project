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
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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

/**
 * Contrôleur de la classe Session
 * Permet de CRUD sur les sessions
 */
@Path("/session-controller")
public class SessionController {
	
	//initialisation des DAO nécessaires
	private SessionDAO sessionDAO = new SessionDAO();
	private DisciplineDAO disciplineDAO = new DisciplineDAO();
	private SiteDAO siteDAO = new SiteDAO();
	
	/**
	 * Méthode GET permettant de retourner toutes les sessions
	 * @param nameDiscipline nom de la discipline si on veut retourner seulement les sessions d'une discipline
	 * @return liste des sessions sous format JSON
	 */
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
		
	/**
	 * Méthode GET permettant de retourner la liste des catégories présentes dans le modèle dans l'énumération CategorieSession
	 * @return la liste des catégories sous format JSON
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-categories")
	public String getCategories() {
	    List<CategorieSession> categories = Arrays.asList(CategorieSession.values());
	    
	    Gson gson = new Gson();
	    String json = gson.toJson(categories);
	    
	    return json;
	}
	
	/**
	 * Méthode GET permettant de retourner les types de session présents dans le modèle dans l'énumération TypeSession
	 * @return les différents types de session
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-types")
	public String getTypes() {
	    List<TypeSession> types = Arrays.asList(TypeSession.values());
	    
	    Gson gson = new Gson();
	    String json = gson.toJson(types);
	    
	    return json;
	}
	

	/**
	 * Méthode GET permettant de chercher une session par code : fonctionnalité barre de recherche
	 * @param code code recherché dans la barre de recherche
	 * @return la liste des sessions ayant un code semblable à l'argument fourni sous format JSON
	 */
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
	
	/**
	 * Méthode POST permettant d'ajouter une session
	 * @param sessionCode code de la session (CLEF PRIMAIRE)
	 * @param sessionDate date de la session
	 * @param sessionFromHour heure de début de la session (vérification de compatibilité)
	 * @param sessionToHour heure de fin de la session (vérification de compatibilité)
	 * @param discipline discipline de la session
	 * @param site site physique sur lequel se déroule la session
	 * @param sessionDescription description de la session
	 * @param sessionType type de la session
	 * @param sessionCategory catégorie de la session
	 * @return Response HTTP : erreur 400 si le format des données est incorrect (heures), erreur 409 si incompatible avec l'emploi du temps et erreur 500 si erreur serveur sinon retourne 200 (OK)
	 */
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
	        	//erreur 500 (serveur)
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	        } else if (ret == 2) {
	        	//compatibilité a echoué : une session existe déjà pour cette discipline sur cette période
	            return Response.status(Response.Status.CONFLICT).build();
	        } else {
	        	//OK succès de l'ajout
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
	
	/**
	 * Méthode PUT permettant de modifier une session
	 * @param sessionCode code de la session (code inchangé == CLEF PRIMAIRE)
	 * @param sessionDate date de la session
	 * @param sessionFromHour heure de début de la session (revérification de compatibilité)
	 * @param sessionToHour heure de fin de la session (revérification de compatibilité)
	 * @param discipline discipline de la session
	 * @param site site physique sur lequel se déroule la session
	 * @param sessionDescription description de la session
	 * @param sessionType type de la session
	 * @param sessionCategory catégorie de la session
	 * @return Response HTTP : erreur 400 si le format des données est incorrect (heures), erreur 409 si incompatible avec l'emploi du temps et erreur 500 si erreur serveur sinon retourne 200 (OK)
	 */
	@PUT
	@Consumes("application/x-www-form-urlencoded")
	@Path("/session-edit")
	public Response editSite(
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
	        
	        ret = sessionDAO.editSession(session);

	        if (ret == 0) {
	        	//erreur 500
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	        } else if (ret == 2) {
	        	//compatibilité a echoué : une session existe déjà pour cette discipline sur cette période
	            return Response.status(Response.Status.CONFLICT).build();
	        } else {
	        	//succès de la modification
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
	
	/**
	 * Méthode DELETE permettant de supprimer une session grâce à son code
	 * @param code code de la session à supprimer
	 * @return vrai si la session a été supprimée (retour de removeSession) false sinon
	 */
	@DELETE
	@Path("/delete-session/{code}")
	public boolean deleteSession(@PathParam("code") String code) {
	    try {
	    	return sessionDAO.removeSession(code);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}
