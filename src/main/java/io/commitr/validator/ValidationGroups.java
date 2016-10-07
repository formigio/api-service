package io.commitr.validator;

import javax.validation.groups.Default;

/**
 * Created by Peter Douglas on 10/6/2016.
 */
public final class ValidationGroups {
    public interface Get extends Default {}
    public interface Put extends Default {}
    public interface Post extends Default {}
    public interface Patch extends Default {}
    public interface Delete extends Default {}
}
