package model;

public class Discipline {
	private String name;
	private boolean flag;
	
	public Discipline(String n, boolean f) {
		this.name = n;
		this.flag = f;
	}
	
	public Discipline(String n) {
		this.name = n;
		this.flag = false;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	
}
