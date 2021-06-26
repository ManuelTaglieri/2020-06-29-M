package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void getRegistiPerAnno(int anno, Map<Integer, Director> idMap) {
		String sql = "SELECT d.id, d.first_name, d.last_name "
				+ "FROM directors d, movies_directors md, movies m "
				+ "WHERE d.id = md.director_id AND m.id = md.movie_id AND m.year = ?";

		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				if (idMap.get(res.getInt("id"))==null) {
					Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
					idMap.put(res.getInt("id"), director);
				}
				
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public List<Adiacenza> getArchi(int anno, Map<Integer, Director> idMap) {
		String sql = "SELECT d1.id AS d1, d2.id AS d2, COUNT(DISTINCT r1.actor_id) AS peso "
				+ "FROM directors d1, directors d2, movies_directors md, movies_directors md2, movies m, movies m2, "
				+ "roles r1, roles r2 "
				+ "WHERE d1.id = md.director_id AND m.id = md.movie_id AND r1.movie_id = m.id "
				+ "AND d2.id = md2.director_id AND m2.id = md2.movie_id AND r2.movie_id = m2.id "
				+ "AND d1.id > d2.id AND r1.actor_id = r2.actor_id AND m.year = ? AND m2.year = m.year "
				+ "GROUP BY d1.id, d2.id";
		Connection conn = DBConnect.getConnection();
		List<Adiacenza> risultato = new ArrayList<>();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				if (idMap.get(res.getInt("d1"))!=null && idMap.get(res.getInt("d2"))!=null) {
				
				risultato.add(new Adiacenza(idMap.get(res.getInt("d1")), idMap.get(res.getInt("d2")), res.getInt("peso")));
				
				}
				
			}
			conn.close();
			return risultato;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
