package com.enonic.app.rewrite.provider.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.enonic.app.rewrite.CreateRuleParams;
import com.enonic.app.rewrite.DeleteRuleParams;
import com.enonic.app.rewrite.EditRuleParams;
import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.domain.RewriteRule;
import com.enonic.app.rewrite.domain.RewriteRules;
import com.enonic.app.rewrite.provider.RewriteContextExistsException;
import com.enonic.app.rewrite.provider.RewriteContextNotFoundException;
import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.redirect.RedirectType;
import com.enonic.xp.context.Context;
import com.enonic.xp.context.ContextAccessor;
import com.enonic.xp.context.ContextBuilder;
import com.enonic.xp.data.PropertyTree;
import com.enonic.xp.node.CreateNodeParams;
import com.enonic.xp.node.Node;
import com.enonic.xp.node.NodeIds;
import com.enonic.xp.node.NodePath;
import com.enonic.xp.node.NodeService;
import com.enonic.xp.node.RefreshMode;
import com.enonic.xp.node.UpdateNodeParams;
import com.enonic.xp.security.RoleKeys;
import com.enonic.xp.security.User;
import com.enonic.xp.security.auth.AuthenticationInfo;

public class RewriteRepoMappingProvider
    implements RewriteMappingProvider
{
    public final static NodePath MAPPING_ROOT_NODE = NodePath.create( "/vhosts" ).build();

    private final NodeService nodeService;

    public RewriteRepoMappingProvider( final NodeService nodeService )
    {
        this.nodeService = nodeService;
    }

    @Override
    public boolean providesForContext( final RewriteContextKey contextKey )
    {
        return setRepoContext().callWith( () -> doGetContextNode( contextKey ) != null );
    }

    @Override
    public RewriteMapping getRewriteMapping( final RewriteContextKey contextKey )
    {
        return setRepoContext().callWith( () -> doGetRewriteMapping( contextKey ) );
    }

    private RewriteMapping doGetRewriteMapping( final RewriteContextKey contextKey )
    {
        final Node node = doGetContextNode( contextKey );
        if ( node == null )
        {
            return null;
        }
        return RewriteMappingSerializer.fromNode( node );
    }

    @Override
    public boolean readOnly()
    {
        return false;
    }

    @Override
    public String name()
    {
        return "Repository";
    }

    @Override
    public void store( final RewriteMapping rewriteMapping )
    {
        final NodePath mappingNodePath = createContextNodePath( rewriteMapping.getContextKey() );

        final Context context = setRepoContext();

        context.runWith( () -> doCreateOrUpdate( mappingNodePath, rewriteMapping ) );
    }

    @Override
    public void create( final RewriteContextKey rewriteContextKey )
    {
        setRepoContext().runWith( () -> doCreate( rewriteContextKey ) );
    }

    private void doCreate( final RewriteContextKey rewriteContextKey )
    {
        final NodePath mappingNodePath = createContextNodePath( rewriteContextKey );

        final Node existingNode = doGetContextNode( rewriteContextKey );

        if ( existingNode != null )
        {
            throw new RewriteContextExistsException( rewriteContextKey );
        }

        doCreateContextNode( mappingNodePath, RewriteMapping.create().
            contextKey( rewriteContextKey ).
            rewriteRules( RewriteRules.create().build() ).
            build() );

        this.nodeService.refresh( RefreshMode.ALL );
    }

    @Override
    public void delete( final RewriteContextKey rewriteContextKey )
    {
        setRepoContext().runWith( () -> doDelete( rewriteContextKey ) );
    }

    @Override
    public void createRule( final CreateRuleParams params )
    {
        setRepoContext().runWith( () -> doCreateRule( params ) );
    }

    @Override
    public void editRule( final EditRuleParams params )
    {
        setRepoContext().runWith( () -> doEditRule( params ) );
    }

    private void doEditRule( final EditRuleParams params )
    {
        final RewriteContextKey contextKey = params.getContextKey();
        final Node contextNode = getExistingContextNode( contextKey );

        final RewriteMapping rewriteMapping = RewriteMappingSerializer.fromNode( contextNode );

        final List<RewriteRule> rules = new ArrayList<>();

        for ( final RewriteRule rule : rewriteMapping.getRewriteRules().getRuleList() )
        {
            if ( !Objects.equals( rule.getRuleId(), params.getRuleId() ) )
            {
                rules.add( rule );
            }
        }

        rules.add( params.getPosition(), RewriteRule.create().
            ruleId( params.getRuleId() ).
            type( RedirectType.valueOf( params.getType() ) ).
            target( params.getSubstitution() ).
            from( params.getNewPattern() ).
            build() );

        final RewriteRules.Builder newRules = RewriteRules.create().rules( rules );

        final RewriteMapping newMapping = RewriteMapping.create().
            contextKey( contextKey ).rewriteRules( newRules.build() ).
            build();

        doUpdateContextNode( contextNode, newMapping );
    }

    @Override
    public void deleteRule( final DeleteRuleParams params )
    {
        setRepoContext().runWith( () -> doDeleteRule( params ) );
    }

    private void doDeleteRule( final DeleteRuleParams params )
    {
        final RewriteContextKey contextKey = params.getContextKey();
        final Node contextNode = getExistingContextNode( contextKey );

        final RewriteMapping rewriteMapping = RewriteMappingSerializer.fromNode( contextNode );

        final RewriteRules.Builder newRules = RewriteRules.create();
        for ( final RewriteRule rule : rewriteMapping.getRewriteRules().getRuleList() )
        {
            if ( !rule.getRuleId().equals( params.getRuleId() ) )
            {
                newRules.addRule( rule );
            }
        }

        final RewriteMapping newMapping = RewriteMapping.create().
            contextKey( contextKey ).rewriteRules( newRules.build() ).
            build();

        doUpdateContextNode( contextNode, newMapping );
    }

    private void doCreateRule( final CreateRuleParams params )
    {
        final RewriteContextKey contextKey = params.getContextKey();

        final Node contextNode = getExistingContextNode( contextKey );

        final RewriteMapping rewriteMapping = RewriteMappingSerializer.fromNode( contextNode );

        final RewriteRule rule = RewriteRule.create().
            from( params.getFrom() ).
            target( params.getTarget() ).
            type( RedirectType.valueOf( params.getType() ) ).
            build();

        final RewriteRules.Builder rewriteRuleBuilder = RewriteRules.from( rewriteMapping.getRewriteRules() );

        if ( "position".equalsIgnoreCase( params.getInsertStrategy() ) )
        {
            rewriteRuleBuilder.addRule( params.getPosition(), rule );
        }
        else
        {
            rewriteRuleBuilder.addRule( rule, "first".equalsIgnoreCase( params.getInsertStrategy() ) );
        }

        final RewriteMapping newMapping = RewriteMapping.create().
            contextKey( contextKey ).
            rewriteRules( rewriteRuleBuilder.build() ).
            build();

        doUpdateContextNode( contextNode, newMapping );
    }

    private Node getExistingContextNode( final RewriteContextKey contextKey )
    {
        final Node existingNode = doGetContextNode( contextKey );

        if ( existingNode == null )
        {
            throw new RewriteContextNotFoundException( contextKey );
        }
        return existingNode;
    }

    private NodeIds doDelete( final RewriteContextKey rewriteContextKey )
    {
        final NodeIds nodeIds = this.nodeService.deleteByPath( createContextNodePath( rewriteContextKey ) );

        System.out.println( "Deleting nodes with id" + nodeIds );

        this.nodeService.refresh( RefreshMode.ALL );
        return nodeIds;
    }

    private Node doCreateOrUpdate( final NodePath nodePath, final RewriteMapping rewriteMapping )
    {
        final Node existing = this.nodeService.getByPath( nodePath );

        if ( existing == null )
        {
            return doCreateContextNode( nodePath, rewriteMapping );
        }
        else
        {
            return doUpdateContextNode( existing, rewriteMapping );
        }
    }

    private Node doCreateContextNode( final NodePath nodePath, final RewriteMapping rewriteMapping )
    {
        final PropertyTree data = RewriteMappingSerializer.toCreateNodeData( rewriteMapping );

        return this.nodeService.create( CreateNodeParams.create().
            name( nodePath.getName() ).
            parent( nodePath.getParentPath() ).
            data( data ).
            build() );
    }

    private Node doUpdateContextNode( final Node existing, final RewriteMapping rewriteMapping )
    {
        return this.nodeService.update( UpdateNodeParams.create().
            id( existing.id() ).
            path( existing.path() ).
            editor( RewriteMappingSerializer.toUpdateNodeData( rewriteMapping ) ).
            build() );
    }

    private Node doGetContextNode( final RewriteContextKey contextKey )
    {
        return this.nodeService.getByPath( createContextNodePath( contextKey ) );
    }

    private NodePath createContextNodePath( final RewriteContextKey contextKey )
    {
        return NodePath.create( MAPPING_ROOT_NODE, RewriteContextName.from( contextKey ).getName() ).build();
    }

    private Context setRepoContext()
    {
        final User admin = User.create().
            key( RewriteMappingRepoInitializer.SUPER_USER ).
            login( RewriteMappingRepoInitializer.SUPER_USER.getId() ).
            build();
        final AuthenticationInfo authInfo = AuthenticationInfo.create().
            principals( RoleKeys.ADMIN ).
            user( admin ).
            build();

        return ContextBuilder.from( ContextAccessor.current() ).
            repositoryId( RewriteMappingRepoInitializer.REPO_ID ).
            authInfo( authInfo ).
            branch( RewriteMappingRepoInitializer.BRANCH ).
            build();
    }

}
