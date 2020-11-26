# app-rewrite
App for handling request redirects


## Config

``com.enonic.app.redirect.cfg:``

    enabled=true
    excludePattern=^/admin/.*|.*/_/asset/.*|.*/_/image/.*
    includePattern=^/site/.*
    ruleFileNameTemplate=com.enonic.app.rewrite.{{vhost}}.conf


## Rewrite files

You can specify files containing rewrite config based on the pattern in the config "ruleFileNameTemplate"
The vhost-name is mapped to the name of a vhost given in ``com.enonic.xp.web.vhost.cfg``

Currently supported; 

*Apache syntax (simplyfied)*

``com.enonic.app.rewrite.myvhost.conf``

    RewriteRule "/oldURL$" "/newURL" [R=301]
    RewriteRule "/allUnderThis/(.*)" "/movedToHere/$1" [R=301]
    RewriteRule "/kontakt" "/contact" [R=301]
    RewriteRule "/avis" "https://www.vg.no" [R=302]

*csv*

``com.enonic.app.rewrite.myvhost.csv``

    From,Target,Type
    ^/blog/tag/video$,https://enonic.com/?topic=video,301
    ^/blog/tag/digital-experiences$,https://enonic.com/blog?topic=digital+experiences,301
    ^/blog/tag/digital-project$,https://enonic.com/blog?topic=digital+projects,301

