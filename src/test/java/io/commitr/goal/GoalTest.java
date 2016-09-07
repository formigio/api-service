package io.commitr.goal;

import io.commitr.util.DTOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
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
        goal.setGuid(DTOUtils.VALID_UUID);
        goal.setTitle("Goal Test");

        assertThat(this.json.write(goal))
                .doesNotHaveJsonPathValue("$.id");

        assertThat(this.json.write(goal))
                .hasJsonPathStringValue("$.guid");
        assertThat(this.json.write(goal))
                .extractingJsonPathStringValue("$.guid").isEqualTo(DTOUtils.VALID_UUID_STRING);

        assertThat(this.json.write(goal))
                .hasJsonPathStringValue("$.title");
        assertThat(this.json.write(goal))
                .extractingJsonPathStringValue("$.title").isEqualTo("Goal Test");
    }

    @Test
    public void testDeserializeWithGuid() throws Exception {

        Goal goal = new Goal();
        goal.setGuid(DTOUtils.VALID_UUID);
        goal.setTitle("Test Goal");

        String content = "{" +
                "    \"id\":1," +
                "    \"guid\":\"" + DTOUtils.VALID_UUID_STRING + "\"," +
                "    \"title\":\"Test Goal\"" +
                "}";

        assertThat(this.json.parse(content))
                .isEqualTo(goal);
        assertThat(this.json.parseObject(content).getId()).isNull();
        assertThat(this.json.parseObject(content).getGuid()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(this.json.parseObject(content).getTitle()).isEqualTo("Test Goal");
    }
}
