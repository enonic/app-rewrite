package com.enonic.app.rewrite.filter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.enonic.xp.config.ConfigBuilder;
import com.enonic.xp.config.ConfigInterpolator;
import com.enonic.xp.config.Configuration;

@Component(immediate = true, configurationPid = "com.enonic.app.rewrite")
public class RewriteFilterConfigImpl
    implements RewriteFilterConfig
{
    private Configuration config;

    @Override
    public boolean enabled()
    {
        return Boolean.valueOf( this.config.get( "enabled" ) );
    }

    public List<String> excludePatterns()
    {
        return doGetCommaSeparatedPropAsList( "excludePatterns" );
    }

    public List<String> includePatterns()
    {
        return doGetCommaSeparatedPropAsList( "includePatterns" );
    }

    private List<String> doGetCommaSeparatedPropAsList( final String propertyName )
    {
        return Stream.of( this.config.get( propertyName ).split( "," ) ).collect( Collectors.toList() );
    }

    @Override
    public String provider()
    {
        return this.config.get( "provider" );
    }

    @Override
    public String ruleFilePattern()
    {
        return this.config.get( "ruleFilePattern" );
    }

    @Activate
    public void activate( final Map<String, String> map )
    {

        this.config = ConfigBuilder.create().
            load( getClass(), "default.properties" ).
            addAll( map ).
            build();

        this.config = new ConfigInterpolator().interpolate( this.config );
    }
}