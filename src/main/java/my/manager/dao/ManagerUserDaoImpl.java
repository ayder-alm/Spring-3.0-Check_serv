package my.manager.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import my.manager.domain.User;

/**
 * Implementation of user DAO layer interface
 * <p>
 * All functions use JdbcTemplate
 */
@Repository
public class ManagerUserDaoImpl extends JdbcDaoSupport implements ManagerUserDao {
	public static Logger log = Logger.getLogger(ManagerUserDaoImpl.class);
	private static UserMapper userMapper = null;
	
	@Autowired
	private DataSource dataSource;
	
	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		userMapper = new UserMapper();
	}
	
	@Override
	public List<User> listUsers() {
		String SQL = "SELECT * FROM USERS ORDER BY NAME";
			
		return getJdbcTemplate().query(SQL, userMapper);
	}

	@Override
	public User getUser(int id) {
		User usr;
		String SQL = "SELECT * FROM USERS WHERE USER_ID = ?";
		try{
			usr = (User)getJdbcTemplate().queryForObject(SQL, 
				new Object[]{new Integer(id)},
				userMapper);
			
		} catch (IncorrectResultSizeDataAccessException e) {
			usr = null;
		}
		return usr;
	}
	
	@Override
	public void createUser(User u) {
		try{
			String SQL = "INSERT INTO USERS(NAME, LOGIN, PASSWORD, EMAIL, ACTIVE, ADMIN) VALUES(?, ?, ?, ?, ?, ?)";	
			
			getJdbcTemplate().update(SQL, u.getName(), u.getLogin(), u.getPassword(), u.getEmail(), u.isActive(), u.isAdmin());
		} catch (Exception e){
			log.error(String.format("Could not create user NAME = %s, LOGIN = %s, EMAIL = %s, ACTIVE = %s, ADMIN = %s",
                     u.getName(), u.getLogin(), u.getEmail(), u.isActive(), u.isAdmin() ) );
		}
	}
	
	@Override
	public void deleteUser(int id) {
		try{
			String SQL = "DELETE FROM USERS WHERE USER_ID = ?";
		    getJdbcTemplate().update(SQL, id);
		    
		    SQL = "DELETE FROM USER_SERVER WHERE USER_ID = ?";	//if user assigned servers exist delete assignments
		    getJdbcTemplate().update(SQL, id);
		    
		    SQL = "DELETE FROM USER_ROLES WHERE USER_ID = ?";	//if user assigned roles exist delete roles
		    getJdbcTemplate().update(SQL, id);
		    
		} catch(Exception e){
			log.error("error deleting user with id = " + id + " error:" + e.getMessage());
		}
	}

	@Override
	public void updateUser(User u) {
		String lastLogin;
		if(u.getLastLoginTime() != null){
			lastLogin = u.getLastLoginTime().toString().substring(0, 19);
		}else{
			lastLogin = null;
		}
		
		try{
			String SQL = "UPDATE USERS SET NAME = ?,  LOGIN = ?, PASSWORD = ?, "
					   + "EMAIL = ?, ACTIVE = ?, ADMIN = ?, LAST_LOGIN = ? WHERE USER_ID = ?";
				
			getJdbcTemplate().update(SQL, u.getName(), u.getLogin(), u.getPassword(), u.getEmail(),
				                 u.isActive(), u.isAdmin(), lastLogin, u.getId());
		} catch (Exception e){
			log.error(String.format("Could not update user NAME = %s, LOGIN = %s, EMAIL = %s," + 
					" ACTIVE = %s LAST_LOGIN = %s!!!!!!!!!! " + e, u.getName(), u.getLogin(), u.getEmail(), 
					u.isActive(), u.isAdmin(), u.getLastLoginTime() ) );
		}
	}
	
	@Override
	public void setUserServerPair(int userId, int serverId) {
		try{
			String SQL = "INSERT INTO USER_SERVER ( USER_ID, SERVER_ID ) VALUES ( ?, ? )";
			getJdbcTemplate().update(SQL, userId, serverId);
		}
		catch(DuplicateKeyException e){
			//do nothing - server pair is already set (no problem)
		}
		catch (Exception e){
			log.error(String.format("Could not set server id = %d for user NAME = %s" + e,
					                serverId, getUser(userId) ) );
		}
	}
	
	@Override
	public void deleteUserServerPair(int userId, int serverId) {
		try{
			String SQL = "DELETE FROM USER_SERVER WHERE USER_ID = ? AND SERVER_ID = ?";
			getJdbcTemplate().update(SQL, userId, serverId);
		} catch(Exception e){
			log.error(e.getMessage());
		}
	}

	@Override
	public int getUserId(String login) {
		try{	
			String SQL = "SELECT USER_ID FROM USERS WHERE LOGIN = ?";
			
			return  getJdbcTemplate().queryForInt(SQL, login) ;
		} 
		catch (DataAccessException e)
		{
			return 0;
		}
	}

	@Override
	public void setUserRole(int userId, String role) {
		try{
			String SQL = "DELETE FROM USER_ROLES WHERE USER_ID = ?";	//if user assigned roles exist delete roles
		    getJdbcTemplate().update(SQL, userId);
			
			SQL = "INSERT INTO USER_ROLES ( USER_ID, AUTHORITY) VALUES(?, ?)";
			getJdbcTemplate().update(SQL, userId, role);
		} catch (Exception e){
			log.error(String.format("Could not set role = %s for userid = %s", role, userId));
		}
		
	}

	/**
	 * Implementation of RowMapper for User
	 */
	private static class UserMapper implements RowMapper<User>{
		public User mapRow(ResultSet rs, int rowNum) 
				throws SQLException {
			User u = new User();
			
			u.setId(rs.getInt("USER_ID"));
			u.setName(rs.getString("NAME"));
			u.setLogin(rs.getString("LOGIN"));
			u.setEmail(rs.getString("EMAIL"));
			u.setPassword(rs.getString("PASSWORD"));
			u.setActive(rs.getBoolean("ACTIVE"));
			u.setAdmin(rs.getBoolean("ADMIN"));
			u.setCreationTime(rs.getTimestamp("CREATED"));
			u.setLastLoginTime(rs.getTimestamp("LAST_LOGIN"));
			
			return u;
		}
	}
}

