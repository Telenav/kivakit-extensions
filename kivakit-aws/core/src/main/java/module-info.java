module kivakit.aws.core
{
    requires transitive kivakit.kernel;
    requires transitive kivakit.resource;

    requires transitive software.amazon.awssdk.core;
    requires transitive software.amazon.awssdk.regions;
    requires transitive software.amazon.awssdk.auth;
    requires transitive software.amazon.awssdk.services.sts;

    exports com.telenav.kivakit.aws.core;
}
