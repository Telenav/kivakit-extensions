@SuppressWarnings("JavaRequiresAutoModule") module kivakit.merged.grpc
{
    exports com.telenav.kivakit.merged.grpc;

    requires transitive grpc.core;
    requires transitive grpc.context;
    requires transitive grpc.netty;
    requires transitive grpc.stub;
    requires transitive grpc.protobuf;
    requires transitive grpc.protobuf.lite;
}