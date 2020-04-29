package com.enonic.app.rewrite.redirect;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import com.enonic.app.rewrite.rewrite.RewriteContext;

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
        final Path targetPath = Paths.get( context.getSourceContext(), target );
        return new RedirectInternal( targetPath.toString() );
    }

    @Override
    public String getTargetPath()
    {
        return this.target;
    }

    @Override
    public String toString()
    {
        return "RedirectInternal{" + "target='" + target + '\'' + '}';
    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        final RedirectInternal that = (RedirectInternal) o;
        return Objects.equals( target, that.target );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( target );
    }
}
