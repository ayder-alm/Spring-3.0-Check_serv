package my.manager.controllertest;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import my.manager.controller.ManagerController;
import my.manager.domain.Server;
import my.manager.domain.ServerState;
import my.manager.domain.Settings;
import my.manager.domain.User;
import my.manager.service.ManagerServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

@ContextConfiguration(locations ={"classpath:test-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class) 
public class MVCTest {

	private ManagerController controller = new ManagerController();
	private ShaPasswordEncoder mockEncoder = null;
	private ManagerServiceImpl mockService = null;
	private BindingResult bindingResult = null;
	
	private MockMvc mockMvc;
	
	@Before
	public void setUp() throws Exception {
		bindingResult = createMock(BindingResult.class);
		mockEncoder = createMock(org.springframework.security.authentication.encoding.ShaPasswordEncoder.class);
		mockService = createMock(my.manager.service.ManagerServiceImpl.class);
		controller.init(mockEncoder, mockService);
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
				       .build(); 
	}
	
	@Test
	public void testGoMinitor() throws Exception {
		mockMvc.perform(get("/")) 
        .andExpect(status().isFound()) 
        .andExpect(redirectedUrl("/monitor/"));
	}
	@Test
	public void testMonitorIfAdmin() throws Exception {
	   
	   User usr = new User(1, "name", "admin", "password", "email", null, true, true);
	   Settings settings = new Settings();
	   settings.setRefreshInterval(10);
	   Principal principal = new Principal() {
									@Override
									public String getName() {
										return "admin";
									}
							     }; 
	   expect(mockService.getUserId("admin")).andReturn(1);
	   expect(mockService.getUser(1)).andReturn(usr);
       expect(mockService.listServers()).andReturn(new ArrayList<Server>());
       expect(mockService.getSettings()).andReturn(settings);
	   replay(mockService);

       mockMvc.perform(get("/monitor/").principal(principal))
       .andExpect(status().isOk())
       .andExpect(model().attributeExists("refreshInterval")) 
       .andExpect(model().attributeExists("serverList"))
       .andExpect(view().name("monitor"));
       verify(mockService);
	}
	
	@Test
	public void testMonitorifOther() throws Exception {
		User usr = new User(2, "name2", "other", "password", "email", null, true, true);
		Settings settings = new Settings();
		settings.setRefreshInterval(10);
		Principal principal = new Principal() {
									@Override
									public String getName() {
										return "other";
									}
							     }; 
       expect(mockService.getUserId("other")).andReturn(2);
       expect(mockService.getUser(2)).andReturn(usr);
       expect(mockService.listServers()).andReturn(new ArrayList<Server>());
       expect(mockService.getSettings()).andReturn(settings);
	   replay(mockService);
       mockMvc.perform(get("/monitor/").principal(principal))
       .andExpect(status().isOk())
       .andExpect(model().attributeExists("refreshInterval")) 
       .andExpect(model().attributeExists("serverList"))
       .andExpect(view().name("monitor"));
       verify(mockService);
	}
	
	@Test
	public void testMonitorServers() throws Exception {
		expect(mockService.listServers()).andReturn(new ArrayList<Server>());
	    replay(mockService);
		mockMvc.perform(get("/monitorservers/"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("serverList"))
		.andExpect(view().name("monitorservers"));
		
		verify(mockService);
	}
	
	@Test
	public void testShowServerInfo() throws Exception {
		int id = 1;
		Server srv = new Server();
		ModelMap model = new ModelMap();
		expect(mockService.getServer(id)).andReturn(srv);
		expect(mockService.getServer(0)).andReturn(null);
		replay(mockService);
		assertEquals("serverinfo", controller.showServerInfo( id, model));
		assertEquals("redirect:/monitorservers/", controller.showServerInfo( 0, new ModelMap()));
		assertEquals(srv, model.get("server"));
		verify(mockService);
	}
	
	@Test
	public void testEditServerNotFound() throws Exception {
		mockMvc.perform(get("/editserver/{id}/",1))
		.andExpect(status().isFound())
		.andExpect(redirectedUrl("/monitorservers/"));
	}
	
	@Test
	public void testEditServerFound() {
		int id = 1;
		Server srv = new Server();
		ModelMap model = new ModelMap();
		expect(mockService.getServer(id)).andReturn(srv);
		replay(mockService);
		assertEquals("editserver", controller.editServer( id, model));
		assertEquals(srv, model.get("server"));
		verify(mockService);
	}
	
	@Test
	public void testEditServerService() {
		Server srvExp = new Server(1, "name", "adress", 1, "url", ServerState.FAIL, "response", true);
		Server srv = new Server(1, "name", "adress", 1, "url", ServerState.FAIL, "response", true);
		
		expect(bindingResult.hasErrors()).andReturn(false);
		expect(mockService.getServer(1)).andReturn(srv);
		expect(mockService.getServer(1)).andReturn(srv);
		expect(bindingResult.hasErrors()).andReturn(true);
		mockService.updateServer(srv);
		expectLastCall();
		replay(mockService, bindingResult);
		assertEquals("redirect:/monitorservers/",controller.editServerService(srvExp, bindingResult, new ModelMap()));
		assertEquals("editserver",controller.editServerService(new Server(), bindingResult, new ModelMap()));
		verify(mockService, bindingResult);
	}
	
	@Test
	public void testDeleteServerService() throws Exception {
		mockMvc.perform(get("/deleteserver/{id}/",1))
		.andExpect(status().isFound())
		.andExpect(redirectedUrl("/monitorservers/"));
	}
	
	@Test
	public void testCreateServer() throws Exception {
		mockMvc.perform(get("/createserver/"))
		.andExpect(status().isOk())
		.andExpect(view().name("createserver"));
	}

	@Test
	public void testCreateServerService() {
		Server srvExp = new Server(0, "name", "adress", 1, "url", ServerState.FAIL, "response", true);
		
		expect(bindingResult.hasErrors()).andReturn(false);
		expect(mockService.getServerId(srvExp.getName())).andReturn(0);
		mockService.createServer(srvExp);
		expectLastCall();
		replay(mockService, bindingResult);
		assertEquals("redirect:/monitorservers/",controller.createServerService(srvExp, bindingResult, new ModelMap()));
		verify(mockService, bindingResult);
	}

	@Test
	public void testMonitorUsers() throws Exception {
		expect(mockService.listUsers()).andReturn(new ArrayList<User>());
		replay(mockService);
		Principal principal = new Principal() {
				@Override
				public String getName() {
					return "admin";
				}
		     };
		
		mockMvc.perform(get("/monitorusers/").principal(principal))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("userList"))
		.andExpect(model().attributeExists("editor"))
		.andExpect(view().name("monitorusers"));
		verify(mockService);
	}

	@Test
	public void testEditUserNoId() throws Exception {
		mockMvc.perform(get("/edituser/{id}/",0))
		.andExpect(status().isFound())
		.andExpect(redirectedUrl("/monitorusers/"));
	}
	
	@Test
	public void testEditUserFoundId() throws Exception {
		int id = 1;
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "name";
			}
	     }; 
		expect(mockService.getUser(id)).andReturn(new User());
		replay(mockService);
		assertEquals("edituser", controller.editUser( id, new ModelMap(), principal));
		verify(mockService);
	}
	
	@Test
	public void testEditUserService() {
		User usr = new User(1, "name", "login", "password", "email", null, true, true);
		Principal principal = new Principal() {
			@Override
			public String getName() {
				return "name";
			}
	     };
		expect(bindingResult.hasFieldErrors("name")).andReturn(false);
		expect(bindingResult.hasFieldErrors("email")).andReturn(false);
		
		expect(mockService.getUser(1)).andReturn(usr);
		mockService.updateUser(isA(User.class));
		expectLastCall();
		replay(mockService, bindingResult);
		assertEquals("redirect:/monitorusers/",controller.editUserService(usr, bindingResult, new ModelMap(), principal));
		verify(mockService, bindingResult);
	}
	
	@Test
	public void testDeleteUserService() throws Exception {
		expect(mockService.getUser(1)).andReturn(new User());
		mockService.deleteUser(1);
		expectLastCall();
		replay(mockService);
		mockMvc.perform(get("/deleteuser/{id}/",1))
		.andExpect(status().isFound())
		.andExpect(redirectedUrl("/monitorusers/"));
		verify(mockService);
	}

	@Test
	public void testCreateUser() throws Exception {
		mockMvc.perform(get("/createuser/"))
		.andExpect(status().isOk())
		.andExpect(view().name("createuser"));
	}

	@Test
	public void testCreateUserServiceNotFoundId() {
		User usr = new User(1, "name", "login", "password", "email", null, true, true);
		
		expect(bindingResult.hasErrors()).andReturn(false);
		expect(mockService.getUserId(isA(String.class))).andReturn(1);
		replay(mockService,bindingResult);
		assertEquals("createuser", controller.createUserService(usr, bindingResult, new ModelMap()));
		verify(mockService, bindingResult);
	}
	
	@Test
	public void testCreateUserServiceValidId1() {
		ModelMap model = new ModelMap();
		User user = new User();
		
		expect(bindingResult.hasErrors()).andReturn(false);
		expect(mockService.getUserId("")).andReturn(0);
		expect(mockEncoder.encodePassword("", "")).andReturn("");
		mockService.createUser(user);
		expectLastCall();
		
		replay(mockService, bindingResult);
		assertEquals("redirect:/monitorusers/", controller.createUserService(user, bindingResult, model));
		verify(mockService, bindingResult);
	}	
	
	@Test
	public void testCreateUserServiceValidId2() {
		ModelMap model = new ModelMap();
		User user = new User();
		expect(bindingResult.hasErrors()).andReturn(false);
		
		expect(mockService.getUserId("")).andReturn(1);
		replay(mockService,bindingResult);
		assertEquals("createuser", controller.createUserService(user, bindingResult, model));
		assertEquals(user, model.get("user"));
		verify(mockService, bindingResult);
	}
	
	@Test
	public void testAssignServers() throws Exception {
		expect(mockService.listUserServers(2)).andReturn(new ArrayList<Server>());
		expect(mockService.listUserProhibitedServers(2)).andReturn(new ArrayList<Server>());
		expect(mockService.getUser(2)).andReturn(new User());
		replay(mockService);
		mockMvc.perform(get("/assignservers/{userId}/",2))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("belong"))
		.andExpect(model().attributeExists("others"))
		.andExpect(view().name("assignservers"));
	}
	
	@Test
	public void testAssignServersService() {
		ModelMap model = new ModelMap();
		String belong[] = {"1"};
		List<Server> list = new ArrayList<Server>();
		list.add(new Server());
		list.add(new Server());
		list.get(0).setId(1);
		expect(mockService.listServers()).andReturn(list);
		mockService.deleteServerForUser(0, 0);
		expectLastCall();
		mockService.setServerForUser(0, 1);
		expectLastCall();
		replay(mockService);
		
		assertEquals("redirect:/edituser/0/", controller.assignServersService(belong, "0", model));
		verify(mockService);
	}

	@Test
	public void testChangeSettings() throws Exception {
		expect(mockService.getSettings()).andReturn(new Settings());
		replay(mockService);
		mockMvc.perform(get("/changesettings/"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("settings"))
		.andExpect(view().name("changesettings"));
		verify(mockService);
	}

	@Test
	public void testChangeSettingsService() {
		expect(bindingResult.hasErrors()).andReturn(false);
		mockService.setSettings(null);
		expectLastCall();
		replay(mockService);
		assertEquals("redirect:/monitorusers/", controller.changeSettingsService(null, bindingResult,null));
		verify(mockService);
	}

	@Test
	public void testChangePassword() throws Exception {
		mockMvc.perform(get("/changepassword/"))
		.andExpect(status().isOk())
		.andExpect(view().name("changepassword"));
	}

	@Test
	public void testChangePasswordService() {
		User usr = new User();
		Principal principal = new Principal() {
			
			@Override
			public String getName() {
				return "";
			}
		};
		expect(mockService.getUserId("")).andReturn(0);
		expect(mockService.getUser(0)).andReturn(usr);
		mockService.updateUser(usr);
		expectLastCall();
		replay(mockService);
		assertEquals("redirect:/monitor/", controller.changePasswordService("", principal, new ModelMap()));
		verify(mockService);
	}

	@Test
	public void testRestore() throws Exception {
		mockMvc.perform(get("/restore/"))
		.andExpect(status().isOk())
		.andExpect(view().name("restore"));
	}
	
	@Test
	public void testRestoreServiceOk() {
		String email = "1@mail.ru";
		String login = "login";
		User usr = new User();
		usr.setEmail(email);
		ModelMap model = new ModelMap();
		
		expect(mockService.getUserId(login)).andReturn(1);
		expect(mockService.getUser(1)).andReturn(usr);
		expect(mockService.generatePassword()).andReturn("");
		mockService.updateUser(usr);
		expectLastCall();
		mockService.sendPasswordToUser("", usr);
		expectLastCall();
		
		replay(mockService);
		
		assertEquals("email", controller.restoreService(email, login, model));
		verify(mockService);
	}
	
	@Test
	public void testRestoreServiceDontMatch() {
		String email = "1@mail.ru";
		String login = "login";
		User usr = new User();
		usr.setEmail(email + "other");		//other email
		ModelMap model = new ModelMap();
		
		expect(mockService.getUserId(login)).andReturn(1);
		expect(mockService.getUser(1)).andReturn(usr);
		replay(mockService);
		
		assertEquals("restore", controller.restoreService(email, login, model));
		assertNotNull(model.get("error"));
		verify(mockService);
	}
	
	@Test
	public void testRestoreServiceNoUser() {
		String email = "1@mail.ru";
		String login = "login";
		ModelMap model = new ModelMap();
		
		expect(mockService.getUserId(login)).andReturn(0);
		replay(mockService);
		
		assertEquals("restore", controller.restoreService(email, login, model));
		assertNotNull(model.get("error"));
		verify(mockService);
	}

	@Test
	public void testLoginOk() throws Exception {
		mockMvc.perform(get("/login/"))
		.andExpect(status().isOk())
		.andExpect(view().name("login"));
	}
	
	@Test
	public void testLoginError() throws Exception {
		mockMvc.perform(get("/login/").param("error", "true"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("error"))
		.andExpect(view().name("login"));
	}
	
	@Test
	public void testLoginServiceAdminLoginExists() throws Exception {
		expect(mockService.getUserId("admin")).andReturn(1);
		expect(mockService.getUser(1)).andReturn(new User());
		expect(mockEncoder.encodePassword("password","admin")).andReturn("");
		mockService.updateUser(isA(User.class));
		expectLastCall();
		replay(mockService, mockEncoder);
		mockMvc.perform(post("/login/")
				.param("j_username", "admin")
				.param("j_password", "password"))
		.andExpect(status().isOk())
		.andExpect(forwardedUrl("/j_spring_security_check"));
		verify(mockService, mockEncoder);
	}
	
	@Test
	public void testFirstRunAdminExists() throws Exception {
		expect(mockService.getUserId("admin")).andReturn(1);
		replay(mockService, mockEncoder);
		mockMvc.perform(post("/login/firstrun")
				.param("password", "pass"))
		.andExpect(status().isOk())
		.andExpect(view().name("login"));
		verify(mockService);
	}
	
	@Test
	public void testFirstRunNoAdminExists() throws Exception {
		String password = "pass";
		
		expect(mockService.getUserId("admin")).andReturn(0);
		
		mockService.createUser(isA(User.class));
		expectLastCall();
		expect(mockService.getUserId("admin")).andReturn(1);
		mockService.setUserRole(1, "ROLE_ADMIN");
		expectLastCall();
		replay(mockService);
		
		assertEquals("login", controller.firstRun(password));
		verify(mockService);
	}
	
}
