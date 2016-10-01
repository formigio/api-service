package io.commitr.goal;

import io.commitr.util.DTOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;

/**
 * Created by Peter Douglas on 9/6/2016.
 */
@RunWith(SpringRunner.class)
@JsonTest
public class GoalTest {

    @Autowired
    private JacksonTester<Goal> json;

    @Test
    public void testSerializeWithGuid() throws Exception {
        Goal goal = new Goal();

        goal.setId(1L);
        goal.setUuid(DTOUtils.VALID_UUID);
        goal.setTitle("Goal Test");
        goal.setTeam(DTOUtils.VALID_UUID);

        JsonContent<Goal> content = this.json.write(goal);

        content.assertThat().doesNotHaveJsonPathValue("$.id");
        content.assertThat().hasJsonPathStringValue("$.guid", DTOUtils.VALID_UUID_STRING);
        content.assertThat().hasJsonPathStringValue("$.title", "Goal Test");
        content.assertThat().hasJsonPathStringValue("$.team", DTOUtils.VALID_UUID_STRING);
    }

    @Test
    public void testDeserializeWithGuid() throws Exception {

        String content = "{" +
                "    \"id\":1," +
                "    \"guid\":\"" + DTOUtils.VALID_UUID_STRING + "\"," +
                "    \"title\":\"Test Goal\"," +
                "    \"team\":\"" + DTOUtils.VALID_UUID_STRING + "\"" +
                "}";

        Goal goal = this.json.parseObject(content);

        assertThat(goal.getId()).isNull();
        assertThat(goal.getUuid()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(goal.getTitle()).isEqualTo("Test Goal");
        assertThat(goal.getTeam()).isEqualTo(DTOUtils.VALID_UUID);
    }
}
