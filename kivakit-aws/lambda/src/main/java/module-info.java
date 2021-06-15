module kivakit.aws.lambda
{
    requires transitive kivakit.kernel;
    requires transitive kivakit.aws.s3;
    requires transitive kivakit.resource;
    requires transitive kivakit.serialization.json;
    requires transitive kivakit.configuration;

    requires transitive aws.lambda.java.core;
    requires transitive software.amazon.awssdk.services.lambda;
    requires transitive software.amazon.awssdk.auth;
    requires transitive software.amazon.awssdk.http;
    requires transitive software.amazon.awssdk.core;
}
