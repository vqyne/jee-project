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

/**
 * Contrôleur de la classe Discipline
 * Permet de CRUD sur les disciplines
 */
@Path("/discipline-controller")
public class DisciplineController {
	
	//initialisation du DAO nécessaire
	private DisciplineDAO disciplineDAO = new DisciplineDAO();

	/**
	 * Méthode permettant de retourner toutes les disciplines présentes en base de données
	 * @return la liste de toutes les disciplines en base de données
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-disciplines")
	public String getDisciplines() {
		List<Discipline> disciplines = null;
		disciplines = disciplineDAO.findAll();
		if(disciplines.isEmpty()) {
			disciplines = new ArrayList<Discipline>();
		}
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String json = gson.toJson(disciplines);
		return json;
	}
	
	/**
	 * Méthode utile à la partie statistiques de l'application web
	 * Cette méthode va permettre de retourner le TOP 5 des disciplines ayant les sessions les plus longues
	 * Pour cela, elle va seulement appeler la fonction associée dans le DAO de Discipline
	 * @return la liste des 5 disciplines ayant les plus gros totaux en durée de leurs sessions
	 */
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
	
	/**
	 * Méthode POST permettant l'ajout d'une discipline
	 * @param name nom de la nouvelle discipline
	 * @param accessible disponible aux paralympiques ou non, argument passé sous format String d'un bit (0 ou 1)
	 * @return true si l'ajout a été effectué false si erreur : retour non optimal, devrait être remplacé par une Response
	 */
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
	
	/**
	 * Méthode POST permettant de modifier une discipline
	 * L'accès aux jeux paralympiques n'est pas modifiable
	 * @param name nom de la discipline à modifier
	 * @param newName nouveau nom
	 * @return true si la modification a été effectué false si erreur : retour non optimal, devrait être remplacé par une Response
	 */
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
	
	/**
	 * Méthode DELETE permettant de supprimer une discipline
	 * @param name nom de la discipline à supprimer
	 * @return true si la modification a été effectué false si erreur : retour non optimal, devrait être remplacé par une Response
	 */
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
