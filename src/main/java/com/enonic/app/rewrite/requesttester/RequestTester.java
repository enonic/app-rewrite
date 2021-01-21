package com.enonic.app.rewrite.requesttester;

import com.enonic.app.rewrite.RequestTesterParams;
import com.enonic.app.rewrite.UpdateRuleParams;

public interface RequestTester
{
    RequestTesterResult testRequest( final RequestTesterParams params );

    boolean hasLoops( final UpdateRuleParams params );
}
