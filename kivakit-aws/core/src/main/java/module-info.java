module kivakit.aws.core
{
    requires kivakit.kernel;

    requires software.amazon.awssdk.regions;
    requires software.amazon.awssdk.auth;
    requires kivakit.resource;

    exports com.telenav.kivakit.aws.core;
    exports com.telenav.kivakit.aws.core.security;
}
