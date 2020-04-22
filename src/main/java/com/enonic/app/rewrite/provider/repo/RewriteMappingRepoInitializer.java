package com.enonic.app.rewrite.provider.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.xp.branch.Branch;
import com.enonic.xp.context.ContextAccessor;
import com.enonic.xp.context.ContextBuilder;
import com.enonic.xp.init.ExternalInitializer;
import com.enonic.xp.node.CreateNodeParams;
import com.enonic.xp.node.NodePath;
import com.enonic.xp.node.NodeService;
import com.enonic.xp.repository.CreateRepositoryParams;
import com.enonic.xp.repository.Repository;
import com.enonic.xp.repository.RepositoryId;
import com.enonic.xp.repository.RepositoryService;
import com.enonic.xp.security.RoleKeys;
import com.enonic.xp.security.acl.AccessControlEntry;
import com.enonic.xp.security.acl.AccessControlList;

public class RewriteMappingRepoInitializer
    extends ExternalInitializer
{
    public final static RepositoryId REPO_ID = RepositoryId.from( "com.enonic.app.rewrite" );

    public final static Branch BRANCH = Branch.from( "master" );

    private static final AccessControlList DEFAULT_ACL = AccessControlList.create().
        add( AccessControlEntry.create().
            allowAll().
            principal( RoleKeys.ADMIN ).
            build() ).
        build();

    private final Logger LOG = LoggerFactory.getLogger( RewriteMappingRepoInitializer.class );

    private RepositoryService repositoryService;

    private NodeService nodeService;

    private RewriteMappingRepoInitializer( final Builder builder )
    {
        super( builder );
        this.repositoryService = builder.repositoryService;
        this.nodeService = builder.nodeService;
    }

    public static Builder create()
    {
        return new Builder();
    }


    @Override
    protected void doInitialize()
    {
        doCreateRepo();
    }

    @Override
    protected String getInitializationSubject()
    {
        return REPO_ID.toString();
    }


    @Override
    protected boolean isInitialized()
    {
        return this.repositoryService.isInitialized( REPO_ID );
    }

    private void doCreateRepo()
    {
        LOG.info( "Creating rewrite-repository" );
        final Repository repository = repositoryService.createRepository( CreateRepositoryParams.create().
            repositoryId( REPO_ID ).
            rootPermissions( DEFAULT_ACL ).
            build() );

        ContextBuilder.from( ContextAccessor.current() ).
            repositoryId( repository.getId() ).
            build().callWith( () -> nodeService.create( CreateNodeParams.create().
            parent( NodePath.ROOT ).
            name( RewriteRepoMappingProvider.MAPPING_ROOT_NODE.getName() ).
            build() ) );
    }

    public static final class Builder
        extends ExternalInitializer.Builder<Builder>
    {
        private RepositoryService repositoryService;

        private NodeService nodeService;

        private Builder()
        {
        }

        public Builder repositoryService( final RepositoryService repositoryService )
        {
            this.repositoryService = repositoryService;
            return this;
        }

        public Builder nodeService( final NodeService nodeService )
        {
            this.nodeService = nodeService;
            return this;
        }

        public RewriteMappingRepoInitializer build()
        {
            return new RewriteMappingRepoInitializer( this );
        }
    }
}
