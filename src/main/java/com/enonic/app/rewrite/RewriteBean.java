package com.enonic.app.rewrite;

import com.enonic.app.rewrite.redirect.RedirectType;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteRule;
import com.enonic.app.rewrite.mapping.ErrorMapper;
import com.enonic.app.rewrite.mapping.RequestTesterResultMapper;
import com.enonic.app.rewrite.mapping.RewriteMappingMapper;
import com.enonic.app.rewrite.requesttester.RequestTester;
import com.enonic.app.rewrite.requesttester.RequestTesterResult;
import com.enonic.xp.script.bean.BeanContext;
import com.enonic.xp.script.bean.ScriptBean;

public class RewriteBean
    implements ScriptBean
{
    private RewriteService rewriteService;

    private RequestTester requestTester;

    @Override
    public void initialize( final BeanContext context )
    {
        this.rewriteService = context.getService( RewriteService.class ).get();
        this.requestTester = context.getService( RequestTester.class ).get();
    }

    public Object getRewriteMapping()
    {
        final RewriteMapping rewriteMapping = this.rewriteService.getRewriteMapping();

        return new RewriteMappingMapper( rewriteMapping );
    }

    public Object requestTester( final String requestURI )
    {
        final RequestTesterResult requestTesterResult;
        try
        {
            requestTesterResult = this.requestTester.testRequest( requestURI );
        }
        catch ( Exception e )
        {
            return new ErrorMapper( e );
        }

        return new RequestTesterResultMapper( requestTesterResult );
    }

    public Object store( final StoreRuleParams params )
    {
        System.out.println( params );

        this.rewriteService.store( new RewriteContextKey( params.getContextKey() ), RewriteRule.create().
            from( params.getFrom() ).
            target( params.getTarget() ).
            order( params.getOrder() ).
            type( RedirectType.MOVED_PERMANENTLY ).
            build() );

        return null;
    }


}
