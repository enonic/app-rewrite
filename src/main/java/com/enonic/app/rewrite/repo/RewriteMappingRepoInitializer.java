package com.enonic.app.rewrite.repo;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.xp.init.ExternalInitializer;
import com.enonic.xp.repository.CreateRepositoryParams;
import com.enonic.xp.repository.RepositoryId;
import com.enonic.xp.repository.RepositoryService;
import com.enonic.xp.security.RoleKeys;
import com.enonic.xp.security.acl.AccessControlEntry;
import com.enonic.xp.security.acl.AccessControlList;

@Component(immediate = true)
public class RewriteMappingRepoInitializer
    extends ExternalInitializer
{
    public final static RepositoryId REPO_ID = RepositoryId.from( "com.enonic.app.rewrite" );

    private static final AccessControlList DEFAULT_ACL = AccessControlList.create().
        add( AccessControlEntry.create().
            allowAll().
            principal( RoleKeys.ADMIN ).
            build() ).
        build();

    private final Logger LOG = LoggerFactory.getLogger( RewriteMappingRepoInitializer.class );

    private RepositoryService repositoryService;

    private RewriteMappingRepoInitializer( final Builder builder )
    {
        super( builder );
        repositoryService = builder.repositoryService;
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
        repositoryService.createRepository( CreateRepositoryParams.create().
            repositoryId( REPO_ID ).
            rootPermissions( DEFAULT_ACL ).
            build() );
    }

    public static final class Builder
        extends ExternalInitializer.Builder<Builder>
    {
        private RepositoryService repositoryService;

        private Builder()
        {
        }

        public Builder repositoryService( final RepositoryService repositoryService )
        {
            this.repositoryService = repositoryService;
            return this;
        }

        public RewriteMappingRepoInitializer build()
        {
            return new RewriteMappingRepoInitializer( this );
        }
    }
}
