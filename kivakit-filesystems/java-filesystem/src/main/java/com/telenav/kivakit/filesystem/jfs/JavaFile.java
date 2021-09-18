package com.telenav.kivakit.filesystem.jfs;

import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.kernel.messaging.Broadcaster;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.WritableResource;
import com.telenav.kivakit.resource.compression.Codec;
import com.telenav.kivakit.resource.path.FilePath;

import java.io.InputStream;
import java.io.OutputStream;


public class JavaFile extends JavaFileSystemObject implements FileService{

    public JavaFile(final FilePath path) {
        super(path, false);
    }

    public JavaFile(final String path) {
        super(FilePath.parseFilePath(path), false);
    }

    @Override
    public InputStream onOpenForReading() {
        return null;
    }

    @Override
    public Boolean isWritable() {
        return null;
    }

    @Override
    public OutputStream onOpenForWriting() {
        return null;
    }

    @Override
    public void addListener(Listener listener, Filter<Transmittable> filter) {

    }

    @Override
    public void clearListeners() {

    }

    @Override
    public boolean hasListeners() {
        return false;
    }

    @Override
    public Broadcaster messageSource() {
        return null;
    }

    @Override
    public void messageSource(Broadcaster parent) {

    }

    @Override
    public void removeListener(Listener listener) {

    }

    @Override
    public void onMessage(Message message) {

    }

    @Override
    public boolean renameTo(FileService that) {
        return false;
    }

    @Override
    public Codec codec() {
        return null;
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public Resource materialized(ProgressReporter reporter) {
        return null;
    }

    @Override
    public boolean isFolder() {
        return false;
    }

    @Override
    public FolderService parent() {
        return null;
    }

    @Override
    public FolderService root() {
        return null;
    }

    @Override
    public void copyTo(WritableResource destination, CopyMode mode, ProgressReporter reporter) {

    }
}
