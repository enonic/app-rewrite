package com.enonic.app.rewrite;

import org.osgi.service.component.annotations.Component;

@Component(immediate = true)
public class LicenseServiceImpl
    implements LicenseService
{
    private volatile boolean licenseEnabled = false;

    @Override
    public boolean isLicenseEnabled()
    {
        return licenseEnabled;
    }

    @Override
    public void setLicenseEnabled()
    {
        this.licenseEnabled = true;
    }
}
