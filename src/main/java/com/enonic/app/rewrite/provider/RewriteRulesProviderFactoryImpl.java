package com.enonic.app.rewrite.provider;

import java.nio.file.Paths;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.common.collect.Lists;

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

    public List<RewriteMappingProvider> getProviders( final RewriteFilterConfig config )
    {
        final List<RewriteMappingProvider> providers = Lists.newArrayList();
        providers.add( initFileProvider( config ) );
        providers.add( initRepoProvider() );
        return providers;
    }

    private RewriteMappingProvider initFileProvider( final RewriteFilterConfig config )
    {
        final String rulePattern = config.ruleFilePattern();
        final HomeDir xpHome = HomeDir.get();
        return RewriteMappingLocalFileProvider.create().
            base( Paths.get( xpHome.toFile().getPath(), "config" ) ).
            ruleFilePattern( rulePattern ).
            build();
    }

    private RewriteMappingProvider initRepoProvider()
    {
        return new RewriteRepoMappingProvider( this.repositoryService, this.indexService, this.nodeService );
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
