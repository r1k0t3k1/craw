package main.java.utils;

import burp.api.montoya.MontoyaApi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;

public class Compresser {
    public static byte[] compress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        try (SevenZOutputFile z7z = new SevenZOutputFile(channel, "password".toCharArray())) {
            SevenZArchiveEntry entry = new SevenZArchiveEntry();
            var datetime = new Date();
            entry.setName(new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(datetime));
            entry.setSize(bytes.length);
            z7z.putArchiveEntry(entry);
            z7z.write(bytes);
            z7z.closeArchiveEntry();
            z7z.finish();
            return channel.array();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decompress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(bytes);
        try (SevenZFile sevenZFile = new SevenZFile(channel, "password".toCharArray())) {
            byte[] buffer = new byte[4096];
            while (sevenZFile.getNextEntry() != null) {
                int n;
                while ((n = sevenZFile.read(buffer)) > -1) {
                    out.write(buffer, 0, n);
                }
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("SevenZ decompress error", e);
        }
    }
}
