package controller;

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
import model.CategorieSite;
import model.Discipline;
import model.Site;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import database.SiteDAO;

@Path("/site-controller")

public class SiteController {
	
	private SiteDAO siteDAO = new SiteDAO();

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
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-categories")
	public String getCategories() {
	    List<CategorieSite> categories = Arrays.asList(CategorieSite.values());
	    
	    Gson gson = new Gson();
	    String json = gson.toJson(categories);
	    
	    return json;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get-sites")
	public String findAll(@QueryParam("site") String siteName) {
		List<Site> sites = siteDAO.findAll();

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String json = gson.toJson(sites);
		return json;
	}
	
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
	
	/*
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{name}")
	public String get(@PathParam("id") Integer id) {
		Site site = siteDAO.findById(id);

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String json = gson.toJson(site);
		return json;
	}
	

	
	@DELETE
	@Path("/{id}")
	public boolean delete(@PathParam("id") Integer id) {
		return siteDAO.removeSite(id);
	}
	*/
}
