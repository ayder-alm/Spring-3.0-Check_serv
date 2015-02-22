package my.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import my.manager.domain.Server;
import my.manager.domain.ServerState;
import my.manager.service.ManagerService;

/**
 * This class is responsible for updating servers' statuses.
 * It provides single update of all active servers' statuses in doUpdate() method
 * @author Ayder
 *
 */
public class ServerStateUpdater {
	public static Logger log = Logger.getLogger(ServerStateUpdater.class);
	private ManagerService service;
	private int timeout;
	/**
	 * Sets service to interact with
	 * @param service
	 */
	public  ServerStateUpdater(ManagerService service)
	{
		this.service = service;
	}
	/**
	 * Provides single update of all servers
	 */	
	public void doUpdate(){
		
		List<Server> servers = service.listServers();  //list of all servers
		if(servers.isEmpty()){
			return;	//if no servers, do nothing - return
		}//else
		
		timeout = service.getSettings().getServerTimeout();
		ServerState state = ServerState.FAIL;

		ExecutorService pool = Executors.newFixedThreadPool(servers.size());   //prepare executors, at least 1 because servers is not empty
		List<Future<ResponseResult>> list = new ArrayList<Future<ResponseResult>>();
		
		for (Server server: servers) {		//if server is active try to check it using UpdaterCallable thread 
			if(server.isActive()){
			      Callable<ResponseResult> callable = new UpdaterCallable(server.getAdress(), server.getUrlPath(),server.getPort(), timeout);
			      Future<ResponseResult> future = pool.submit(callable);
			      list.add(future);
			}
			else
			{
				list.add(null);
			}
		}
		
		for (int i = 0; i < list.size(); i++) {		//write results to database
			Server server = servers.get(i);
			if(server.isActive()){
				try {
					ResponseResult rr = list.get(i).get();
					
					server.setResponse(rr.responseMessage);
					
					if(rr.code == 200)
					{
						state = ServerState.OK;
					}
					else if( (rr.code > 200)&&(rr.code < 500) )
					{
						state = ServerState.WARN;
					}
					else
					{
						state = ServerState.FAIL;
					}
					service.updateServerState(server, state, rr.responseMessage);
				} 
				catch(ExecutionException e){
					log.error("Updater execution caught exception: " + e.getCause());
					service.updateServerState(server, ServerState.FAIL, "exception caught");	//in case of exception, server is updated as FAIL
				}
				catch(InterruptedException e){
					log.error("Updater execution is interrupted: " + e.getCause());		//do not update servers, maybe server is down
					
				}
				catch (Exception e) {
					log.error(e.toString());
					service.updateServerState(server, ServerState.FAIL, "exception caught");	//in case of any other exception, server is updated as FAIL
				}
			}
			else
			{
				service.updateServerState(server,ServerState.FAIL, "activate to check"); //if server is not active - it is updated as FAIL with "activate to check" as response
			}
		}
	}
}
