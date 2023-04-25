package database;

import model.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DisciplineDAO {
	

	public List<Discipline> findAll(){
		List<Discipline> ret = new ArrayList<Discipline>();
		Connection connexion = DBManager.getInstance().getConnection();
		try {
			Statement statement = connexion.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM Discipline");
			while(rs.next()) {
				String name = rs.getString("name");
				//Boolean flag = rs.getByte("flag");
				ret.add(new Discipline(name));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	public Discipline findByString(String searchText) {
		Discipline ret = null;
		Connection connexion = DBManager.getInstance().getConnection();
		try {
			PreparedStatement ps = connexion.prepareStatement("SELECT * FROM Discipline WHERE upper(title) = ?");
			ps.setString(1, "%" + searchText.toUpperCase() + "%");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String name = rs.getString("name");
				ret = new Discipline(name);
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	
}
