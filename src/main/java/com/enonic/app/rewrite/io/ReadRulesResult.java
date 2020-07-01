package com.enonic.app.rewrite.io;

import com.enonic.app.rewrite.rewrite.RewriteRules;

public class ReadRulesResult
{
    private final Integer ok;

    private final Integer failed;

    private final RewriteRules rules;

    private ReadRulesResult( final ReadRulesResult.Builder builder )
    {
        ok = builder.ok;
        failed = builder.failed;
        rules = builder.rules;
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

    public RewriteRules getRules()
    {
        return rules;
    }

    public static ReadRulesResult.Builder create()
    {
        return new ReadRulesResult.Builder();
    }

    public static final class Builder
    {
        private Integer ok = 0;

        private Integer failed = 0;

        private RewriteRules rules;

        private Builder()
        {
        }

        public ReadRulesResult.Builder addNew()
        {
            this.ok++;
            return this;
        }

        public ReadRulesResult.Builder addFailed()
        {
            this.failed++;
            return this;
        }

        public ReadRulesResult.Builder rules( final RewriteRules rules )
        {
            this.rules = rules;
            return this;
        }

        public ReadRulesResult build()
        {
            return new ReadRulesResult( this );
        }
    }
}