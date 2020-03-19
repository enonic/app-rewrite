# app-rewrite
App for handling request redirects


## Config

``com.enonic.app.redirect.cfg:``

    enabled=true
    excludePatterns=^/admin/.*,.*/_/asset/.*,.*/_/image/.*
    includePatterns=^/site/.*
    ruleFilePattern=com.enonic.app.rewrite.{{vhost}}.txt


## Rewrite files

You can specify files containing rewrite config based on the pattern in the config "ruleFilePattern"
The vhost-name is mapped to the name of a vhost given in ``com.enonic.xp.web.vhost.cfg``

Currently supported; apache syntax (simplyfied)

``com.enonic.app.rewrite.myvhost.txt``

    RewriteRule "/oldURL$" "/newURL" [R=301]
    RewriteRule "/allUnderThis/(.*)" "/movedToHere/$1" [R=301]
    RewriteRule "/kontakt" "/contact" [R=301]
    RewriteRule "/avis" "https://www.vg.no" [R=302]


