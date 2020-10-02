package com.enonic.app.rewrite;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.enonic.app.rewrite.provider.repo.RewriteMappingRepoInitializer;
import com.enonic.xp.index.IndexService;
import com.enonic.xp.node.NodeService;
import com.enonic.xp.repository.RepositoryService;

@Component(immediate = true)
public class RewriteAppActivator
{

    private RepositoryService repositoryService;

    private IndexService indexService;

    private NodeService nodeService;

    @Activate
    public void activate( final ComponentContext componentContext )
    {
        RewriteMappingRepoInitializer.create().
            repositoryService( repositoryService ).
            nodeService( nodeService ).
            setIndexService( indexService ).
            build().
            initialize();

        componentContext.enableComponent( null );
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
