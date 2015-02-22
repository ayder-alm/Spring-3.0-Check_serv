package my.manager.domain;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * This class implements sysem Settings entity
 * written in POJO style.
 */
public class Settings {
	
	@NotNull(message = "{NotEmpty.refreshinterval}")
	@DecimalMin(value = "1", message = "{Decimal.refreshinterval}")
	@DecimalMax(value = "36000", message = "{Decimal.refreshinterval}")	//10 hours
	private Integer refreshInterval;
	
	@NotNull(message = "{NotEmpty.checkinterval}")
	@DecimalMin(value = "1", message = "{Decimal.checkinterval}")
	@DecimalMax(value = "36000", message = "{Decimal.checkinterval}")	//10 hours
	private Integer checkInterval;
	
	@NotNull(message = "{NotEmpty.timeout}")
	@DecimalMin(value = "1", message = "{Decimal.timeout}")
	@DecimalMax(value = "300", message = "{Decimal.timeout}")			//5 min
	private Integer serverTimeout;
	
	@NotNull(message = "{NotEmpty.smtpport}")
	@DecimalMin(value = "1", message = "{Decimal.smtpport}")
	@DecimalMax(value = "65535", message = "{Decimal.smtpport}")		//max port number
	private Integer smtpPort;
	
	@NotEmpty(message = "{NotEmpty.smtpadress}")
	@Size(min = 1, max = 128, message = "{Size.smtpadress}")
	@Pattern(regexp = "(([a-zA-Z0-9\\-_]+(\\.[a-zA-Z0-9\\-_]+)*)@([a-zA-Z0-9\\-_]+(\\.[a-zA-Z0-9\\-_]+)*)\\.([a-zA-Z]{2,6}))|" 
					+ "[a-zA-Z ]*<(([a-zA-Z0-9\\-_]+(\\.[a-zA-Z0-9\\-_]+)*)@([a-zA-Z0-9\\-_]+(\\.[a-zA-Z0-9\\-_]+)*)\\.([a-zA-Z]{2,6}))>")
	private String smtpAdress;
	
	public Integer getRefreshInterval() {
		return refreshInterval;
	}
	public void setRefreshInterval(Integer refreshInterval) {
		this.refreshInterval = refreshInterval;
	}
	public Integer getCheckInterval() {
		return checkInterval;
	}
	public void setCheckInterval(Integer checkInterval) {
		this.checkInterval = checkInterval;
	}
	public Integer getServerTimeout() {
		return serverTimeout;
	}
	public void setServerTimeout(Integer serverTimeout) {
		this.serverTimeout = serverTimeout;
	}
	public Integer getSmtpPort() {
		return smtpPort;
	}
	public void setSmtpPort(Integer smtpPort) {
		this.smtpPort = smtpPort;
	}
	public String getSmtpAdress() {
		return smtpAdress;
	}
	public void setSmtpAdress(String smtpAdress) {
		this.smtpAdress = smtpAdress;
	}
}

