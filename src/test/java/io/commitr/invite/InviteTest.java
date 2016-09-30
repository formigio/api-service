package io.commitr.invite;

import io.commitr.util.DTOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by peter on 9/24/16.
 */
@RunWith(SpringRunner.class)
@JsonTest
public class InviteTest {

    @Autowired
    JacksonTester<Invite> json;

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\n" +
                "    \"id\": 1,\n" +
                "    \"guid\": \"" + DTOUtils.VALID_UUID_STRING+ "\",\n" +
                "    \"goal\": \"" + DTOUtils.VALID_UUID_STRING+ "\"\n" +
                "}";

        Invite invite = this.json.parseObject(content);

        assertThat(invite.getId()).isNull();
        assertThat(invite.getUuid()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(invite.getGoal()).isEqualTo(DTOUtils.VALID_UUID);

    }

    @Test
    public void testSerialize() throws Exception {
        Invite invite = new Invite();

        invite.setUuid(DTOUtils.VALID_UUID);
        invite.setGoal(DTOUtils.VALID_UUID);

        JsonContent<Invite> content = this.json.write(invite);

        assertThat(content)
                .doesNotHaveJsonPathValue("$.id");
        assertThat(content)
                .hasJsonPathStringValue("$.guid")
                .extractingJsonPathStringValue("$.guid").isEqualTo(DTOUtils.VALID_UUID_STRING);
        assertThat(content)
                .hasJsonPathValue("$.goal")
                .extractingJsonPathStringValue("$.goal").isEqualTo(DTOUtils.VALID_UUID_STRING);

    }
}
