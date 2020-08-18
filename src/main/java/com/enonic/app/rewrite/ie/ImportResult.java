package com.enonic.app.rewrite.ie;

public class ImportResult
{
    private boolean failed;

    private final Integer newRules;

    private final Integer errors;

    private final Integer deleted;

    private final Integer updated;

    private final Integer unsupported;

    private ImportResult( final Builder builder )
    {
        this.failed = false;
        this.newRules = builder.newRules;
        this.errors = builder.errors;
        this.deleted = builder.deleted;
        this.updated = builder.updated;
        this.unsupported = builder.unsupported;
    }

    private ImportResult( final Builder builder, final boolean failed )
    {
        this.newRules = builder.newRules;
        this.errors = builder.errors;
        this.deleted = builder.deleted;
        this.updated = builder.updated;
        this.unsupported = builder.unsupported;
        this.failed = failed;
    }

    public boolean isFailed()
    {
        return failed;
    }

    public Integer getNewRules()
    {
        return newRules;
    }

    public Integer getErrors()
    {
        return errors;
    }

    public Integer getTotal()
    {
        return newRules + errors;
    }

    public Integer getDeleted()
    {
        return deleted;
    }

    public Integer getUpdated()
    {
        return updated;
    }

    public Integer getUnsupported()
    {
        return unsupported;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public static ImportResult failed()
    {
        return new ImportResult( create(), true );
    }

    public static final class Builder
    {
        private Integer newRules = 0;

        private Integer errors = 0;

        private Integer deleted = 0;

        private Integer updated = 0;

        private Integer unsupported = 0;

        private Builder()
        {
        }

        public Builder setNew( final Integer newRules )
        {
            this.newRules = newRules;
            return this;
        }

        public Builder addFailed()
        {
            this.errors++;
            return this;
        }

        public Builder setDeleted( final Integer deleted )
        {
            this.deleted = deleted;
            return this;
        }

        public Builder setUnsupported( final Integer unsupported )
        {
            this.unsupported = unsupported;
            return this;
        }

        public Builder setErrors( final Integer errors )
        {
            this.errors = errors;
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
        return "ImportResult{" + "newRules=" + newRules + ", failed=" + errors + ", deleted=" + deleted + ", updated=" + updated +
            ", unsupported=" + unsupported + '}';
    }
}
