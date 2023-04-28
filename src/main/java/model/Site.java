package model;

public class Site {
	private int id;
	private String name;
	private String city;
	private CategorieSite category;
	
	public Site(String n,String v,CategorieSite c) {
		this.name = n;
		this.city = v;
		this.category = c;
	}
	
	public Site(int id,String n,String v,CategorieSite c) {
		this.id = id;
		this.name = n;
		this.city = v;
		this.category = c;
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String n) {
		this.name = n;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String v) {
		this.city = v;
	}

	public CategorieSite getCategory() {
		return this.category;
	}

	public void setCategory(CategorieSite categorie) {
		this.category = categorie;
	}
	
	
}
