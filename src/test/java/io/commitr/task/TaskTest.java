package io.commitr.task;

import io.commitr.util.DTOUtils;
import org.hamcrest.beans.SamePropertyValuesAs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by peter on 9/15/16.
 */
@RunWith(SpringRunner.class)
@JsonTest
public class TaskTest {

    @Autowired
    JacksonTester<Task> json;

    @Test
    public void testSerialize() throws Exception {

        Task task = new Task();

        task.setId(1L);
        task.setUuid(DTOUtils.VALID_UUID);
        task.setTitle("Task Test");
        task.setGoal(DTOUtils.VALID_UUID);
        task.setCompleted(true);

        JsonContent<Task> taskJson = this.json.write(task);

        assertThat(taskJson)
                .doesNotHaveJsonPathValue("$.id");
        assertThat(taskJson)
                .hasJsonPathStringValue("$.uuid")
                .extractingJsonPathStringValue("$.uuid").isEqualTo(DTOUtils.VALID_UUID_STRING);
        assertThat(taskJson)
                .hasJsonPathStringValue("$.title");
        assertThat(taskJson)
                .hasJsonPathValue("$.goal")
                .extractingJsonPathStringValue("$.goal").isEqualTo(DTOUtils.VALID_UUID_STRING);
        assertThat(taskJson)
                .hasJsonPathBooleanValue("$.completed")
                .extractingJsonPathBooleanValue("$.completed").isTrue();
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{" +
                "    \"id\": 1," +
                "    \"title\": \"Task Test\"," +
                "    \"goal\": \"ab13ca05-6706-45e1-b2aa-2394fc09d3a0\"," +
                "    \"completed\": true," +
                "    \"uuid\": \"ab13ca05-6706-45e1-b2aa-2394fc09d3a0\"" +
                "}";

        Task task = this.json.parseObject(content);

        assertThat(task.getId()).isNull();
        assertThat(task.getUuid()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(task.getTitle()).isEqualTo("Task Test");
        assertThat(task.getGoal()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(task.getCompleted()).isTrue();
    }

}
