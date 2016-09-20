package io.commitr.task;

import io.commitr.util.DTOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by peter on 9/17/16.
 */
@RunWith(SpringRunner.class)
@JsonTest
public class TaskDTOTest {

    TaskDTO dtoNotCompleted = new TaskDTO();

    @Autowired
    JacksonTester<TaskDTO> json;

    @Before
    public void setUp() throws Exception {
        dtoNotCompleted.setUuid(DTOUtils.VALID_UUID);
        dtoNotCompleted.setTitle("Test Task");
        dtoNotCompleted.setGoal(DTOUtils.VALID_UUID);
        dtoNotCompleted.setCompleted(false);
    }

    @Test
    public void testSerialize() throws Exception {
        TaskDTO taskDTO = dtoNotCompleted;

        JsonContent<TaskDTO> dtoJson = this.json.write(taskDTO);

        assertThat(dtoJson)
                .hasJsonPathStringValue("$.guid")
                .extractingJsonPathStringValue("$.guid").isEqualTo(DTOUtils.VALID_UUID_STRING);
        assertThat(dtoJson)
                .hasJsonPathStringValue("$.title");
        assertThat(dtoJson)
                .hasJsonPathValue("$.goal")
                .extractingJsonPathStringValue("$.goal").isEqualTo(DTOUtils.VALID_UUID_STRING);
        assertThat(dtoJson)
                .hasJsonPathBooleanValue("$.completed")
                .extractingJsonPathBooleanValue("$.completed").isFalse();

    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\n" +
                "    \"guid\": \"ab13ca05-6706-45e1-b2aa-2394fc09d3a0\",\n" +
                "    \"title\": \"Task Test\",\n" +
                "    \"goal\": \"ab13ca05-6706-45e1-b2aa-2394fc09d3a0\",\n" +
                "    \"completed\": true\n" +
                "}";

        TaskDTO task = this.json.parseObject(content);

        assertThat(task.getUuid()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(task.getTitle()).isEqualTo("Task Test");
        assertThat(task.getGoal()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(task.getCompleted()).isTrue();
    }
}
