package my.manager.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import my.manager.domain.Server;
import my.manager.domain.ServerState;
import my.manager.domain.Settings;

/**
 * Implementation of server DAO layer interface
 * <p>
 * All functions use JdbcTemplate
 */
@Repository
public class ManagerServerDaoImpl extends JdbcDaoSupport implements ManagerServerDao {
	public static Logger log = Logger.getLogger(ManagerServerDaoImpl.class);
	private static ServerMapper serverMapper = null;
	private static SettingsMapper settingsMapper = null;
	
	@Autowired
	private DataSource dataSource;
	
	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		serverMapper = new ServerMapper();
		settingsMapper = new SettingsMapper();
	}
	
	@Override
	public Server getServerById(int id) {
		Server serv;
		String SQL = "SELECT * FROM SERVERS WHERE SERVER_ID = ?";
		try{
			serv = (Server)getJdbcTemplate().queryForObject( SQL, new Object[]{id},serverMapper);
		} catch (IncorrectResultSizeDataAccessException e) {
			serv = null;
		}
		return serv;
	}
	
	@Override
	public List<Server> listServers() {
		String SQL = "SELECT * FROM SERVERS ORDER BY STATE DESC;";
		
		return getJdbcTemplate().query(SQL, serverMapper);
	}	
	
	@Override
	public List<Server> getServersByState(ServerState state) {
		String SQL = "SELECT * FROM SERVERS WHERE STATE = ? ORDER BY NAME";
		
		return getJdbcTemplate().query(SQL, new Object[]{new Integer(state.ordinal()+1)},serverMapper);
	}

	@Override
	public void createServer(Server s) {
		try{
			String SQL = "INSERT INTO SERVERS(NAME, ADRESS, PORT, URL_PATH, ACTIVE)"
					 + " VALUES(?, ?, ?, ?, ?)";	
			getJdbcTemplate().update(SQL, s.getName(), s.getAdress() , s.getPort(), s.getUrlPath(), s.isActive());
		} catch (Exception e){
			log.error(String.format("Could not create server NAME = %s, ADRESS = %s, PORT = %s, URL_PATH = %s, ACTIVE = %s",
					               s.getName(), s.getAdress() , s.getPort(), s.getUrlPath(), s.isActive() ) );
		}
	}
	
	@Override
	public void deleteServer(int id) {
		try{
			String SQL = "DELETE FROM SERVERS WHERE SERVER_ID = ?";
		    getJdbcTemplate().update(SQL, new Object[]{new Integer(id)});
		    
		    SQL = "DELETE FROM USER_SERVER WHERE SERVER_ID = ?";
		    getJdbcTemplate().update(SQL, new Object[]{new Integer(id)});
		    
		} catch(DataAccessException e){
			log.error("error trying to delete server with id = " + id + ", message: " + e);
		}
	}

	@Override
	public void updateServer(Server s){
		
		String lastCheck;
		if(s.getLastCheckTime() != null){
			lastCheck = s.getLastCheckTime().toString().substring(0, 19);
		}else{
			lastCheck = null;
		}
		
		try{
			String SQL = "UPDATE SERVERS SET NAME = ?, ADRESS = ?, PORT = ?, URL_PATH = ?, STATE = ?,"
					   + " RESPONSE = ?, LAST_CHECK = ?, ACTIVE = ? WHERE SERVER_ID = ?";
				
			getJdbcTemplate().update(SQL, s.getName(), s.getAdress() , s.getPort(), s.getUrlPath(), 
						            s.getState().ordinal() + 1, s.getResponse(),
						            lastCheck, s.isActive(), s.getId());
		} catch (Exception e){
			log.error(String.format("Could not update server NAME = %s, ADRESS = %s, PORT = %s,"
                     + "URL_PATH = %s, STATE = %s, RESPONSE = %s, stack trace:\n" + e.getMessage(),
                     s.getName(), s.getAdress() , s.getPort(), s.getUrlPath(), s.getState(), s. getResponse() ) );
		}
	}

	@Override
	public boolean isActiveServer(int servId) {
		String SQL = "SELECT COUNT(*) FROM SERVERS WHERE SERVER_ID = ? AND ACTIVE = TRUE";
		
		return ( getJdbcTemplate().queryForInt(SQL, new Object[]{new Integer(servId)}) == 1 ) ? true : false;
	}
	

	@Override
	public int getServerId(String name) {
		try{
			String SQL = "SELECT SERVER_ID FROM SERVERS WHERE NAME = ?";
			
			return  getJdbcTemplate().queryForInt(SQL, name) ;
		} 
		catch (DataAccessException e)
		{
			return 0;
		}
	}

	@Override
	public Settings getSettings() {
		Settings settings;
		String SQL = "SELECT * FROM SETTINGS";
		
		try{
			settings = (Settings)getJdbcTemplate().queryForObject(SQL, 
					settingsMapper);
			
		} catch (IncorrectResultSizeDataAccessException e) {
			settings = null;
		}
	
		return settings;
	}

	@Override
	public void setSettings(Settings s) {
		try{
			String SQL = "UPDATE SETTINGS SET SERVER_CHECK_INTERVAL = ?,  CLIENT_REFRESH_INTERVAL = ?, "
					   + "SERVER_TIMEOUT = ?, SMTP_SERVER_ADRESS = ?, SMTP_PORT = ?";
				
			getJdbcTemplate().update(SQL, s.getCheckInterval(), s.getRefreshInterval(), s.getServerTimeout(),
									s.getSmtpAdress(), s.getSmtpPort());
		} catch (Exception e){
			log.error( "Could not update settings" );
		}
	}
	
	@Override
	public List<Server> listServersForUser(int userId) {
		String SQL = "SELECT * FROM SERVERS WHERE SERVER_ID IN " 
	               + "(SELECT SERVER_ID FROM USER_SERVER WHERE USER_ID = ?)";
		return getJdbcTemplate().query(SQL, new Object[]{new Integer(userId)},serverMapper);
	}

	@Override
	public List<Server> listServersNotForUser(int userId) {
		String SQL = "SELECT * FROM SERVERS WHERE SERVER_ID NOT IN " 
	               + "(SELECT SERVER_ID FROM USER_SERVER WHERE USER_ID = ?)";
		return getJdbcTemplate().query(SQL, new Object[]{new Integer(userId)},serverMapper);
	}
	
	/**
	 * Implementation of RowMapper for Server
	 */
	private static class ServerMapper implements RowMapper<Server>{
		public Server mapRow(ResultSet rs, int rowNum) 
				throws SQLException {
			Server s = new Server();
			
			s.setName(rs.getString("NAME"));
			s.setAdress(rs.getString("ADRESS"));
			s.setPort(rs.getInt("PORT"));
			s.setUrlPath(rs.getString("URL_PATH"));
			s.setState(ServerState.valueOf(rs.getString("STATE")));
			s.setCreationTime(rs.getTimestamp("CREATED"));
			s.setResponse(rs.getString("RESPONSE"));
			s.setActive(rs.getBoolean("ACTIVE"));
			s.setId(rs.getInt("SERVER_ID"));
			s.setLastCheckTime(rs.getTimestamp("LAST_CHECK"));
			return s;
		}
	}
	/**
	 * Implementation of RowMapper for Settings
	 */
	private static class SettingsMapper implements RowMapper<Settings>{
		
		public Settings mapRow(ResultSet rs, int rowNum) 
				throws SQLException {
			Settings s = new Settings();
			
			s.setCheckInterval(rs.getInt("server_check_interval"));
			s.setRefreshInterval(rs.getInt("client_refresh_interval"));
			s.setServerTimeout(rs.getInt("server_timeout"));
			s.setSmtpPort(rs.getInt("smtp_port"));
			s.setSmtpAdress(rs.getString("smtp_server_adress"));
			
			return s;
		}
	}
}

