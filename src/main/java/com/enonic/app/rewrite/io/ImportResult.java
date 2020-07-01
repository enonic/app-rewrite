package com.enonic.app.rewrite.io;

import com.enonic.app.rewrite.rewrite.RewriteRules;

public class ImportResult
{
    private final Integer newRules;

    private final Integer failed;

    private final Integer deleted;

    private final Integer updated;

    private ImportResult( final Builder builder )
    {
        newRules = builder.newRules;
        failed = builder.failed;
        deleted = builder.deleted;
        updated = builder.updated;
    }

    public Integer getNewRules()
    {
        return newRules;
    }

    public Integer getFailed()
    {
        return failed;
    }

    public Integer getTotal()
    {
        return newRules + failed;
    }

    public Integer getDeleted()
    {
        return deleted;
    }

    public Integer getUpdated()
    {
        return updated;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public static final class Builder
    {
        private Integer newRules = 0;

        private Integer failed = 0;

        private Integer deleted = 0;

        private Integer updated = 0;

        private Builder()
        {
        }

        public Builder addNew()
        {
            this.newRules++;
            return this;
        }

        public Builder setNew( final Integer newRules )
        {
            this.newRules = newRules;
            return this;
        }

        public Builder addFailed()
        {
            this.failed++;
            return this;
        }

        public Builder addDeleted()
        {
            this.deleted++;
            return this;
        }

        public Builder setDeleted( final Integer deleted )
        {
            this.deleted = deleted;
            return this;
        }


        public Builder addUpdated()
        {
            this.updated++;
            return this;
        }

        public ImportResult build()
        {
            return new ImportResult( this );
        }
    }

    @Override
    public String toString()
    {
        return "ImportResult{" + "newRules=" + newRules + ", failed=" + failed + ", deleted=" + deleted + ", updated=" + updated + '}';
    }
}
