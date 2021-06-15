module kivakit.aws.s3
{
    requires transitive kivakit.kernel;
    requires transitive kivakit.configuration;
    requires transitive kivakit.aws.core;
    requires transitive kivakit.resource;

    requires transitive software.amazon.awssdk.http;
    requires transitive software.amazon.awssdk.services.s3;
    requires transitive software.amazon.awssdk.core;
    requires transitive software.amazon.awssdk.regions;
    requires transitive software.amazon.awssdk.auth;
}
