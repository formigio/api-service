package io.commitr.team;

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

/**
 * Created by peter on 10/1/16.
 */
@RunWith(SpringRunner.class)
@JsonTest
public class TeamTest {

    @Autowired
    JacksonTester<Team> json;

    @Test
    public void testSerialize() throws Exception {
        Team team = new Team();

        team.setId(1L);
        team.setName("Team Test");
        team.setUuid(DTOUtils.VALID_UUID);
        team.setIdentity(DTOUtils.VALID_UUID);

        JsonContent<Team> content = this.json.write(team);

        content.assertThat().doesNotHaveJsonPathValue("$.id");
        content.assertThat().hasJsonPathStringValue("$.name", "Team Test");
        content.assertThat().hasJsonPathStringValue("$.uuid", DTOUtils.VALID_UUID_STRING);
        content.assertThat().hasJsonPathStringValue("$.identity", DTOUtils.VALID_UUID_STRING);

    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{" +
                "    \"id\":1," +
                "    \"name\":\"Team Test\"," +
                "    \"uuid\":\"" + DTOUtils.VALID_UUID_STRING + "\"," +
                "    \"identity\":\"" + DTOUtils.VALID_UUID_STRING + "\"" +
                "}";

        Team team = this.json.parseObject(content);

        assertThat(team.getId()).isNull();
        assertThat(team.getName()).isNotNull();
        assertThat(team.getName()).isEqualTo("Team Test");
        assertThat(team.getUuid()).isNotNull();
        assertThat(team.getUuid()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(team.getIdentity()).isNotNull();
        assertThat(team.getIdentity()).isEqualTo(DTOUtils.VALID_UUID);

    }
}
