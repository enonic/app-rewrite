package com.enonic.app.rewrite.rewrite;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class RewriteMappings
    implements Iterable<RewriteMapping>
{
    private List<RewriteMapping> rewriteMappingList;


    public static Builder create()
    {
        return new Builder();
    }

    public RewriteMappings( final Builder builder )
    {
        this.rewriteMappingList = builder.rewriteMappings;
    }

    public RewriteMapping getRewriteMapping( final RewriteContextKey contextKey )
    {
        for ( final RewriteMapping mapping : rewriteMappingList )
        {
            if ( mapping.getContextKey().equals( contextKey ) )
            {
                return mapping;
            }
        }

        return null;
    }

    @Override
    public Iterator<RewriteMapping> iterator()
    {
        return this.rewriteMappingList.iterator();
    }

    public int size()
    {
        return rewriteMappingList.size();
    }

    public List<RewriteMapping> getRewriteMappings()
    {
        return rewriteMappingList;
    }

    public static final class Builder
    {
        private List<RewriteMapping> rewriteMappings = Lists.newArrayList();

        private Builder()
        {
        }

        public Builder add( final RewriteMapping rewriteMapping )
        {
            rewriteMappings.add( rewriteMapping );
            return this;
        }

        public RewriteMappings build()
        {
            return new RewriteMappings( this );
        }
    }
}