package org.coner.api.response;

import com.google.common.collect.ImmutableList;
import java.util.List;

public class ErrorsResponse {

    public ErrorsResponse() {
        // no-op
    }

    public ErrorsResponse(String... errors) {
        this.errors = ImmutableList.copyOf(errors);
    }

    private List<String> errors;

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = ImmutableList.copyOf(errors);
    }
}
