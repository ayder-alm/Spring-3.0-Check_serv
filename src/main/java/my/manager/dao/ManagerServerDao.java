package my.manager.dao;

import java.util.List;
import my.manager.domain.Server;
import my.manager.domain.ServerState;
import my.manager.domain.Settings;
/**
 * Interface for DAO layer of MVC
 * <p>
 * used in Service layer
 */
public interface ManagerServerDao {
	/**
	 * Gets server by id
	 * @param id
	 * @return Server or null if not exists or error
	 */
	public Server getServerById(int id);
	/**
	 * Gets all server into list
	 * @return List<Server> - list of all servers in database
	 */
	public List<Server> listServers();
	/**
	 * Creates server in database
	 * @param server - server to create
	 */
	public void createServer(Server server);
	/**
	 * Deletes server by given id
	 * @param id of server to delete
	 */
	public void deleteServer(int id);
	/**
	 * Updates server in database
	 * @param s - Server to update
	 */
	public void updateServer(Server server);
    /**
	 * Gets server by their state (fail, ok, warn)
	 * @param state according to which to select
	 * @return Server list
	 */
    public List<Server> getServersByState(ServerState state);
    /**
	 * Gets if server is active or not according to id
	 * @param servId - id to check upon server being active
	 * @return true if server is active
	 */
    public boolean isActiveServer(int servId);
	/**
	 * Gets server id by its name
	 * @param name - server name to find server
	 * @return serverId or 0 on error or absence
	 */
    public int getServerId(String name);
    
	/**
	 * Gets current settings
	 * @return Settings object with all settings
	 */
	public Settings getSettings();
	/**
	 * Sets system settings
	 * @param s - Settings object to set
	 */
	public void setSettings(Settings s);
	/**
	 * Lists server that belong to user
	 * @param userId - id of owner user
	 * @return List<Server> list of users
	 */
    public List<Server> listServersForUser(int userId);
    /**
	 * Lists server that don't belong to user
	 * @param userId - user not owner id
	 * @return List<Server> list of users
	 */
    public List<Server> listServersNotForUser(int userId);
}
