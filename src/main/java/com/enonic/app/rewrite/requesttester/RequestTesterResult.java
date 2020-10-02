package com.enonic.app.rewrite.requesttester;

import java.util.ArrayList;
import java.util.List;

public class RequestTesterResult
{
    public enum TestResultState
    {
        OK, LOOP, ERROR
    }

    public TestResultState getResultState()
    {
        return resultState;
    }

    private final List<RedirectTestResult> matchList;

    private final TestResultState resultState;

    private RequestTesterResult( final Builder builder )
    {
        matchList = builder.matchList;
        resultState = builder.resultState;
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
        private List<RedirectTestResult> matchList = new ArrayList<>();

        private TestResultState resultState = TestResultState.OK;

        private Builder()
        {
        }

        public Builder loop()
        {
            this.resultState = TestResultState.LOOP;
            return this;
        }

        public Builder error()
        {
            this.resultState = TestResultState.ERROR;
            return this;
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
