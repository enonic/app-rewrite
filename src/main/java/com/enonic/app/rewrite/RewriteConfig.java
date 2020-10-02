package com.enonic.app.rewrite;

public @interface RewriteConfig
{

    boolean enabled() default false;

    String excludePattern() default "^/admin/.*|.*/_/asset/.*|.*/_/image/.*";

    String includePattern() default "^/site/.*";

    String ruleFileNameTemplate() default "com.enonic.app.rewrite.{{vhost}}.txt";

}
