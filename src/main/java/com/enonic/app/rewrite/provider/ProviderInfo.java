package com.enonic.app.rewrite.provider;

public class ProviderInfo
{
    private final String name;

    private final Boolean readOnly;

    private ProviderInfo( final Builder builder )
    {
        name = builder.name;
        readOnly = builder.readOnly;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public Boolean isReadOnly()
    {
        return readOnly;
    }

    public static final class Builder
    {
        private String name;

        private Boolean readOnly;

        private Builder()
        {
        }

        public Builder name( final String name )
        {
            this.name = name;
            return this;
        }

        public Builder readOnly( final Boolean readOnly )
        {
            this.readOnly = readOnly;
            return this;
        }

        public ProviderInfo build()
        {
            return new ProviderInfo( this );
        }
    }

    public String getName()
    {
        return name;
    }
}
