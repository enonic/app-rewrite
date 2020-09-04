package com.enonic.app.rewrite.format;

import com.enonic.app.rewrite.domain.RewriteRules;

public class SourceReadResult
{
    private final Integer ok;

    private final Integer failed;

    private final Integer unsupported;

    private final RewriteRules rules;

    private SourceReadResult( final SourceReadResult.Builder builder )
    {
        ok = builder.ok;
        failed = builder.failed;
        rules = builder.rules;
        unsupported = builder.unsupported;
    }

    public Integer getOk()
    {
        return ok;
    }

    public Integer getFailed()
    {
        return failed;
    }

    public Integer getTotal()
    {
        return ok + failed;
    }

    public Integer getUnsupported()
    {
        return unsupported;
    }

    public RewriteRules getRules()
    {
        return rules;
    }

    public static SourceReadResult.Builder create()
    {
        return new SourceReadResult.Builder();
    }

    public static final class Builder
    {
        private Integer ok = 0;

        private Integer failed = 0;

        private Integer unsupported = 0;

        private RewriteRules rules;

        private Builder()
        {
        }

        public SourceReadResult.Builder addNew()
        {
            this.ok++;
            return this;
        }

        public SourceReadResult.Builder addFailed()
        {
            this.failed++;
            return this;
        }

        public SourceReadResult.Builder addUnsupported()
        {
            this.unsupported++;
            return this;
        }

        public SourceReadResult.Builder rules( final RewriteRules rules )
        {
            this.rules = rules;
            return this;
        }

        public SourceReadResult build()
        {
            return new SourceReadResult( this );
        }
    }
}