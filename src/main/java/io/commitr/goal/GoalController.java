package io.commitr.goal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/goal")
public class GoalController {

	@Autowired
	GoalService goalService;
	
	@RequestMapping(method=RequestMethod.POST)
	public Goal setGoal(@RequestBody Goal goal){
		return goalService.createGoal(goal);
	}
	
	@RequestMapping(value="/{guid}", method=RequestMethod.GET)
	public Goal getGoal(String guid) {
		return new Goal();
	}
}
