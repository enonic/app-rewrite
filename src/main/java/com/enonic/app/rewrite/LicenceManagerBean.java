package com.enonic.app.rewrite;

import java.util.function.Supplier;

import com.enonic.xp.script.bean.BeanContext;
import com.enonic.xp.script.bean.ScriptBean;

public class LicenceManagerBean
    implements ScriptBean
{
    private Supplier<LicenseService> licenseDetailsManagerSupplier;

    @Override
    public void initialize( final BeanContext context )
    {
        this.licenseDetailsManagerSupplier = context.getService( LicenseService.class );
    }

    public void activateLicense()
    {
        licenseDetailsManagerSupplier.get().setLicenseEnabled();
    }
}
