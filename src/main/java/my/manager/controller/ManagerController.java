package my.manager.controller;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.validation.Valid;
import my.manager.domain.Server;
import my.manager.domain.Settings;
import my.manager.domain.User;
import my.manager.service.ManagerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
/**
 * Main controller class responsible for site mapping, user-service 
 * relation regulation and validation.
 * Those functions that end with Service prefix usually process data 
 * and have no view source attached
 * @author Ayder
 */
@Controller
public class ManagerController {
	
	public static Logger log = Logger.getRootLogger();
	private ShaPasswordEncoder encoder;
	private ManagerService service;
	/**
	 * initialization method
	 */
	@Autowired
	public void init(ShaPasswordEncoder encoder, ManagerService service){
		this.encoder=encoder;
		this.service=service;
	}
	/**
	 * Redirects from root to default monitor page
	 */
	
	@RequestMapping("/")
	public String goMinitor(){
		return "redirect:/monitor/";
	}
	/**
	 * Default monitor page responsible for displaying assigned servers for users
	 * and all servers to "admin" user
	 */
	@RequestMapping(value = "/monitor/")
	public String monitor(Principal principal, ModelMap model) {
		List<Server> serverList = new ArrayList<Server>();
		String login = "";
		boolean isAdmin = false;
		int userId = 0;
		
		if(principal!=null){
			login = principal.getName();
			userId = service.getUserId(login);
		}
		
		isAdmin = service.getUser(userId).isAdmin();
		
		if(isAdmin){
			serverList = service.listServers();
		}
		else
		{
			serverList = service.listUserServers(userId);
		}
		model.addAttribute("refreshInterval", service.getSettings().getRefreshInterval());
		model.addAttribute("serverList", serverList);
		return "monitor";
	}
	/**
	 * Redirects to monitor servers view
	 * <p>
	 * used only by "admin"
	 */	
	@RequestMapping(value = "/monitorservers/")
	public String monitorServers(ModelMap model) {
		List<Server> serverList = service.listServers();
		model.addAttribute("serverList", serverList);
		return "monitorservers";
	}
	/**
	 * Shows server details for single server uses server id
	 * <p>
	 * used only by "admin"
	 */
	@RequestMapping(value ="/serverinfo/{id}/",method = RequestMethod.GET)
	public String showServerInfo(@PathVariable("id") int id,  ModelMap model) {
		Server server = service.getServer(id);
		if(server!=null) {
			model.addAttribute("server", server);
			return "serverinfo";
		}
		return "redirect:/monitorservers/";
	}
	/**
	 * Redirects to server edit form according to server id
	 * <p>
	 * used only by "admin"
	 */
	@RequestMapping(value ="/editserver/{id}/",method = RequestMethod.GET)
	public String editServer(@PathVariable("id") int id, ModelMap model) {
		Server server = service.getServer(id);
		if(server!=null) {
			model.addAttribute("server", server);
			return "editserver";
		}
		return "redirect:/monitorservers/";
	}
	/**
	 * Processes server check and update after "editserver" page
	 * <p>
	 * used only by "admin"
	 */
	@RequestMapping(value ="/editserver/",method = RequestMethod.POST)
	public String editServerService(@Valid @ModelAttribute Server srv, BindingResult result, ModelMap model) {
		String srvPrevName = null;
		if(result.hasErrors())
		{
			model.addAttribute("server",srv);
			return "editserver";
		}else{
			srvPrevName = service.getServer(srv.getId()).getName();//get previous name of server
			
			if( ( !srvPrevName.equals(srv.getName()) ) && (service.getServerId(srv.getName()) != 0)) 	//if server changed its name and  
			{												//another with given name exists send back to edit with another name
				model.addAttribute("duplication", " ");
				return "editserver";
			} else {
			
				Server server = service.getServer(srv.getId());	//get server, update fields and write to database
				
				server.setName(srv.getName());
				server.setAdress((srv.getAdress()));
				server.setUrlPath((srv.getUrlPath()));
				server.setPort(srv.getPort());
				server.setActive(srv.isActive());
				server.setResponse("Waiting for status check.");
			
				service.updateServer(server);
				return "redirect:/monitorservers/";
			}
		}
	}
	/**
	 * Deletes server by id. Has no attached view source
	 * <p>
	 * used only by "admin"
	 */
	@RequestMapping(value ="/deleteserver/{id}/",method = RequestMethod.GET)
	public String deleteServerService(@PathVariable("id") int id,  ModelMap model) {
		Server server = service.getServer(id);
		if(server!=null) {
			service.deleteServer(id);
		}
		return "redirect:/monitorservers/";
	}
	/**
	 * Redirects to create server view
	 * <p>
	 * used only by "admin"
	 */
	@RequestMapping(value ="/createserver/", method = RequestMethod.GET)
	public String createServer(ModelMap model) {
		return "createserver";
	}
	/**
	 * Checks if create server POST data is valid and if server already exists
	 * redirects back to create server with error if any occured during validation
	 * <p>
	 * used only by "admin"
	 */
	@RequestMapping(value ="/createserver/", method = RequestMethod.POST)
	public String createServerService(@Valid @ModelAttribute Server srv, BindingResult result,  ModelMap model) {
		
		if( result.hasErrors() ){
			return "createserver";
		} else {
			
			if(service.getServerId(srv.getName()) != 0) 	//if server with given name exists send back to create server
			{
				model.addAttribute("exists", "server already exists");
				return "createserver";
			}else{
				service.createServer(srv);											  //create if valid
				return "redirect:/monitorservers/";
			}
		}
	}
	/**
	 * Redirects to monitor user view showing all users where they
	 * can be edited, created or deleted
	 * <p>
	 * used only by "admin"
	 */
	@RequestMapping(value = "/monitorusers/")
	public String monitorUsers(ModelMap model, Principal principal) {
		List<User> userList = service.listUsers();
		model.addAttribute("userList", userList);
		model.addAttribute("editor", principal.getName());
		return "monitorusers";
	}
	/**
	 * Redirects to edit user view based on user id.
	 * Checks if user exists in database in case of invalid id provided
	 * <p>
	 * used only by "admin"
	 */
	@RequestMapping(value ="/edituser/{id}/",method = RequestMethod.GET)
	public String editUser(@PathVariable("id") int id,  ModelMap model, Principal principal) {
		User user = service.getUser(id);
		if(user!=null) {
			model.addAttribute("user", user);
			model.addAttribute("editor", principal.getName());
			return "edituser";
		}
		return "redirect:/monitorusers/";
	}
	/**
	 * Checks and updates users sent by POST.
	 * sends back if any error, updates user otherwise.
	 * <p>
	 * used only by "admin"
	 */
	@RequestMapping(value ="/edituser/", method = RequestMethod.POST)
	public String editUserService(@Valid @ModelAttribute User usr, BindingResult result, ModelMap model, Principal principal) {
		
		if(result.hasFieldErrors("name") || result.hasFieldErrors("email" )){
			return "edituser";
		}else{
			User user = service.getUser(usr.getId());
			user.setEmail(usr.getEmail());
			user.setName(usr.getName());
			
			if( user.getLogin().equals("admin")){
				service.updateUser(user);
			} else {

				if( principal.getName().equals("admin") ){
					user.setActive(usr.isActive());
					if(usr.isAdmin()){
						user.setAdmin(true);
						service.updateUser(user);
						service.setUserRole(user.getId(), "ROLE_ADMIN");
					}else{
						user.setAdmin(false);
						service.updateUser(user);
						service.setUserRole(user.getId(), "ROLE_USER");
					}
				} else {
					if( !usr.isAdmin() )
						user.setActive(usr.isActive());
					service.updateUser(user);
				}
			}
			return "redirect:/monitorusers/";
		}
	}
	/**
	 * Deletes user based on user id if user is present and is not"admin"
	 * <p>
	 * used only by "admin"
	 */
	@RequestMapping(value ="/deleteuser/{id}/",method = RequestMethod.GET)
	public String deleteUserService(@PathVariable("id") int id,  ModelMap model) {
		User user = service.getUser(id);
		if( (user != null) || (!service.getUser(id).getLogin().equals("admin")) ){
			service.deleteUser(id);
		}
		return "redirect:/monitorusers/";
	}
	/**
	 * Redirects to create userview
	 * <p>
	 * used only by "admin"
	 */
	@RequestMapping(value ="/createuser/", method = RequestMethod.GET)
	public String createUser(ModelMap model) {
		return "createuser";
	}
	/**
	 * Creates user sent via POST method if user does not already exist and is valid
	 * <p>
	 * used only by "admin"
	 */
	@RequestMapping(value ="/createuser/", method = RequestMethod.POST)
	public String createUserService(@Valid @ModelAttribute User usr, BindingResult result, ModelMap model) {
		
		if(result.hasErrors()){
			return "createuser";
		}else{
			if(service.getUserId(usr.getLogin()) != 0){ //if User already exists
				model.addAttribute("exists", "");
				model.addAttribute("user",usr);
				return "createuser";
			}
				
			String newPassword = encoder.encodePassword(usr.getPassword(), usr.getLogin());
			usr.setPassword(newPassword);
			service.createUser(usr);
			return "redirect:/monitorusers/";
		}
	}
	/**
	 * Redirects to assign servers for user view
	 * <p>
	 * used only by "admin"
	 */
	@RequestMapping(value ="/assignservers/{userId}/", method = RequestMethod.GET)
	public String assignServers(@PathVariable("userId") int userId, ModelMap model) {
		List<Server> belong = service.listUserServers(userId);
		List<Server> others = service.listUserProhibitedServers(userId);
		model.addAttribute("belong",belong);
		model.addAttribute("others", others);
		model.addAttribute("user", service.getUser(userId));
		
		return "assignservers";
	}
	/**
	 * Assigns servers for user passed by id.
	 * Server ids are stored in string array
	 * <p>
	 * used only by "admin"
	 */
	@RequestMapping(value ="/assignservers/", method = RequestMethod.POST)
	public String assignServersService(@RequestParam("belongId") String[] belong, 
									   @RequestParam("userId") String userIdString,
									   ModelMap model) {
		int serverId = 0;
		int userId =  Integer.parseInt(userIdString);
		
		List<Integer> deleteIds = new ArrayList<Integer>();		//ids to delete from user servers
		List<Integer> addIds = new ArrayList<Integer>();		//ids to be added
		List<Server> allServers = service.listServers();		
		
		for(Server server: allServers){							//initially accept that all servers don't belong
			deleteIds.add(server.getId());
		}
		
		for(String idString : belong){							//send server ids from rermove to add list
			try{
				serverId = Integer.parseInt(idString);
				deleteIds.remove( (Integer)serverId );
				addIds.add(serverId);
			}
			catch(NumberFormatException e)						//in case some error
			{
				//do nothing
			}
		}
		
		for(int servId : deleteIds){							//clear ids that don't belong
			service.deleteServerForUser(userId, servId);
		}
		
		for(int servId : addIds){								//add ids that belong
			service.setServerForUser(userId, servId);
		}
		
		return "redirect:/edituser/" + userId + "/";			//go back to user edit form
	}
	/**
	 * Redirects to change settings view
	 * <p>
	 * used only by "admin"
	 */
	@RequestMapping(value ="/changesettings/", method = RequestMethod.GET)
	public String changeSettings(ModelMap model) {

		model.addAttribute("settings", service.getSettings());
		return "changesettings";
	}
	/**
	 * Saves new settings for application
	 * <p>
	 * used only by "admin"
	 */
	@RequestMapping(value ="/changesettings/", method = RequestMethod.POST)
	public String changeSettingsService(@Valid @ModelAttribute Settings settings, BindingResult result, ModelMap model){
		if(result.hasErrors()){
			return "changesettings";
		}
		else{
			service.setSettings(settings);
			return "redirect:/monitorusers/";
		}
	}
	/**
	 * Redirects to change password view
	 * <p>
	 * used only by registered users
	 */
	@RequestMapping(value ="/changepassword/", method = RequestMethod.GET)
	public String changePassword(ModelMap model) {
		return "changepassword";
	}
	/**
	 * Gets principal user? encodes new password and saves it
	 * <p>
	 * used only by "admin"
	 */
	@RequestMapping(value ="/changepassword/", method = RequestMethod.POST)
	public String changePasswordService(@RequestParam("newpassword") String pass, Principal principal, ModelMap model) {
		
		String currentUser = principal.getName();
		int id = service.getUserId(currentUser);
		User user = service.getUser(id);
		String newPassword = encoder.encodePassword(pass, user.getLogin());	
																			
		user.setPassword(newPassword);
		service.updateUser(user);
		return "redirect:/monitor/";
	}
	/**
	 * Redirects to restore account view
	 * <p>
	 * used by anonymous users
	 */
	@RequestMapping(value ="/restore/", method = RequestMethod.GET)
	public String restore(ModelMap model) {
		return "restore";
	}
	/**
	 * Check if user exists, email belongs to user.
	 * Generates new password and sends to email view.
	 * Sends back with error if any problem.
	 * <p>
	 * used only by "admin"
	 */
	@RequestMapping(value ="/restore/", method = RequestMethod.POST)
	public String restoreService(@RequestParam("email") String email, @RequestParam("login") String login, ModelMap model) {
		User user;
		String formEmail = email.trim();
		String newPassword = "", encodedPassword;
		int userId = service.getUserId(login);
		
		if(userId == 0)			//user is not present
		{
			model.addAttribute("error", "user doesn't exist");
			return "restore";
		}
		user = service.getUser(userId);
		if( formEmail.equals( user.getEmail() )){	//checks if email belongs to user then generates new password
			newPassword = service.generatePassword();
			encodedPassword = encoder.encodePassword(newPassword, user.getLogin());	
			user.setPassword(encodedPassword);
			service.updateUser(user);
			
			service.sendPasswordToUser(newPassword, user);
			model.addAttribute("email", user.getEmail());
			return "email";
		}
		else{
			model.addAttribute("error", "email does not match");
			return "restore";
		}
	}
	
