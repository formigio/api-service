package io.commitr.goal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.Validator;
import java.util.UUID;

@RestController
@RequestMapping(value="/goal")
public class GoalController {

	@Autowired
	GoalService goalService;
	
	@RequestMapping(method=RequestMethod.POST)
	public Goal setGoal(@RequestBody Goal goal){
		return goalService.createGoal(goal);
	}
	
	@RequestMapping(value="/{uuid}", method=RequestMethod.GET)
	public Goal getGoal(@PathVariable UUID uuid) {
		return goalService.getGoal(uuid);
	}
}