package mutil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CidTest {

    @BeforeEach
    void setup() {
        Mockito.reset();
    }

    @Test
    void testGetCIDReq() throws IOException {
        String testUrl = "https://test.com";
        HttpURLConnection connection = Cid.getCIDReq(testUrl);

        assertNotNull(connection);
        assertEquals("GET", connection.getRequestMethod());
        assertEquals(Cid.REFERER, connection.getRequestProperty("Referer"));
        assertNotNull(connection.getRequestProperty("User-Agent"));
    }

    // Helper method to compress data in GZIP format
    private byte[] compressStringToGzip(String data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            gzipOutputStream.write(data.getBytes(StandardCharsets.UTF_8));
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Test
    void testGetCIDS() throws IOException {
        try (MockedStatic<Cid> mockedCid = Mockito.mockStatic(Cid.class, Mockito.CALLS_REAL_METHODS)) {
            HttpURLConnection mockConnection = mock(HttpURLConnection.class);

            // 模拟返回GZIP编码
            when(mockConnection.getContentEncoding()).thenReturn("gzip");

            // 压缩 mockResponse 到 GZIP 格式
            String mockResponse = "{\"cid\": 123456789}";
            ByteArrayInputStream gzipInputStream = new ByteArrayInputStream(compressStringToGzip(mockResponse));
            when(mockConnection.getInputStream()).thenReturn(new GZIPInputStream(gzipInputStream));

            mockedCid.when(() -> Cid.getCIDReq(anyString())).thenReturn(mockConnection);

            // 模拟URL列表
            List<String> urls = List.of("http://test.com/1");
            List<Long> cids = Cid.getCIDS(urls);

            // 验证解析出来的 CID
            assertEquals(1, cids.size());
            assertEquals(123456789, cids.get(0));
        }
    }
}