	/**
	 * Redirects to login view. Sets error flag if error happened
	 * <p>
	 * used only by "admin"
	 */
	@RequestMapping(value ="/login/",  method = RequestMethod.GET)
	public String login( @RequestParam(value="error", required=false) String error, ModelMap model)
	{
		if(error!=null)
			model.addAttribute("error",true);
			
		return "login";
	}
	
	/**
	 * Redirects to /login/firstrun view if login="admin" pass="admin" and no other admin exists.
	 * Checks "admin" account presence redirects to /login/firstrun to check if admin's password 
	 * should be changed during first run.
	 * <p>
	 * Sets last login time for users and redirects to standart spring security form handler
	 * used by anonymous users
	 */
	@RequestMapping(value ="/login/",  method = RequestMethod.POST)
	public String loginService(@RequestParam("j_username") String login, @RequestParam("j_password") String password, ModelMap model) {
		login = login.trim();
		if( (password.equals("admin") ) && (login.equals("admin") ) ){	//if admin:admin credentials provided
			if(service.getUserId("admin")==0)							//and "admin" account does not exist
			{															//redirect to change password
				return "adminchangepassword";
			}
		}
	    
		int userId = service.getUserId(login);					
		
		if(userId==0)
			return "redirect:/login/?error=true";						//if user does not exist return error
		
		User usr = service.getUser(userId);
		String encPassword = encoder.encodePassword(password, login);
		
		if( usr.getPassword().equals(encPassword) )						//if credentials are ok set last login time
		{
			usr.setLastLoginTime( new Timestamp( Calendar.getInstance().getTimeInMillis() ) );
			service.updateUser(usr);
		}
		return "forward:/j_spring_security_check";						//return to spring default handler
	}
	
