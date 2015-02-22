package my.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import my.manager.service.ManagerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * This class schedules regular server updates based on check interval 
 * and is responsible for following check interval change.
 * @author Ayder
 */
@Component()
public class StateUpdaterMain{
	public static Logger log = Logger.getLogger(StateUpdaterMain.class);
	private ServerStateUpdater updater = null;
	private int checkInterval = 5;
	private ScheduledExecutorService scheduler = null;
	private ScheduledFuture<?> handle = null;
	private Runnable runnable = null;
	
	@Autowired
	private ManagerService service;
	/**
	 * This initialization method is called once when beans are instantiated in context 
	 */
	public void init(){
		updater = new ServerStateUpdater(service);
		scheduler = Executors.newScheduledThreadPool(1);
		runnable = new ScheduledCheck();
		handle = scheduler.scheduleAtFixedRate(runnable, 0, checkInterval, TimeUnit.SECONDS);
	}
	/**
	 * This method reschedules ScheduledCheck after it is performed if settings have changed
	 */
	private void updateCheckInterval()
	{
		int newCheckInterval = service.getSettings().getCheckInterval();
		if(newCheckInterval == checkInterval){
			return;		//do nothing if no change
		}
		else{			//reschedule if changed
			checkInterval = newCheckInterval;
			handle.cancel(false);
			handle = scheduler.scheduleAtFixedRate(runnable, checkInterval, checkInterval, TimeUnit.SECONDS);
		}
	}
	/**
	 * Class is used as a thread to launch update of all servers once (it is being scheduled)
	 * and launching check interval once update is done.
	 * @author Ayder
	 *
	 */
	private class ScheduledCheck implements Runnable {
		@Override
		public void run() {
			updater.doUpdate();	//update server states
			updateCheckInterval();	
		}
	}
	
}
