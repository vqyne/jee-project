package model;

import java.util.Date;

public class Athlete {
	private int id;
	private String lastname;
	private String firstname;
	private String country;
	private Date birthdate;
	private Genre genre;
	private Discipline discipline;
	
	public Athlete(String f, String l, String c, Discipline d) {
		this.firstname = f;
		this.lastname = l;
		this.country = c;
		this.discipline = d;
	}
	
	/**
	 * Constructor for find and findAll of AthleteDAO
	 * @param id id of the athlete in the database
	 * @param l lastname of the athlete
	 * @param f firstname of the athlete
	 * @param c country of the athlete
	 * @param birthd birthdate of the athlete
	 * @param genre genre of the athlete
	 * @param dis discipline of the athlete
	 */
	public Athlete(int id, String l,String f, String c, Date birthd, Genre genre, Discipline dis) {
		this.id = id;
		this.lastname = l;
		this.firstname = f;
		this.country = c;
		this.birthdate = birthd;
		this.genre = genre;
		this.discipline = dis;
	}
	
	public int getId() {
		return id;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Discipline getDiscipline() {
		return discipline;
	}

	public void setDiscipline(Discipline discipline) {
		this.discipline = discipline;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public Genre getGenre() {
		return genre;
	}
	
	public String getGenreString() {
		return genre.name();
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}
	
	public String toString() {
		return "L'athlète " + this.firstname + " " + this.lastname +  " (" + this.genre + ") " + " est né(e) le " + this.birthdate + ", son pays est " + this.country + " et pratique la discipline " + this.discipline.getName();  
	}
	
	
}
