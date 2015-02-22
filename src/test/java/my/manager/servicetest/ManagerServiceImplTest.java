package my.manager.servicetest;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import my.manager.dao.ManagerServerDao;
import my.manager.dao.ManagerUserDao;
import my.manager.domain.Server;
import my.manager.domain.ServerState;
import my.manager.domain.User;
import my.manager.service.ManagerServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.easymock.EasyMock.*;

@ContextConfiguration(locations ={"classpath:test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class ManagerServiceImplTest {
	
    private ManagerServiceImpl service;
	private ManagerServerDao mockServerDao;
	private ManagerUserDao mockUserDao;

	@Before
	public void setUp() throws Exception {
	          service = new ManagerServiceImpl();
	          mockServerDao = createStrictMock(ManagerServerDao.class);
	          mockUserDao = createStrictMock(ManagerUserDao.class);
	          service.setManagerDao(mockUserDao, mockServerDao);
	}

	@After
	public void tearDown() throws Exception {
		service = null;
		mockServerDao = null;
		mockUserDao = null;
	}

	@Test
	public void testUpdateServerState() {
		Server srv = new Server();
		Server dbSrv = new Server();

		expect(mockServerDao.getServerById(srv.getId())).andReturn(dbSrv);
		mockServerDao.updateServer(dbSrv);
		expectLastCall();       
        replay(mockServerDao); 
        service.updateServerState(srv, ServerState.FAIL, "");
		verify(mockServerDao);
	}
	
	@Test
	public void testUpdateUserLastLoginTime() {
		User usr = new User();
		User usrFromDb = new User();
		expect(mockUserDao.getUser(usr.getId())).andReturn(usrFromDb);
		mockUserDao.updateUser(usrFromDb);
		expectLastCall();
		
		replay(mockUserDao); 
        service.updateUserLastLoginTime(usr);
		verify(mockUserDao);
	}
	
	@Test
	public void testUpdateUserLastLoginTimeNoUser() {
		User usr = new User();
		expect(mockUserDao.getUser(usr.getId())).andReturn(null);
		
		replay(mockUserDao); 
        service.updateUserLastLoginTime(usr);
		verify(mockUserDao);
	}

	@Test
	public void testListServers() {
		List<Server> serverList = new ArrayList<Server>(); 
		expect(mockServerDao.listServers()).andReturn(serverList);
		replay(mockServerDao); 
		assertNotNull(service.listServers());
		verify(mockServerDao);
	}
	
	@Test
	public void testCreateServer() {
		Server server = new Server();
		mockServerDao.createServer(isA(Server.class));
		expectLastCall();
		replay(mockServerDao); 
		service.createServer(server);
		verify(mockServerDao);
	}
	
	@Test
	public void testDeleteServer() {
		int id = 0;
		mockServerDao.deleteServer(id);
		expectLastCall();
		replay(mockServerDao); 
		service.deleteServer(id);
		verify(mockServerDao);
	}
	
	@Test
	public void testGetUser() {
		User u = new User();
		int id = 1;
		expect(mockUserDao.getUser(id)).andReturn(u);
		expect(mockUserDao.getUser(0)).andReturn(null);
		replay(mockUserDao); 
		assertNotNull(service.getUser(id));
		assertNull(service.getUser(0));
		verify(mockUserDao);
	}
	
	@Test
	public void testCreateUser() {
		User u = new User();
		mockUserDao.createUser(u);
		expectLastCall();
		expect(mockUserDao.getUserId("")).andReturn(0);	
		mockUserDao.setUserRole(0, "ROLE_USER");
		expectLastCall();
		replay(mockUserDao); 
		
		service.createUser(u);
		verify(mockUserDao);
	}
	
	@Test
	public void testDeleteUser() {
		int id = 0;
		mockUserDao.deleteUser(id);
		expectLastCall();
		replay(mockUserDao); 
		service.deleteUser(id);
		verify(mockUserDao);
	}
	
	@Test
	public void testListUsers() {
		List<User> userList = new ArrayList<User>(); 
		expect(mockUserDao.listUsers()).andReturn(userList);
		replay(mockUserDao); 
		assertNotNull(service.listUsers());
		verify(mockUserDao);
	}
	
	@Test
	public void testUpdateUser() {
		User u = new User();
		mockUserDao.updateUser(u);
		expectLastCall();
		replay(mockUserDao); 
		service.updateUser(u);
		verify(mockUserDao);
	}
	
	@Test
	public void testGetServer() {
		Server u = new Server();
		int id = 1;
		expect(mockServerDao.getServerById(id)).andReturn(u);
		expect(mockServerDao.getServerById(0)).andReturn(null);
		replay(mockServerDao); 
		assertNotNull(service.getServer(id));
		assertNull(service.getServer(0));
		verify(mockServerDao);
	}
	
	@Test
	public void testUpdateServer() {
		Server s = new Server();
		mockServerDao.updateServer(s);
		expectLastCall();
		replay(mockServerDao); 
		service.updateServer(s);
		verify(mockServerDao);
	}
	
	@Test
	public void testListUserServers() {
		int userId = 1;
		List<Server> serverList = new ArrayList<Server>(); 
		expect(mockServerDao.listServersForUser(userId)).andReturn(serverList);
		replay(mockServerDao); 
		assertNotNull(service.listUserServers(userId));
		verify(mockServerDao);
	}
	
	@Test
	public void testListUserProhibitedServers() {
		int userId = 1;
		List<Server> serverList = new ArrayList<Server>(); 
		expect(mockServerDao.listServersNotForUser(userId)).andReturn(serverList);
		replay(mockServerDao); 
		assertNotNull(service.listUserProhibitedServers(userId));
		verify(mockServerDao);
	}
	
	@Test
	public void testGetUserId() {
		int id = 1;
		String name = "user";
		expect(mockUserDao.getUserId(name)).andReturn(id);
		replay(mockUserDao); 
		assertEquals(id, service.getUserId(name));
		verify(mockUserDao);
	}
	@Test
	public void testSetServerForUser() {
		int serverId = 1;
		int userId = 1;
		mockUserDao.setUserServerPair(userId, serverId);
		expectLastCall();
		replay(mockUserDao); 
		
		service.setServerForUser(userId, serverId);
		verify(mockUserDao);
	}

	@Test
	public void testDeleteServerForUser() {
		int serverId = 1;
		int userId = 1;
		mockUserDao.deleteUserServerPair(userId, serverId);
		expectLastCall();
		replay(mockUserDao); 
		
		service.deleteServerForUser(userId, serverId);
		verify(mockUserDao);
	}
	
	@Test
	public void testGeneratePassword() {
		for(int i = 0; i < 100; i++){
			String pass = service.generatePassword();
			assertTrue(pass.length() >= 6);
			assertTrue(pass.length() <= 16);
		}
	}
	/*
	@Test
	public void testSetUserRole() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetServerId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSettings() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetSettings() {
		fail("Not yet implemented");
	}
*/

}
