package my.manager.daotest;

import static org.junit.Assert.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import my.manager.dao.ManagerServerDao;
import my.manager.dao.ManagerUserDao;
import my.manager.domain.Server;
import my.manager.domain.ServerState;
import my.manager.domain.Settings;
import my.manager.domain.User;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations ={"classpath:test-context.xml"})
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class ManagerDaoImplTest {

	public static Logger log = Logger.getLogger(ManagerDaoImplTest.class);
    private  ManagerServerDao serverDao;
    private  ManagerUserDao userDao;
	private static JdbcTemplate jdbc;
	public static final int N = 1000;
	
	@SuppressWarnings("static-access")
	@Autowired
	void init(ManagerServerDao serverDao, ManagerUserDao userDao, JdbcTemplate jdbc){
		this.serverDao = serverDao;
		this.userDao = userDao;
		this.jdbc = jdbc;
	}
	
	private static void newServer(int id, String name, String adress, int port, String url,
								  ServerState state, String response, boolean active)
	{
		String SQL = String.format("INSERT INTO SERVERS(SERVER_ID, NAME, ADRESS, PORT, URL_PATH, STATE, RESPONSE, ACTIVE)"
				   + " VALUES(%d, '%s', '%s', %d, '%s', '%s', '%s', %s)", id, name, adress , port, url, state, response, active );
		jdbc.update(SQL);
	}
	
	private static Server getServer(String name)
	{
		Server serv;
		try{
			String SQL = "SELECT * FROM SERVERS WHERE NAME = '" + name + "'";
			serv = (Server)jdbc.queryForObject( SQL,
					new RowMapper<Server>()
					{
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
					} );
		}
		catch( DataAccessException e)
		{
			serv = null;
		}
		return serv;
	}

	private static void newUser(int id,String name, String login, String password, String email, 
			Timestamp lastLoginTime, boolean active, boolean admin)
	{
		String SQL = String.format("INSERT INTO USERS(USER_ID, NAME, LOGIN, PASSWORD, EMAIL, LAST_LOGIN, ACTIVE, ADMIN) "
				   + " VALUES(%d, '%s', '%s', '%s', '%s', '%s', %s, %s)", id, name, login , password, email,
				   											lastLoginTime.toString().substring(0, 19), active, admin );
		jdbc.update(SQL);
	}

	private static User getUser(String name)
	{
		User usr;
		try{
			String SQL = "SELECT * FROM USERS WHERE NAME = '" + name + "'";
			usr = (User)jdbc.queryForObject( SQL,
					new RowMapper<User>(){
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
					} );
		}
		catch( DataAccessException e)
		{
			usr = null;
		}
		return usr;
	}
	
	@Test
    public void testAutowired() throws Exception {
		assertNotNull(jdbc);
        assertNotNull(userDao);
        assertNotNull(serverDao);
    }
	
	@Before
	public void setUp() throws Exception {
		
		String SQL = "DELETE FROM SERVERS";
		jdbc.execute(SQL);
		SQL = "DELETE FROM USERS";
		jdbc.execute(SQL);
		SQL = "DELETE FROM USER_SERVER";
		jdbc.execute(SQL);
		SQL = "DELETE FROM USER_ROLES";
		jdbc.execute(SQL);
		SQL = "DELETE FROM SETTINGS";
		jdbc.execute(SQL);
		
		newServer(N + 1, "name1", "adress1", 81, "url1", ServerState.FAIL, "resp1", true);
		newServer(N + 2, "name2", "adress0", 82, "url2", ServerState.FAIL, "resp2", true);
		newServer(N + 3, "name3", "adress3", 83, "url3", ServerState.WARN, "resp3", true);
		newServer(N + 4, "name4", "adress4", 84, "url4", ServerState.WARN, "resp4", false);
		newServer(N + 5, "name5", "adress5", 85, "url5", ServerState.OK, "resp5", false);
		newServer(N + 6, "name6", "adress6", 86, "url6", ServerState.OK, "resp6", false);
		
		newUser(N + 1, "name1", "login1", "password1", "email1", new Timestamp(1000), true, true);
		newUser(N + 2, "name2", "login2", "password2", "email2", new Timestamp(2000), true, false);
		newUser(N + 3, "name3", "login3", "password3", "email3", new Timestamp(3000), false, true);
		newUser(N + 4, "name4", "login4", "password4", "email4", new Timestamp(4000), false, false);
	}
	
	@Test
	public void testGetServerById() {
		assertNull(serverDao.getServerById(-1));
		assertNull(serverDao.getServerById(0));
		assertNull(serverDao.getServerById(N + 200));
		
		newServer(N + 100, "name100", "adress100", 810, "url100", ServerState.FAIL, "resp100", true);
		Server serv = serverDao.getServerById(N + 100);
		assertEquals(serv.getName(), "name100");
		assertEquals(serv.getAdress(), "adress100");
		assertEquals(serv.getPort(), new Integer(810));
		assertEquals(serv.getUrlPath(), "url100");
		assertEquals(serv.getState(), ServerState.FAIL);
		assertEquals(serv.getResponse(), "resp100");
	}
	
	@Test
	public void testListServers() {
		String SQL = "DELETE FROM SERVERS";
		jdbc.execute(SQL);
		assertTrue(serverDao.listServers().size()==0);
		newServer(N + 1, "name1", "adress1", 801, "url1", ServerState.FAIL, "1", true);
		newServer(N +2, "name2", "adress0", 802, "url2", ServerState.WARN, "2", true);
		assertTrue(serverDao.listServers().size()==2);
	}
	
	@Test
	public void testGetServersByState() {
		
		assertTrue(serverDao.getServersByState(ServerState.FAIL).size()==2);
		assertTrue(serverDao.getServersByState(ServerState.OK).size()==2);
		assertTrue(serverDao.getServersByState(ServerState.WARN).size()==2);
	}
	
	@Test
	public void testCreateServer() {
		Server serv = new Server(N + 7, "name7", "adress1", 81, "url1", ServerState.FAIL, "resp1", true);
		serverDao.createServer(serv);
		serverDao.createServer(serv);
		Server servExp = getServer("name7");
		
		assertEquals(servExp.getPort(), serv.getPort());
		assertTrue(servExp.isActive()==serv.isActive());
		assertEquals(serv.getName(), servExp.getName());
		assertEquals(serv.getAdress(), servExp.getAdress());
		assertEquals(serv.getUrlPath(), servExp.getUrlPath());
	}

	@Test
	public void testDeleteServer() {
		Server serv = getServer("name1");
		assertNotNull(serv);

		serverDao.deleteServer(N + 1);

		serv = getServer("name1");
		assertNull(serv);
	}
	
	@Test
	public void testUpdateServer() {
		
		Server servExp = getServer("name1");
		assertNotNull(servExp);
		
		Server serv = new Server(N + 1, "nameX", "adressX", 80, "urlX", ServerState.OK, "respX", false);
		serverDao.updateServer(serv);
		servExp = getServer("nameX");
		
		assertTrue(servExp.getName().equals("nameX"));
		assertTrue(servExp.getAdress().equals("adressX"));
		assertTrue(servExp.getUrlPath().equals("urlX"));
		assertTrue(servExp.getResponse().equals("respX"));
		assertTrue(servExp.getId() == N + 1);
		assertFalse(servExp.isActive());
		
		serv.setId(0);
		serverDao.updateServer(serv);
	}
	
	@Test(expected = java.lang.NullPointerException.class)
	public void testUpdateServerNull() 
	{
		serverDao.updateServer(null);
	}
	
	@Test
	public void testIsActivetServer() {
		assertTrue(serverDao.isActiveServer(N + 1));
		assertFalse(serverDao.isActiveServer(N + 6));
	}

	@Test
	public void testListUsers() {
		assertFalse(userDao.listUsers().isEmpty());
		String SQL = "DELETE FROM USERS";
		jdbc.execute(SQL);
		assertTrue(userDao.listUsers().isEmpty());
	}

	@Test
	public void testGetUser() {
		User usrExp = userDao.getUser(N + 1);
		User usr = getUser("name1");
		
		assertEquals(usr.getName(), usrExp.getName());
		assertEquals(usr.getEmail(), usrExp.getEmail());
		assertEquals(usrExp.getId(), N + 1);
		assertEquals(usr.getLastLoginTime(), usrExp.getLastLoginTime());
		assertEquals(usr.getLogin(), usrExp.getLogin());
		assertEquals(usr.getPassword(), usrExp.getPassword());
	}

	@Test
	public void testCreateUser() {
		User usr = new User(N + 7, "name7", "login7", "password7", "email7", new Timestamp(7), true, true);
		userDao.createUser(usr);
		User usrExp = getUser("name7");
		
		assertEquals(usr.getName(), usrExp.getName());
		assertEquals(usr.getEmail(), usrExp.getEmail());
		assertEquals(usr.getPassword(), usrExp.getPassword());
	}

	@Test
	public void testDeleteUser() {
		User usr= getUser("name1");
		assertNotNull(usr);
		userDao.deleteUser(N + 1);
		usr= getUser("name1");
		assertNull(usr);
	}

	@Test
	public void testUpdateUser() {
		User usrExp = getUser("name1");
		assertNotNull(usrExp);
		
		User usr = new User(N + 1, "name10", "login10", "password10", "email10", new Timestamp(10000), false, false);
		userDao.updateUser(usr);
		
		usrExp = getUser("name10");
		assertNotNull(usrExp);
		
		assertEquals(usr.getName(), usrExp.getName());
		assertEquals(usr.getEmail(), usrExp.getEmail());
		assertEquals(usrExp.getId(), N + 1);
		assertEquals(usr.getLastLoginTime(), usrExp.getLastLoginTime());
		assertEquals(usr.getLogin(), usrExp.getLogin());
		assertEquals(usr.getPassword(), usrExp.getPassword());
	}

	@Test
	public void testSetUserServerPair() {
		String SQL = "SELECT COUNT(*) FROM USER_SERVER";
		int count = jdbc.queryForInt(SQL);
		assertEquals(count, 0);
		
		userDao.setUserServerPair(N + 1, N + 2);
		count = jdbc.queryForInt(SQL);
		assertEquals(count, 1);
		
		SQL = "SELECT USER_ID FROM USER_SERVER WHERE SERVER_ID = " + (N + 2);
		int userId  = jdbc.queryForInt(SQL);
		SQL = "SELECT SERVER_ID FROM USER_SERVER WHERE USER_ID = " + (N + 1);
		int serverId  = jdbc.queryForInt(SQL);
		assertEquals(userId, N + 1);
		assertEquals(serverId, N + 2);
		
		SQL = "SELECT COUNT(*) FROM USER_SERVER";
		userDao.setUserServerPair(N + 1, N + 3);
		count = jdbc.queryForInt(SQL);
		assertEquals(count, 2);
	}
	
	@Test
	public void testDeleteUserServerPair() {
		String SQL = "SELECT COUNT(*) FROM USER_SERVER";
		int count = jdbc.queryForInt(SQL);
		assertEquals(count, 0);
		
		userDao.setUserServerPair(N + 1, N + 2);
		
		count = jdbc.queryForInt(SQL);
		assertEquals(count, 1);
		
		userDao.deleteUserServerPair(N + 1, N + 2);
		count = jdbc.queryForInt(SQL);
		assertEquals(count, 0);
	}
	
	@Test
	public void testListServersForUser() {
		int count = serverDao.listServersForUser(N + 1).size();
		assertEquals(count, 0);
		
		userDao.setUserServerPair(N + 1, N + 2);
		userDao.setUserServerPair(N + 1, N + 3);
		
		count = serverDao.listServersForUser(N + 1).size();
		assertEquals(count, 2);
		
		assertEquals(serverDao.listServersForUser(N + 1).get(0).getName(), "name2");
		assertEquals(serverDao.listServersForUser(N + 1).get(1).getName(), "name3");
	}
	
	@Test
	public void testListServersNotForUser() {
		int count = serverDao.listServersNotForUser(N + 1).size();
		assertEquals(count, 6);
		
		userDao.setUserServerPair(N + 1, N + 2);
		count = serverDao.listServersNotForUser(N + 1).size();
		assertEquals(count, 5);
		
		userDao.setUserServerPair(N + 1, N + 3);
		count = serverDao.listServersNotForUser(N + 1).size();
		assertEquals(count, 4);
	}
	
	@Test
	public void testGetUserId() {
		
		int idExp = userDao.getUserId("login1");
		assertEquals(idExp, N + 1);
		
		idExp = userDao.getUserId("fail");
		assertEquals(idExp, 0);
	}
	
	@Test
	public void testSetUserRole() {
		String SQL = "SELECT COUNT(*) FROM USER_ROLES";
		int count = jdbc.queryForInt(SQL);
		assertEquals(count, 0);
		
		userDao.setUserRole(N + 1, "role1");
		count = jdbc.queryForInt(SQL);
		assertEquals(count, 1);
		
		SQL = "SELECT COUNT(AUTHORITY) FROM USER_ROLES WHERE USER_ID = " + (N + 1);
		count = jdbc.queryForInt(SQL);
		assertEquals(count, 1);
		
		userDao.setUserRole(N + 1, "role2");
		count = jdbc.queryForInt(SQL);
		assertEquals(1, count);
	}
	
	@Test
	public void testGetServerId() {
		int idExp = serverDao.getServerId("name1");
		assertEquals(idExp, N + 1);
		
		idExp = serverDao.getServerId("fail");
		assertEquals(idExp, 0);
	}
	
	@Test
	public void testGetSettings() {
		String SQL = "INSERT INTO SETTINGS (SERVER_CHECK_INTERVAL, CLIENT_REFRESH_INTERVAL, "
				   + "SERVER_TIMEOUT, SMTP_SERVER_ADRESS, SMTP_PORT) VALUES(1,2,3,'addr',4)";
		jdbc.update(SQL);
		
		Settings stt = serverDao.getSettings();
		assertEquals(stt.getCheckInterval(), new Integer(1));
		assertEquals(stt.getRefreshInterval(), new Integer(2));
		assertEquals(stt.getServerTimeout(), new Integer(3));
		assertEquals(stt.getSmtpAdress(), "addr");
		assertEquals(stt.getSmtpPort(), new Integer(4));
	}
	
	@Test 
	public void testSetSettings() {
		String SQL = "INSERT INTO SETTINGS (SERVER_CHECK_INTERVAL, CLIENT_REFRESH_INTERVAL, "
				   + "SERVER_TIMEOUT, SMTP_SERVER_ADRESS, SMTP_PORT) VALUES(1,2,3,'addr',4)";
		jdbc.update(SQL);
		
		Settings sttExp = new Settings();
		
		sttExp.setCheckInterval(1);
		sttExp.setRefreshInterval(2);
		sttExp.setServerTimeout(3);
		sttExp.setSmtpPort(4);
		sttExp.setSmtpAdress("addr");
		
		serverDao.setSettings(sttExp);
		
		Settings stt = serverDao.getSettings();
		
		assertEquals(stt.getCheckInterval(), new Integer(1));
		assertEquals(stt.getRefreshInterval(), new Integer(2));
		assertEquals(stt.getServerTimeout(), new Integer(3));
		assertEquals(stt.getSmtpAdress(), "addr");
		assertEquals(stt.getSmtpPort(), new Integer(4));
	}
}
