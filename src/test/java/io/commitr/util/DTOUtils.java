package io.commitr.util;

import io.commitr.goal.Goal;
import io.commitr.invite.Invite;
import io.commitr.task.Task;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

/**
 * Created by Peter Douglas on 9/6/2016.
 */
public class DTOUtils {

    public static UUID VALID_UUID = UUID.fromString("ab13ca05-6706-45e1-b2aa-2394fc09d3a0");
    public static String VALID_UUID_STRING = "ab13ca05-6706-45e1-b2aa-2394fc09d3a0";

    public static UUID NON_VALID_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static String NON_VALID_UUID_STRING = "00000000-0000-0000-0000-000000000000";

    public static Goal createGoal(UUID uuid, String title) {
        Goal goal = new Goal();
        goal.setUuid(uuid);
        goal.setTitle(title);

        return goal;
    }

    public static Task createTask(UUID uuid, String title, UUID goal, Boolean completed) {
        Task task = new Task();
        task.setUuid(uuid);
        task.setTitle(title);
        task.setGoal(goal);
        task.setCompleted(completed);

        return task;
    }

    @Data
    @AllArgsConstructor
    public static class TaskDTO {
        private String uuid;
        private String title;
        private String goal;
        private Boolean completed;
    }

    public static Invite createInvite(UUID uuid, UUID goal) {
        Invite invite = new Invite();

        invite.setUuid(uuid);
        invite.setGoal(goal);

        return invite;
    }
}