	/**
	 * Used to set password of "admin" account during first login
	 * <p>
	 * used by anonymous users
	 */
	@RequestMapping(value ="/login/firstrun",  method = RequestMethod.POST)
	public String firstRun(@RequestParam("password") String password) {
		User admin;
		String encodedPass = "";
		int userId = service.getUserId("admin"); 						//if "admin" is absent create new "admin" account
																		//and set new password
		if( userId == 0 )
		{
			admin = new User();
			admin.setLogin("admin");
			admin.setActive(true);
			admin.setAdmin(true);
			admin.setEmail("admin@servermanager.net");
			admin.setName("Admin Admin");
			encodedPass = encoder.encodePassword(password, "admin");	
			admin.setPassword(encodedPass);
			service.createUser(admin);
			service.setUserRole(service.getUserId("admin"), "ROLE_ADMIN");
			log.info("User admin is created!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}
		return "login";													//if admin present return back and do nothing
	}

	@RequestMapping("/400")
	public String error400(ModelMap model){
		addErrorForModel(model, 400);
		return "error";
	}
	
	@RequestMapping("/403")
	public String error403(ModelMap model){
		addErrorForModel(model, 403);
		return "error";
	}
	
	@RequestMapping("/404")
	public String error404(ModelMap model){
		addErrorForModel(model, 404);
		return "error";
	}
	
	private void addErrorForModel(ModelMap model, int errorCode){
		model.addAttribute("errorName","error" + errorCode);
		model.addAttribute("errorBody","error" + errorCode + ".body");
		model.addAttribute("errorCause","error" + errorCode + ".cause");
	}
}	 
