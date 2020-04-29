package com.enonic.app.rewrite.requesttester;

import java.util.List;

import com.google.common.collect.Lists;

public class RequestTesterResult
{

    private final List<RedirectTestResult> matchList;

    private RequestTesterResult( final Builder builder )
    {
        matchList = builder.matchList;
    }

    public List<RedirectTestResult> getMatchList()
    {
        return matchList;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public static final class Builder
    {
        private List<RedirectTestResult> matchList = Lists.newArrayList();

        private Builder()
        {
        }

        public Builder add( final RedirectTestResult result )
        {
            this.matchList.add( result );
            return this;
        }

        public RequestTesterResult build()
        {
            return new RequestTesterResult( this );
        }
    }
}
