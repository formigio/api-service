package io.commitr.util;

import io.commitr.goal.Goal;

import java.util.UUID;

/**
 * Created by Peter Douglas on 9/6/2016.
 */
public class DTOUtils {
    public static UUID VALID_UUID = UUID.fromString("ab13ca05-6706-45e1-b2aa-2394fc09d3a0");
    public static String VALID_UUID_STRING = "ab13ca05-6706-45e1-b2aa-2394fc09d3a0";
    public static Goal createGoal(UUID guid, String title) {
        Goal goal = new Goal();
        goal.setGuid(guid);
        goal.setTitle(title);

        return goal;
    }
}
