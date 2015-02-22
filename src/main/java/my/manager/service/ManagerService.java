package my.manager.service;

import java.util.List;
import my.manager.domain.*;
/**
 * 
 * This class is interface for MVC service layer.
 * Its implemented classes are to be autowired into controller class.
 * Most methods are delegates to DAO layer.
 *
 * @author Ayder
 *
 */
public interface ManagerService {
	/**
	 * Gets user by id 
	 * @param id - id of needed user
	 * @return User
	 */
	public User getUser(int id);
	/**
	 * Gets user id by given login
	 * @param login - login of needed user
	 * @return id of user
	 */
	public int getUserId(String login);
	/**
	 * Creates user
	 * @param user - user to create
	 */
	public void createUser(User user);
	/**
	 * Sets user role for spring security 
	 * @param userId - id of given user
	 * @param role - to set for user
	 */
	public void setUserRole(int userId, String role);
	/**
	 * Updates User
	 * @param user
	 */
	public void updateUser(User user);
	/**
	 * Deletes user with given id
	 * @param id - id of given user
	 */
	public void deleteUser(int id);
    /**
	 * Gets all users from DAO  
	 * @return list of all users
	 */
    public List<User> listUsers();
	/**
	 * Updates last login time for user. 
	 * Used in controller to register user successful login.
	 * @param user - User to set last login time
	 */
    public void updateUserLastLoginTime(User user);
	/**
	 * Updates server state in database. Used by ServerStateUpdater class.
	 * @param server - updated server new state to be set, response to be set
	 * @param state - state to be set for server
	 * @param response - response to be set for server
	 */
    public void updateServerState(Server server, ServerState state, String response);
    /**
	 * Get system settings 
	 * @return settings object
	 */
    public Settings getSettings();
    /**
	 * Sets system settings 
	 * @param s - settings
	 */
	public void setSettings(Settings s);
	/**
	 * Gets server by id
	 * @param id - id of server 
	 * @return Server
	 */
    public Server getServer(int id);
	/**
	 * Gets server id by its name
	 * @param name - name of server 
	 * @return id - of the server
	 */
    public int getServerId(String name);
    /**
	 * Deletes server by its id
	 * @param id- ID of server 
	 */
	public void deleteServer(int id);
	/**
	 * Creates new server 
	 * @param server - new server to create 
	 */
    public void createServer(Server server);
    /**
	 * Updates given server 
	 * @param server - new server to create 
	 */
    public void updateServer(Server server);
    /**
	 * Gets all servers as a List 
	 * @return List of servers
	 */
    public List<Server> listServers();
    /**
	 * Sets servers that user has access to
	 * @param userId - id of user
	 * @param serverId - id of server to set for user
	 */
    public void setServerForUser(int userId, int serverId);
    /**
   	 * Deletes server from list of servers that user has access to
   	 * @param userId - id of user
   	 * @param serverId - id of server to set for user
   	 */
    public void deleteServerForUser(int userId, int serverId);
    /**
	 * Gets not owned servers for user
	 * @param ownerId - id of user
	 * @return List of user not owned servers
	 */
	List<Server> listUserProhibitedServers(int ownerId);
	  /**
		 * Gets owned servers for user 
		 * @param ownerId - id of user
		 * @return List of user owned servers
	  */
	List<Server> listUserServers(int ownerId);
	/**
	 * Generates password that consists of [a-z] and [0-9].
	 * Length is 6 to 16 characters
	 * @return password on success
	 */
	public String generatePassword();
	/**
	 * Sends password to user
	 * via email service.
	 * @param pass
	 * @param usr
	 */
	public void sendPasswordToUser(String pass, User usr);
	
}
