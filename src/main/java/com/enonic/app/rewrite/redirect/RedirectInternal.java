package com.enonic.app.rewrite.redirect;

import com.enonic.app.rewrite.UrlHelper;
import com.enonic.app.rewrite.domain.RewriteContext;

public class RedirectInternal
    implements RedirectTarget
{
    private final String target;

    private RedirectInternal( final String target )
    {
        this.target = target;
    }

    public static RedirectInternal from( final RewriteContext context, final String target )
    {
        final String targetPath = UrlHelper.createUrl( context.getSourceContext(), target );
        return new RedirectInternal( targetPath );
    }

    @Override
    public String getTargetPath()
    {
        return this.target;
    }

    @Override
    public String toString()
    {
        return "RedirectInternal{ target='" + target + "'}";
    }

}
