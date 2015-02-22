package my.manager.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Ayder
 * Class Used to handle all controller exceptions:
 */

@ControllerAdvice
public class ManagerControllerAdvice {
	private static final Logger log = Logger.getLogger(ManagerController.class);
	
	@ExceptionHandler(Exception.class)				//catch all controller exceptions
	public ModelAndView handleExceptions(Exception ex){
		
		log.warn("Controller->handleException() - Caught: " + ex.getClass().getSimpleName());
		
		ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("error");
	    modelAndView.addObject("errorClass", ex.getClass().getSimpleName());
	    modelAndView.addObject("errorMessage", ex.getMessage());
	    modelAndView.addObject("errorMessageCause", ex.getCause());
	    
	    return modelAndView;
	}
}
