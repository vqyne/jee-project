package controller;

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
import model.CategorieSite;
import model.Discipline;
import model.Site;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import database.SiteDAO;

/**
 * Contrôleur de la classe Site
 * Permet de CRUD sur les sessions
 */
@Path("/site-controller")
public class SiteController {
	
	//initialisation du DAO nécessaire
	private SiteDAO siteDAO = new SiteDAO();

	/**
	 * Méthode utile pour notre fonctionnalité statistiques et permettant de retourner les 5 sites les plus utilisés lors des Jeux
	 * @return la liste des 5 sites les plus utilisés sous format JSON
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-top-five-sites")
	public String getTopFive() {
		List<Site> sites = null;
	
		sites = siteDAO.findTopFiveSitesBySessions();

		if(sites.isEmpty()) {
			System.out.println("Base vide : le top 5 des sites ne peut pas être affiché");
			sites = new ArrayList<Site>();
		}
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String json = gson.toJson(sites);
		return json;
	}
	
	/**
	 * Méthode GET permettant de retourner les catégories de site
	 * @return les catégories de site sous format JSON
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-categories")
	public String getCategories() {
	    List<CategorieSite> categories = Arrays.asList(CategorieSite.values());
	    
	    Gson gson = new Gson();
	    String json = gson.toJson(categories);
	    
	    return json;
	}
	
	/**
	 * Méthode GET permettant de retourner tous les sites présents en base de données
	 * @return liste des sites sous format JSON
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-sites")
	public String findAll() {
		List<Site> sites = siteDAO.findAll();

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String json = gson.toJson(sites);
		return json;
	}
	
	/**
	 * Méthode GET permettant de retourner un site en particulier
	 * @param id identifiant du site à retourner
	 * @return site ayant pour id id sous format JSON
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-site/{id}")
	public String findAll(@PathParam("id") int id) {
		Site site = siteDAO.findById(id);

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String json = gson.toJson(site);
		return json;
	}
	
	/**
	 * Méthode POST permettant d'ajouter un nouveau site
	 * @param name nom du site à ajouter
	 * @param city ville du site
	 * @param category catégorie du nouveau site
	 * @return true si le site à été ajouté false sinon : retour non optimal (Response à la place d'un boolean) 
	 */
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("/site-add")
	public boolean addSite(@FormParam("name") String name, @FormParam("city") String city, @FormParam("category") String category) {
		
		try {
			Site site = new Site(name, city, CategorieSite.valueOf(category.toLowerCase()));
			siteDAO.addSite(site);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Méthode PUT permettant de modifier un site
	 * @param name nom du site à modifier
	 * @param city ville du site
	 * @param category catégorie du nouveau site
	 * @return true si le site à été modifié false sinon : retour non optimal (Response à la place d'un boolean) 
	 */
	@PUT
	@Consumes("application/x-www-form-urlencoded")
	@Path("/site-edit")
	public boolean editSite(@FormParam("id") int id,@FormParam("name") String name, @FormParam("city") String city, @FormParam("category") String category) {
		
		try {
			Site site = new Site(id, name, city, CategorieSite.valueOf(category.toLowerCase()));
			siteDAO.editSite(site);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Méthode DELETE permettant de supprimer un site
	 * @param id id du site à supprimer
	 * @return true si le site à été supprimé false sinon : retour non optimal (Response à la place d'un boolean) 
	 */
	@DELETE
	@Path("/delete-site/{id}")
	public boolean deleteSite(@PathParam("id") int id) {
	    try {
	        siteDAO.removeSite(id);
	        return true;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

}
