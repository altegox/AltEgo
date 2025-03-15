package org.altegox.api;

import org.altegox.framework.model.LangModel;
import org.altegox.framework.entity.request.DefaultRequest;

public class OpenaiModel extends LangModel {

    private final DefaultRequest requestParam;

    private OpenaiModel(Builder builder) {
        super(builder);
        this.requestParam = builder.requestParam;
    }

    public DefaultRequest getRequestParam() {
        return requestParam;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends LangModel.Builder<Builder> {
        private DefaultRequest requestParam;

        public Builder requestParam(DefaultRequest requestParam) {
            this.requestParam = requestParam;
            return this;
        }

        @Override
        public OpenaiModel build() {
            return new OpenaiModel(this);
        }
    }
}
