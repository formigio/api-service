package io.commitr.commitment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Peter Douglas on 8/24/2016.
 */
@RestController
public class CommitmentController {

    @Autowired
    CommitmentRepository commitmentRepository;

    @RequestMapping(value = "/commitment", method = RequestMethod.GET)
    public List<Commitment> commitment()
    {
        return commitmentRepository.findAll();
    }
}
