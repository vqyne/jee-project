package model;

import java.time.LocalTime;
import java.util.Date;

public class Session {
	private String code;
	private Date date;
	private LocalTime fromHour;
	private LocalTime toHour;
	private Discipline discipline;
	private Site site;
	private String description;
	private TypeSession type;
	private CategorieSession category;
	
	public Session(Date date, LocalTime from, LocalTime to, Discipline d, Site s, String des, TypeSession t, CategorieSession c) {
		this.date = date;
		this.fromHour = from;
		this.toHour = to;
		this.discipline = d;
		this.site = s;
		this.description = des;
		this.type = t;
		this.category = c;
		
		this.code = d.getName().substring(0, 3) + "00";
	}
	
	public Session(String code, Date date, LocalTime from, LocalTime to, Discipline d, Site s, String des, TypeSession t, CategorieSession c) {
		this.code = code;
		this.date = date;
		this.fromHour = from;
		this.toHour = to;
		this.discipline = d;
		this.site = s;
		this.description = des;
		this.type = t;
		this.category = c;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = this.discipline.getName().substring(0, 3) + code;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public LocalTime getFromHour() {
		return fromHour;
	}
	public void setFromHour(LocalTime fromHour) {
		this.fromHour = fromHour;
	}
	public LocalTime getToHour() {
		return toHour;
	}
	public void setToHour(LocalTime toHour) {
		this.toHour = toHour;
	}
	public Discipline getDiscipline() {
		return discipline;
	}
	public void setDiscipline(Discipline discipline) {
		this.discipline = discipline;
	}
	public Site getSite() {
		return site;
	}
	public void setSite(Site site) {
		this.site = site;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public TypeSession getType() {
		return type;
	}
	public void setType(TypeSession type) {
		this.type = type;
	}
	public CategorieSession getCategory() {
		return category;
	}
	public void setCategory(CategorieSession c) {
		this.category = c;
	}
	
	
}
