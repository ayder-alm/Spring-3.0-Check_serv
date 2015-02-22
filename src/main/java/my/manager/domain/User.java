package my.manager.domain;

import java.sql.Timestamp;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * This class implements User entity
 * written in POJO style.
 * JSR 303 constraints have been applied using annotations to provide validation
 * toString was used for debugging.
 * @author 
 */
public class User {
	private int id;
	
	@NotEmpty(message = "{NotEmpty.user.name}")
	@Size(min = 1, max = 128, message = "{Size.user.name}")
	@Pattern(regexp="([A-Z][a-z]*([a-z]*\\-{0,1}[a-z]+)* [A-Z][a-z]*([a-z]*\\-{0,1}[a-z]+)*)"
				 + "|([А-Я][а-я]*([а-я]*\\-{0,1}[а-я]+)* [А-Я][а-я]*([а-я]*\\-{0,1}[а-я]+)*)",
				 message = "{Pattern.user.name}")
	private String name;
	
	@NotEmpty(message = "{NotEmpty.user.login}")
	@Size(min = 1, max = 16, message = "{Size.user.login}")
	@Pattern(regexp="[a-zA-Z]{1,16}",
			 message = "{Pattern.user.login}")
	private String login;
	
	@NotEmpty(message = "{NotEmpty.user.password}")
	@Size(min = 6, max = 16, message = "{Size.user.password}")
	private String password;
	
	@NotEmpty(message = "{NotEmpty.user.email}")
	@Size(min = 1, max = 128, message = "{Size.user.email}")
	@Pattern(regexp="([a-zA-Z0-9\\-_]+(\\.[a-zA-Z0-9\\-_]+)*)@([a-zA-Z0-9\\-_]+(\\.[a-zA-Z0-9\\-_]+)*)\\.([a-zA-Z]{2,6})",
			 message = "{Pattern.user.email}")
	private String email;
	
	private Timestamp creationTime;
	private Timestamp lastLoginTime;
	private boolean active;
	private boolean admin;
	/**
	 * Default constructor
	 */
	public User()
	{
		id = 0;
		name = "";
		login = "";
		password = "";
		email = "";
		creationTime = new Timestamp(1000);
		lastLoginTime  = new Timestamp(1000);
		active = true;
		admin = false;
	}
	/**
	 * This constructor is used in ServerManager tests
	 */
	public User(int id,String name, String login, String password, String email, 
				Timestamp lastLoginTime, boolean active, boolean admin )
	{
		this.id = id;
		this.name = name;
		this.login = login;
		this.password = password;
		this.email = email;
		this.creationTime = new Timestamp(1000);
		this.lastLoginTime  = lastLoginTime;
		this.active = active;
		this.admin = admin;
	}
	
	@Override
	public String toString(){
		String s = "id:" + id + " name:" + name +" login:" + login + " password:" + password + "email:" + email + " creationTime:" + creationTime
					+" lastLoginTime:" + lastLoginTime +" active:" + active +" admin:" + admin;
		return s;
	}
	
	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getLogin() {
		return this.login;
	}

	public String getPassword() {
		return this.password;
	}
	
	public String getEmail() {
		return this.email;
	}
		
	public Timestamp getCreationTime() {
		return this.creationTime;
	}
	
	public Timestamp getLastLoginTime() {
		return this.lastLoginTime;
	}
	
	public boolean isActive() {
		return this.active;
	}
	
	public boolean isAdmin() {
		return this.admin;
	}
	
	public void setId(int id)	{
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String pass) {
		this.password = pass;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCreationTime(Timestamp time) {
		this.creationTime = time;
	}
	
	public void setLastLoginTime(Timestamp time) {
		this.lastLoginTime = time;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setAdmin(boolean state) {
		this.admin = state;
	}

}

