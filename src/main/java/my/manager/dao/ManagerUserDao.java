package my.manager.dao;

import java.util.List;
import my.manager.domain.User;
/**
 * Interface for user DAO layer of MVC
 * <p>
 * used in Service layer
 */
public interface ManagerUserDao {
	
    public void setUserServerPair(int userId, int serverId);
    /**
	 * Deletes server from user's server list 
	 * @param userId user owner id
	 * @param serverId server to be assigned to owner
	 */
    public void deleteUserServerPair(int userId, int serverId);
    /**
	 * Lists server that belong to user
	 * @param userId - id of owner user
	 * @return List<Server> list of users
	 */
    public User getUser(int id); 
    /**
	 * Creates given user
	 * @param user - user to create
	 */
    public void createUser(User user);
    /**
	 * Deletes user by id
	 * @param id of user to be deleted
	 */
    public void deleteUser(int id);
    /**
	 * Updates given user in database
	 * @param u - User to be updated
	 */
    public void updateUser(User user);
    /**
	 * Gets list of all users
	 * @return List<User> list of Users in database
	 */
    public List<User> listUsers();
    /**
	 * Sets user role to be used during authentication
	 * @param userId - int user id
	 * @param role - role to be set for user
	 */
    void setUserRole(int userId, String role);
	/**
	 * Gets user by id
	 * @param login to search user id
	 * @return userID or 0 if error or does not exist
	 */
	public int getUserId(String login);
}
