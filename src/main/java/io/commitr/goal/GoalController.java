package io.commitr.goal;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/goal")
public class GoalController {
	
	@RequestMapping(method=RequestMethod.POST)
	public Goal setGoal(@RequestBody Goal goal){
		return new Goal();
	}
	
	@RequestMapping(value="/{guid}", method=RequestMethod.GET)
	public Goal getGoal(String guid) {
		return new Goal();
	}
}
