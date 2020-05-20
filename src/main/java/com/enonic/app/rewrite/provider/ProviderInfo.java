package com.enonic.app.rewrite.provider;

public class ProviderInfo
{
    private final String name;

    private ProviderInfo( final Builder builder )
    {
        name = builder.name;
    }

    public static Builder create()
    {
        return new Builder();
    }


    public static final class Builder
    {
        private String name;

        private Builder()
        {
        }

        public Builder name( final String name )
        {
            this.name = name;
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
