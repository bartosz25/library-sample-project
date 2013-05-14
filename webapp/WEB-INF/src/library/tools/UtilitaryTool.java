package library.tools;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilitaryTool {
    final static Logger logger = LoggerFactory.getLogger(UtilitaryTool.class);

    public static String getBaseUrl(HttpServletRequest request) {
        try {
            return new URL(request.getScheme(), request.getServerName(), request.getServerPort(), "").toString();
        } catch (Exception e) {
            logger.error("An exception occured on constructing URL", e);
            return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
        }
    }

}