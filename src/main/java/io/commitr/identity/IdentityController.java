package io.commitr.identity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by peterfdouglas on 10/19/2016.
 */
@RestController
@RequestMapping("/me")
public class IdentityController {

    @Autowired
    IdentityService identityService;

    @GetMapping
    public Identity getIdentity() {
        return identityService.getIdentity();
    }
}
