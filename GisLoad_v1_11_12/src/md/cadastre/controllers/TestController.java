package md.cadastre.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import md.cadastre.dbconnection.Database;
import md.cadastre.objects.Copill;

@ManagedBean(eager = true)
@ApplicationScoped
public class TestController {
	
	private List<Copill> allCopilList;
	
	
	
	public TestController() {
		getAllCopii();
	}

	public void getAllCopii() {
		Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        
        allCopilList = new ArrayList<>();
        
        try {
        	conn = Database.getConnection();
        	stmt = conn.createStatement();
        	rs = stmt.executeQuery("SELECT nume, prenume FROM public.copiii_sapp");
        	
        	while(rs.next()) {
        		Copill copil = new Copill(rs.getString("nume"), rs.getString("prenume"));
        		allCopilList.add(copil);
        	}
        } catch (SQLException ex) {
        	System.out.println("SQLException " + ex);
        } finally {
        	try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
               System.out.println(ex.getMessage());
            }
		}
	}

	public List<Copill> getAllCopilList() {
		return allCopilList;
	}
	
	
}
