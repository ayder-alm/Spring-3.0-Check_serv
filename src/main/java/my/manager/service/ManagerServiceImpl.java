package my.manager.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import my.manager.dao.ManagerUserDao;
import my.manager.dao.ManagerServerDao;
import my.manager.domain.Server;
import my.manager.domain.ServerState;
import my.manager.domain.Settings;
import my.manager.domain.User;
/**
 * This class is implementation of MVC service layer interface.
 * It's autowired into controller class.
 * Most methods are delegates to DAO layer, so please refer to DAO documentation if it 
 * is not present here.
 * <p>
 * Delegate methods to DAO layer are prepended by @Transactional(readOnly=) annotation.
 * @author Ayder
 */
@Service("service")
public class ManagerServiceImpl implements ManagerService {
	
	@Autowired
	private ManagerUserDao userDao;
	@Autowired
	private ManagerServerDao serverDao;
	
	public static Logger log = Logger.getLogger(ManagerServiceImpl.class);
	/**
	 * Used in testing to insert mockDao
	 * @param dao
	 */
	public void setManagerDao(ManagerUserDao userdao, ManagerServerDao serverDao)
	{
		this.userDao = userdao;
		this.serverDao = serverDao;
	}
	
	@Override
	@Transactional(readOnly = false) 
	public void updateServerState(Server server, ServerState state, String response) {
			
			Server serv = serverDao.getServerById(server.getId());
			Date date = new Date();
			
			serv.setState(state);
			serv.setResponse(response);
			serv.setLastCheckTime( new Timestamp( date.getTime() ) );
			serverDao.updateServer(serv);
	}
	
	@Override
	@Transactional(readOnly = false) 
	public void updateUserLastLoginTime(User U) {
		User user = userDao.getUser(U.getId());
		
		if( user != null ) {
			Date date = new Date();
			user.setLastLoginTime( new Timestamp( date.getTime() ) );
			userDao.updateUser(user);
		} 
	}

	@Override
	@Transactional(readOnly = true) 
	public List<Server> listServers() {
		
		return serverDao.listServers();
	}

	@Override
	@Transactional(readOnly = false) 
	public void createServer(Server server) {
		serverDao.createServer(server);
	}

	@Override
	@Transactional(readOnly = false) 
	public void deleteServer(int id) {
		serverDao.deleteServer(id);
	}

	@Override
	@Transactional(readOnly = true) 
	public User getUser(int id) {
		
		return userDao.getUser(id);
	}

	@Override
	@Transactional(readOnly = false) 
	public void createUser(User user) {
		userDao.createUser(user);								//create user in database
		int userId = userDao.getUserId(user.getLogin());		//get assigned id for new user
		userDao.setUserRole(userId, "ROLE_USER");				//set default 'ROLE_USER' to newly registered user (can be changed later)
	}

	@Override
	@Transactional(readOnly = false) 
	public void deleteUser(int id) {
		userDao.deleteUser(id);
	}

	@Override
	@Transactional(readOnly = true) 
	public List<User> listUsers() {
		return userDao.listUsers();
	}

	@Override
	@Transactional(readOnly = false) 
	public void updateUser(User user) {
		userDao.updateUser(user);
	}

	@Override
	@Transactional(readOnly = true) 
	public Server getServer(int id) {
		
		return serverDao.getServerById(id);
	}

	@Override
	@Transactional(readOnly = false) 
	public void updateServer(Server server) {
		serverDao.updateServer(server);
	}

	@Override
	@Transactional(readOnly = true) 
	public List<Server> listUserServers(int ownerId) {
		
		return serverDao.listServersForUser(ownerId);
	}

	@Override
	@Transactional(readOnly = true) 
	public List<Server> listUserProhibitedServers(int ownerId) {
		
		return serverDao.listServersNotForUser(ownerId);
	}
	
	@Override
	@Transactional(readOnly = true) 
	public int getUserId(String login) {

		return userDao.getUserId(login);
	}

	@Override
	@Transactional(readOnly = false) 
	public void setServerForUser(int userId, int serverId) {
		userDao.setUserServerPair(userId, serverId);
	}

	@Override
	@Transactional(readOnly = false) 
	public void deleteServerForUser(int userId, int serverId) {

		userDao.deleteUserServerPair(userId, serverId);
	}

	@Override
	@Transactional(readOnly = false)
	public void setUserRole(int userId, String role) {
		userDao.setUserRole(userId, role);
	}

	@Override
	@Transactional(readOnly = true)
	public int getServerId(String name) {
		return serverDao.getServerId(name);
	}

	@Override
	@Transactional(readOnly = true)
	public Settings getSettings() {
		
		return serverDao.getSettings();
	}

	@Override
	@Transactional(readOnly = false)
	public void setSettings(Settings settings) {
		serverDao.setSettings(settings);
	}

	@Override
	public String generatePassword() {
		StringBuffer password = new StringBuffer(10);
		
		String symbols = "abcdefghijklmnopqrstuvwxyz1234567890";
		
		Random rand = new Random();
		
		int length = rand.nextInt(10) + 6;
		
		for(int i = 0; i < length; i ++)
		{
			password.append(symbols.charAt(rand.nextInt(36)));
			//log.debug(password);
		}
		
		return new String(password.toString());
	}

	@Override
	public void sendPasswordToUser(String pass, User usr) {
		String to = usr.getEmail();
                
        final String username = "server.manager.test@gmail.com";	//gmail login 
		final String password = "servermanager";				//gmail password
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");						//gmail port serverDao.getSettings().getSmtpPort());
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
        
        try {
            // Create mail message object
            Message msg = new MimeMessage(session);
 
            // Set attributes
            //msg.setFrom(new InternetAddress(serverDao.getSettings().getSmtpAdress()));
            msg.setFrom(new InternetAddress("server.manager.test@gmail.com"));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject("Restored password to account:" + usr.getLogin());
 
            // Message body preparation below
            msg.setText("Your login is: "+usr.getLogin()+". \nYour new password is:" + pass);
//            Multipart multipart = new MimeMultipart();
//            BodyPart part = new MimeBodyPart(); 
//            part.setContent( "Your login is: "+usr.getLogin()+". \nYour new password is:" + pass,
//            				 "text/html; charset=utf-8" );
//            multipart.addBodyPart(part);
//            msg.setContent(multipart);
            
            // Send
            Transport.send(msg);
            log.info("message sent to " + usr.getEmail());
        }
        catch (MessagingException mex) {
            log.error(mex);
        }
	}
}
