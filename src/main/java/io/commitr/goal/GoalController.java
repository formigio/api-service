package io.commitr.goal;

import io.commitr.controller.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.Validator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(value="/goal")
public class GoalController {

	@Autowired
	GoalService goalService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Goal setGoal(@RequestBody Goal goal){
		return goalService.createGoal(goal);
	}

	@GetMapping("/{uuid}")
	@ResponseStatus(HttpStatus.OK)
	public Goal getGoal(@PathVariable UUID uuid) {
		return goalService.getGoal(uuid);
	}

	@GetMapping
	public List<Goal> getGoalByTeam(@RequestParam("team") UUID uuid) {
		List<Goal> goals = goalService.getGoalsByTeam(uuid);

		return goals;
	}
}