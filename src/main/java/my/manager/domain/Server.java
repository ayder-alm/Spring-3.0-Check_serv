package my.manager.domain;

import java.sql.Timestamp;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * This class implements Server entity
 * written in POJO style.
 * JSR 303 constraints have been applied using annotations to provide validation
 * toString was used for debugging.
 * @author Ayder 
 */
public class Server {
	private int id;
	
	@NotEmpty(message = "{NotEmpty.server.name}")
	@Size(min = 1, max = 128, message = "{Size.server.name}")
	@Pattern(regexp = "((?=^.{1,128}$)(^(?:(?!\\d+\\.)[a-zA-Z0-9_\\-]{1,63}\\.?)+(?:[a-zA-Z]{2,})$))",
			 message = "{Pattern.server.name}")
	private String name;
	
	@NotEmpty(message = "{NotEmpty.server.adress}")
	@Size(min = 1, max = 128, message = "{Size.server.adress}")
	@Pattern(regexp = "((?=^.{1,128}$)(^(?:(?!\\d+\\.)[a-zA-Z0-9_\\-]{1,63}\\.?)+(?:[a-zA-Z]{2,})$))|"	//server name or IP
					+ "(\\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." 
			        + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." 
					+ "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
			        + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b)",
					 message = "{Pattern.server.adress}")
	private String adress;
	
	@NotNull(message = "{NotEmpty.server.port}")
	@DecimalMin(value = "1", message = "{Decimal.server.port}")
	@DecimalMax(value = "65535", message = "{Decimal.server.port}")
	private Integer port;
	
	@NotEmpty(message = "{NotEmpty.server.urlPath}")
	@Size(min = 1, max = 128, message = "{Size.server.urlPath}")
	@Pattern(regexp = "([/?].*)?", message = "{Pattern.server.urlPath}")
	private String urlPath;
	
	private ServerState state;
	private String response;
	private Timestamp creationTime;
	private Timestamp lastCheckTime;
	private boolean active = true;
	
	public Server() {
		id = 0;
		name = "";
		adress = "";
		port = 80;
		urlPath = "";
		state = ServerState.FAIL;
		response = "";
		creationTime = new Timestamp(1000);
		lastCheckTime = new Timestamp(1000);
		active = true;
	}
	
	public Server(int id, String name, String adress, int port, String url, ServerState state, String response, boolean active) {
		this.id = id;
		this.name = name;
		this.adress = adress;
		this.port = port;
		this.urlPath = url;
		this.state = state;
		this.response = response;
		this.creationTime = new Timestamp(1000);
		this.lastCheckTime = new Timestamp(1000);
		this.active = active;
	}
	
	@Override
	public String toString(){
		String s = "id:" + id + " name:" + name + " adress:" + adress + " port:" + port + " urlPath:" + urlPath
					+" state:" + state +" response:" + response +" creationTime:" + creationTime +" lastCheckTime:" + lastCheckTime +" active:" + active;
		return s;
	}
	
	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getAdress() {
		return this.adress;
	}
	
	public Integer getPort() {
		return this.port;
	}
	
	public String getUrlPath() {
		return this.urlPath;
	}

	public ServerState getState() {
		return this.state;
	}
	
	public String getResponse() {
		return this.response;
	}

	public Timestamp getCreationTime() {
		return this.creationTime;
	}
	
	public Timestamp getLastCheckTime() {
		return this.lastCheckTime;
	}
	
	public boolean isActive() {
		return this.active;
	}
	
	public void setId(int id)	{
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setAdress(String adress) {
		this.adress = adress;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public void setUrlPath(String url) {
		this.urlPath = url;
	}

	public void setState(ServerState state) {
		this.state = state;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public void setCreationTime(Timestamp time) {
		this.creationTime = time;
	}
	
	public void setLastCheckTime(Timestamp time) {
		this.lastCheckTime = time;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
}

