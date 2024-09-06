package mutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Comment {

    private static final String BASE_FORMAT = "https://api.bilibili.com/x/v2/dm/web/seg.so?type=1&oid=%d&segment_index=%d";
    private final long cid;
    private int index;
    private static final AtomicInteger CommentCount = new AtomicInteger(0);

    public Comment(long cid) {
        this.cid = cid;
        this.index = 0;
    }

    public URL getNextURL() throws IOException {
        index++;
        String url = String.format(BASE_FORMAT, cid, index);
        return new URL(url);
    }

    public static Map<String, Integer> findComment(long cid) {
        HttpURLConnection connection = null;
        Comment commentUtil = new Comment(cid);
        Map<String, Integer> m = new HashMap<>();
        System.out.printf("正在获取第%d个cid的弹幕\n", CommentCount.incrementAndGet());
        try {
            while (true) {
                URL url = commentUtil.getNextURL();
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", Utils.getUserAgent());
                connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
                connection.setRequestProperty("Cookie","buvid4=4AB4E671-01BD-492F-D6E3-37979C3663CC68101-023041323-b1nz50QSFWAoifr9BxZQNA%3D%3D; DedeUserID=424161100; DedeUserID__ckMd5=b2734a7edd59ca72; header_theme_version=CLOSE; hit-dyn-v2=1; buvid_fp_plain=undefined; enable_web_push=DISABLE; _uuid=F96102C9F-710C1-ED71-A33C-2710B62C8899271543infoc; buvid3=2ACB37D0-884B-DAB5-F2A2-19F68B8D3EEE14933infoc; b_nut=1716209114; rpdid=|(J~|Ru|l||m0J'u~uYk|muu~; CURRENT_FNVAL=4048; home_feed_column=5; CURRENT_QUALITY=112; fingerprint=a06dce1ee86330880ef5181107943775; buvid_fp=a06dce1ee86330880ef5181107943775; bili_ticket=eyJhbGciOiJIUzI1NiIsImtpZCI6InMwMyIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3MjU1NDk3OTEsImlhdCI6MTcyNTI5MDUzMSwicGx0IjotMX0.yEzDzTtE1VnlKXi0yyJukEBcCYedDsph3Y0UgRW-7Lo; bili_ticket_expires=1725549731; SESSDATA=28ce8808%2C1740890489%2C68c24%2A91CjDo8SQiS5BOr59kSZwDXj5F4xS8AdJ7fOzENVpW0CiwPkq6LcA62o-BrLZU-43ukXkSVjNtV1pNZXZlVFNPVXhsOGxtUUpDbmV1QzlidXlUd2o4dmZzNTk4RDcxNmVmYi1iSkhxYTMzUDJLTGU4Y3JYdlpuZGUzOUdLeHNielJHcjRnV0FKTnZRIIEC; bili_jct=78f918fc6b06574f70786636c89e2ad2; browser_resolution=2048-1034; PVID=2; sid=5i42ahn3; b_lsid=FE6E61072_191BB7C321A; bp_t_offset_424161100=973195969585741824");

                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    return m;
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                Pattern pattern = Pattern.compile("[a-z0-9]{8}:.*?@");
                Matcher matcher = pattern.matcher(content.toString());
                while (matcher.find()) {
                    String result = matcher.group();
                    if (result.length() > 10) {
                        result = result.substring(10, result.length() - 1);
                        m.merge(result, 1, Integer::sum);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return m;
    }
}