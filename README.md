# Rewrite App for Enonic XP 
This Enonic XP application allows you to define rewrite rules to handle the request redirects.

The application supports two types of providers to manage rewrite rules:

- `File` - configuration file per a virtual host. That provider is read-only. That means that you need to manage rules directly inside file.
- `Repository` - using this type of provider you can manage redirect rules directly from the application UI. Configuration for each virtual host is stored in the repository called `com.enonic.app.rewrite`. 

**Note**: If you have both types of providers for one virtual host, then `File` provider will take precedence.  

## Configuration

To configure this application, place a file named `com.enonic.app.rewrite.cfg` inside the configuration directory of your XP instance with the following options:

- `enabled` - this option is used to enable or disable handling the request redirects by application.
- `includePattern` - a RegExp pattern for request URI that should be processed by the application.
- `excludePattern` - a RegExp pattern for request URI that should not be processed by the application. 
- `ruleFileNameTemplate` - template of the virtual host configuration filename with rewrite rules. Default template is `com.enonic.app.rewrite.{{vhost}}.conf`, where `{{vhost}}` is mapped to the name of a virtual host mapping given in `com.enonic.xp.web.vhost.cfg` file, for instance, adm. For more information please visit [this page](https://developer.enonic.com/docs/xp/stable/deployment/vhosts).

By default this application is shipped with the following configuration:

    enabled=true
    excludePattern=^/admin/.*|.*/_/asset/.*|.*/_/image/.*
    includePattern=^/site/.*
    ruleFileNameTemplate=com.enonic.app.rewrite.{{vhost}}.conf


## Usage

Currently the application supports *Apache syntax (simplified)*:

``com.enonic.app.rewrite.myvhost.conf``

    RewriteRule "/oldURL$" "/newURL" [R=301]
    RewriteRule "/allUnderThis/(.*)" "/movedToHere/$1" [R=301]
    RewriteRule "/kontakt" "/contact" [R=301]
    RewriteRule "/avis" "https://rett24.no" [R=302]

If you want to specify a rewrite rule which contains URI-illegal characters, then you have to percent-encode `source` and `target`.

    RewriteRule "/reparasjon%20og%20vedlikehold%20av%20kj%C3%B8ret%C3%B8y.pdf" "/kj%C3%B8ret%C3%B8y" [R=301]

Currently the application uses the following flags implicitly for each rule:

- `NE` - noescape
- `L` - last
- `NC` - nocase

For more details about supported flags you can visit the [following page](https://httpd.apache.org/docs/2.4/rewrite/flags.html#flag_ne).

**Note**: Changes to configuration files with rewrite rules won't take effect until the Rewrite app is restarted. 
