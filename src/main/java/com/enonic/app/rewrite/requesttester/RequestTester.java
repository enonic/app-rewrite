package com.enonic.app.rewrite.requesttester;

import com.enonic.app.rewrite.UpdateRuleParams;

public interface RequestTester
{
    RequestTesterResult testRequest( final String requestURL );

    boolean hasLoops( final UpdateRuleParams params );
}
