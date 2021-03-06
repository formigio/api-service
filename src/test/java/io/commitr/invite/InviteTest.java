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
    private JacksonTester<Invite> json;

    private final String INVITER = "inviter";
    private final String INVITEE = "invitee";

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\n" +
                "    \"id\": 1,\n" +
                "    \"uuid\": \"" + DTOUtils.VALID_UUID_STRING+ "\",\n" +
                "    \"entity\": \"" + DTOUtils.VALID_UUID_STRING+ "\",\n" +
                "    \"entityType\": \"goal\",\n" +
                "    \"inviter\": \"" + INVITER + "\",\n" +
                "    \"invitee\": \"" + INVITEE + "\"\n" +
                "}";

        Invite invite = this.json.parseObject(content);

        assertThat(invite.getId()).isNull();
        assertThat(invite.getUuid()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(invite.getEntity()).isEqualTo(DTOUtils.VALID_UUID);
        assertThat(invite.getEntityType()).isEqualTo("goal");
        assertThat(invite.getInviter()).isEqualTo(INVITER);
        assertThat(invite.getInvitee()).isEqualTo(INVITEE);

    }

    @Test
    public void testSerialize() throws Exception {
        Invite invite = new Invite();

        invite.setUuid(DTOUtils.VALID_UUID);
        invite.setEntity(DTOUtils.VALID_UUID);
        invite.setEntityType("goal");
        invite.setInviter(INVITER);
        invite.setInvitee(INVITEE);

        JsonContent<Invite> content = this.json.write(invite);

        assertThat(content)
                .doesNotHaveJsonPathValue("$.id");
        assertThat(content)
                .hasJsonPathStringValue("$.uuid")
                .extractingJsonPathStringValue("$.uuid").isEqualTo(DTOUtils.VALID_UUID_STRING);
        assertThat(content)
                .hasJsonPathValue("$.entity")
                .extractingJsonPathStringValue("$.entity").isEqualTo(DTOUtils.VALID_UUID_STRING);
        assertThat(content)
                .hasJsonPathValue("$.entityType")
                .extractingJsonPathStringValue("$.entityType").isEqualTo("goal");
        assertThat(content)
                .hasJsonPathValue("$.inviter")
                .extractingJsonPathStringValue("$.inviter").isEqualTo(INVITER);
        assertThat(content)
                .hasJsonPathValue("$.invitee")
                .extractingJsonPathStringValue("$.invitee").isEqualTo(INVITEE);

    }
}
