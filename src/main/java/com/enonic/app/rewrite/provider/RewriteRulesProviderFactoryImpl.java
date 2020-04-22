package com.enonic.app.rewrite.provider;

import java.nio.file.Paths;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.enonic.app.rewrite.filter.RewriteFilterConfig;
import com.enonic.app.rewrite.provider.file.RewriteMappingLocalFileProvider;
import com.enonic.app.rewrite.provider.repo.RewriteRepoMappingProvider;
import com.enonic.xp.home.HomeDir;
import com.enonic.xp.index.IndexService;
import com.enonic.xp.node.NodeService;
import com.enonic.xp.repository.RepositoryService;

@Component(immediate = true)
public class RewriteRulesProviderFactoryImpl
    implements RewriteRulesProviderFactory
{
    private RepositoryService repositoryService;

    private IndexService indexService;

    private NodeService nodeService;

    public RewriteMappingProvider get( final RewriteFilterConfig config )
    {

        if ( config.provider().equals( "file" ) )
        {
            final String rulePattern = config.ruleFilePattern();
            final HomeDir xpHome = HomeDir.get();
            return RewriteMappingLocalFileProvider.create().
                base( Paths.get( xpHome.toFile().getPath(), "config" ) ).
                ruleFilePattern( rulePattern ).
                build();
        }

        if ( config.provider().equals( "repo" ) )
        {
            return new RewriteRepoMappingProvider( this.repositoryService, this.indexService, this.nodeService );
        }

        throw new IllegalArgumentException( "Unknown provider: [" + config.provider() + "]" );
    }

    @Reference
    public void setRepositoryService( final RepositoryService repositoryService )
    {
        this.repositoryService = repositoryService;
    }

    @Reference
    public void setIndexService( final IndexService indexService )
    {
        this.indexService = indexService;
    }

    @Reference
    public void setNodeService( final NodeService nodeService )
    {
        this.nodeService = nodeService;
    }
}
