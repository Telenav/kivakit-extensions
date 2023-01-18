package com.telenav.kivakit.microservice.internal.protocols.grpc.runtimeproto;

/**
 * @author Alex Shvid
 */

public record Pair<F, S>(F first, S second)
{

    public static <F, S> Pair<F, S> newPair(F first, S second)
    {
        return new Pair<F, S>(first, second);
    }

    @Override
    public String toString()
    {
        return "Pair [first=" + first + ", second=" + second + "]";
    }
}
