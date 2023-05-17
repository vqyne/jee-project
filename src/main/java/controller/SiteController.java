package controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import model.Site;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.ArrayList;

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

	
}
