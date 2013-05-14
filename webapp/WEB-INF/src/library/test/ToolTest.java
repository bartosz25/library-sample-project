package library.test;
 

import library.tools.MailerTool;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
 

public class ToolTest extends AbstractControllerTest {
    @Autowired
    private MailerTool mailerTool;
    
    @Test
    public void testMailerTool() {
        System.out.println(mailerTool.getServletContext().getRealPath(System.getProperty("file.separator")));
        System.out.println(mailerTool.getServletContext());
    }
}