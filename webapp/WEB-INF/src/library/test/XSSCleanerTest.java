package library.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import library.security.XSSCleaner;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class XSSCleanerTest extends AbstractControllerTest {    
    @Autowired
    private XSSCleaner xssCleaner;

    @Test
    public void testCleanedStrings() {
        List<String> acceptedTags = new ArrayList<String>();
        acceptedTags.add("b");
        acceptedTags.add("i");
        System.out.println("Accepted tags are " + acceptedTags);
        Map<String, String> outputs = new HashMap<String, String>();
        outputs.put("<b><b>bolder</b>", "<b><b>bolder</b>");
        outputs.put("<i>italic</i>", "<i>italic</i>");
        outputs.put("<b><i>bolder-italic</i></b>", "<b><i>bolder-italic</i></b>");
        outputs.put("<script type=\"text/javascript\">alert(\"test\")</script>", "alert(\"test\")");
        outputs.put("<img src=\"image.jgp\" />", "");
        outputs.put("<A HREF=\"http://www.gohttp://www.google.com/ogle.com/\">XSS</A>", "XSS");
        outputs.put("<A HREF=\"javascript:document.location='http://www.google.com/'\">XSS</A>", "XSS");
        outputs.put("<A HREF=\"http://www.google.com./\">XSS</A>", "XSS");
        outputs.put("<A HREF=\"http://google.com/\">XSS</A>", "XSS");
        outputs.put("<A HREF=\"http://google:ha.ckers.org\">XSS</A>", "XSS");
        outputs.put("<A HREF=\"//google\">XSS>/A>", "XSS>/A>");
        outputs.put("<A HREF=\"h&#x0A;tt&#09;p://6&#09;6.000146.0x7.147/\">XSS</A>", "XSS");
        outputs.put("<A HREF=\"http://0102.0146.0007.00000223/\">XSS</A>", "XSS");
        outputs.put("<A HREF=\"http://0x42.0x0000066.0x7.0x93/\">XSS</A>", "XSS");
        outputs.put("<A HREF=\"http://1113982867/\">XSS</A>", "XSS");
        outputs.put("<A HREF=\"http://%77%77%77%2E%67%6F%6F%67%6C%65%2E%63%6F%6D\">XSS</A>", "XSS");
        outputs.put("<A HREF=\"http://66.102.7.147/\">XSS</A>", "XSS");
        outputs.put("<SCRIPT a=\">'>\" SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>", "'>\" SRC=\"http://ha.ckers.org/xss.js\">");
        outputs.put("<SCRIPT>document.write(\"<SCRI\");</SCRIPT>PT SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>", "document.write(\"PT SRC=\"http://ha.ckers.org/xss.js\">");
        outputs.put("<SCRIPT a=`>` SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>", "` SRC=\"http://ha.ckers.org/xss.js\">");
        outputs.put("<SCRIPT \"a='>'\" SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>", "'\" SRC=\"http://ha.ckers.org/xss.js\">");
        outputs.put("<SCRIPT a=\"blah\" '' SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>", "");
        outputs.put("<SCRIPT =\"blah\" SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>", "");
        outputs.put("<SCRIPT a=\">\" SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>", "\" SRC=\"http://ha.ckers.org/xss.js\">");
        outputs.put("<SCRIPT>a=/XSS/"+
"alert(a.source)</SCRIPT>", "a=/XSS/alert(a.source)");
        outputs.put("<IMG \"\"\"><SCRIPT>alert(\"XSS\")</SCRIPT>\">", "alert(\"XSS\")\">");
        outputs.put("<<SCRIPT>alert(\"XSS\");//<</SCRIPT>", "alert(\"XSS\");//");
        outputs.put("<IFRAME SRC=http://ha.ckers.org/scriptlet.html <", "<IFRAME SRC=http://ha.ckers.org/scriptlet.html <");
        outputs.put("<IMG SRC=\"javascript:alert('XSS')\"", "<IMG SRC=\"javascript:alert('XSS')\"");
        outputs.put("<SCRIPT SRC=//ha.ckers.org/.j>", "");
        outputs.put("<SCRIPT SRC=http://ha.ckers.org/xss.js", "<SCRIPT SRC=http://ha.ckers.org/xss.js");
        outputs.put("<BODY onload!#$%&()*~+-_.,:;?@[/|\\]^`=alert(\"XSS\")>", "");
        outputs.put("<SCRIPT/XSS SRC=\"http://ha.ckers.org/xss.js\"></SCRIPT>", "");
        outputs.put("<IMG SRC=\" &#14;  javascript:alert('XSS');\">", "");
        outputs.put("<IMG&#x0D;SRC&#x0D;=&#x0D;\"&#x0D;j&#x0D;a&#x0D;v&#x0D;a&#x0D;s&#x0D;c&#x0D;r&#x0D;i&#x0D;p&#x0D;t&#x0D;:&#x0D;a&#x0D;l&#x0D;e&#x0D;r&#x0D;t&#x0D;(&#x0D;'&#x0D;X&#x0D;S&#x0D;S&#x0D;'&#x0D;)&#x0D;\"&#x0D;>&#x0D;", "&#x0D;");
        outputs.put("<IMG SRC=\"jav&#x0D;ascript:alert('XSS');\">", "");
        outputs.put("<IMG SRC=\"jav&#x0A;ascript:alert('XSS');\">", "");
        outputs.put("<IMG SRC=\"jav&#x09;ascript:alert('XSS');\">", "");
        outputs.put("<IMG SRC=\"jav&#x09;ascript:alert('XSS');\">", "");
        outputs.put("<STYLE>@im\\port'\\ja\\vasc\\ript:alert(\"XSS\")';</STYLE>", "@im\\port'\\ja\\vasc\\ript:alert(\"XSS\")';");
        outputs.put("</TITLE><SCRIPT>alert(\"XSS\");</SCRIPT>", "alert(\"XSS\");");
        outputs.put("\";alert('XSS');//", "\";alert('XSS');//");
        outputs.put("<HEAD><META HTTP-EQUIV=\"CONTENT-TYPE\" CONTENT=\"text/html; charset=UTF-7\"> </HEAD>+ADw-SCRIPT+AD4-alert('XSS');+ADw-/SCRIPT+AD4-", "+ADw-SCRIPT+AD4-alert('XSS');+ADw-/SCRIPT+AD4-");
        outputs.put("<IMG SRC=&#x6A&#x61&#x76&#x61&#x73&#x63&#x72&#x69&#x70&#x74&#x3A&#x61&#x6C&#x65&#x72&#x74&#x28&#x27&#x58&#x53&#x53&#x27&#x29>", "");
        outputs.put("<DIV STYLE=\"background-image:\0075\0072\006C\0028'\006a\0061\0076\0061\0073\0063\0072\0069\0070\0074\003a\0061\006c\0065\0072\0074\0028.1027\0058.1053\0053\0027\0029'\0029\">", "");
        outputs.put("<IMG SRC=&#0000106&#0000097&#0000118&#0000097&#0000115&#0000099&#0000114&#0000105&#0000112&#0000116&#0000058&#0000097&#0000108&#0000101&#0000114&#0000116&#0000040&#0000039&#0000088&#0000083&#0000083&#0000039&#0000041>", "");
        outputs.put("<IMG SRC=&#106;&#97;&#118;&#97;&#115;&#99;&#114;&#105;&#112;&#116;&#58;&#97;&#108;&#101;&#114;&#116;&#40;&#39;&#88;&#83;&#83;&#39;&#41;>", "");
        outputs.put("<IMG SRC=javascript:alert(String.fromCharCode(88,83,83))>", "");
        outputs.put("<IMG SRC=`javascript:alert(\"RSnake says, 'XSS'\")`>", "");
        outputs.put("<IMG SRC=javascript:alert(&quot;XSS&quot;)>", "");
        outputs.put("<IMG SRC=JaVaScRiPt:alert('XSS')>", "");
        outputs.put("<"+
"%3C"+
"&lt"+
"<"+
"&LT"+
"<"+
"&60"+
"&#060"+
"&#0060"+
"&#00060"+
"&#000060"+
"&#0000060"+
"&#60;"+
"&#060;"+
"&#0060;"+
"&#00060;"+
"&#000060;"+
"&#0000060;"+
"&#x3c"+
"&#x03c"+
"&#x003c"+
"&#x0003c"+
"&#x00003c"+
"&#x000003c"+
"&#x3c;"+
"&#x03c;"+
"&#x003c;"+
"&#x0003c;"+
"&#x00003c;"+
"&#x000003c;"+
"&#X3c"+
"&#X03c"+
"&#X003c"+
"&#X0003c"+
"&#X00003c"+
"&#X000003c"+
"&#X3c;"+
"&#X03c;"+
"&#X003c;"+
"&#X0003c;"+
"&#X00003c;"+
"&#X000003c;"+
"&#x3C"+
"&#x03C"+
"&#x003C"+
"&#x0003C"+
"&#x00003C"+
"&#x000003C"+
"&#x3C;"+
"&#x03C;"+
"&#x003C;"+
"&#x0003C;"+
"&#x00003C;"+
"&#x000003C;"+
"&#X3C"+
"&#X03C"+
"&#X003C"+
"&#X0003C"+
"&#X00003C"+
"&#X000003C"+
"&#X3C;"+
"&#X03C;"+
"&#X003C;"+
"&#X0003C;"+
"&#X00003C;"+
"&#X000003C;"+
"\\x3c"+
"\\x3C"+
"\\u003c"+
"\\u003C", "<%3C&lt<&LT<&60&#060&#0060&#00060&#000060&#0000060&#60;&#060;&#0060;&#00060;&#000060;&#0000060;&#x3c&#x03c&#x003c&#x0003c&#x00003c&#x000003c&#x3c;&#x03c;&#x003c;&#x0003c;&#x00003c;&#x000003c;&#X3c&#X03c&#X003c&#X0003c&#X00003c&#X000003c&#X3c;&#X03c;&#X003c;&#X0003c;&#X00003c;&#X000003c;&#x3C&#x03C&#x003C&#x0003C&#x00003C&#x000003C&#x3C;&#x03C;&#x003C;&#x0003C;&#x00003C;&#x000003C;&#X3C&#X03C&#X003C&#X0003C&#X00003C&#X000003C&#X3C;&#X03C;&#X003C;&#X0003C;&#X00003C;&#X000003C;\\x3c\\x3C\\u003c\\u003C");
        outputs.put("<BR SIZE=\"&{alert('XSS')}\">", "");
        outputs.put("<? echo('<SCR)';echo('IPT>alert(\"XSS\")</SCRIPT>'); ?>", "alert(\"XSS\")'); ?>");
        outputs.put("<!--#exec cmd=\"/bin/echo '<SCRIPT SRC'\"--><!--#exec cmd=\"/bin/echo '=http://ha.ckers.org/xss.js></SCRIPT>'\"-->", "'\"-->");
        outputs.put("<SCRIPT SRC=\"http://ha.ckers.org/xss.jpg\"></SCRIPT>", "");
        outputs.put("<XSS STYLE=\"behavior: url(http://ha.ckers.org/xss.htc);\">", "");
        outputs.put("<META HTTP-EQUIV=\"Set-Cookie\" Content=\"USERID=<SCRIPT>alert('XSS')</SCRIPT>\">", "alert('XSS')\">");
        outputs.put("<!--[if gte IE 4]><SCRIPT>alert('XSS');</SCRIPT><![endif]-->", "alert('XSS');");
        outputs.put("<HTML><BODY><?xml:namespace prefix=\"t\" ns=\"urn:schemas-microsoft-com:time\"><?import namespace=\"t\" implementation=\"#default#time2\"><t:set attributeName=\"innerHTML\" to=\"XSS<SCRIPT DEFER>alert('XSS')</SCRIPT>\"> </BODY></HTML>", "alert('XSS')\">");
        outputs.put("<XML SRC=\"http://ha.ckers.org/xsstest.xml\" ID=I></XML><SPAN DATASRC=#I DATAFLD=C DATAFORMATAS=HTML></SPAN>", "");
        outputs.put("<XML ID=\"xss\"><I><B><IMG SRC=\"javas<!-- -->cript:alert('XSS')\"></B></I></XML><SPAN DATASRC=\"#xss\" DATAFLD=\"B\" DATAFORMATAS=\"HTML\"></SPAN>", "<I><B>cript:alert('XSS')\"></B></I>");
        outputs.put("<XML ID=I><X><C><![CDATA[<IMG SRC=\"javas]]><![CDATA[cript:alert('XSS');\">]]></C></X></xml><SPAN DATASRC=#I DATAFLD=C DATAFORMATAS=HTML>", "]]>");
        outputs.put("<HTML xmlns:xss><?import namespace=\"xss\" implementation=\"http://ha.ckers.org/xss.htc\"><xss:xss>XSS</xss:xss></HTML>", "XSS");
        outputs.put("<TABLE><TD BACKGROUND=\"javascript:alert('XSS')\"></TD></TABLE>", "");
        outputs.put("<TABLE BACKGROUND=\"javascript:alert('XSS')\"></TABLE>", "");
        outputs.put("<STYLE>BODY{-moz-binding:url(\"http://ha.ckers.org/xssmoz.xml#xss\")}</STYLE>", "BODY{-moz-binding:url(\"http://ha.ckers.org/xssmoz.xml#xss\")}");
        outputs.put("<STYLE>@import'http://ha.ckers.org/xss.css';</STYLE>", "@import'http://ha.ckers.org/xss.css';");
        outputs.put("<LINK REL=\"stylesheet\" HREF=\"http://ha.ckers.org/xss.css\">", "");
        outputs.put("<LINK REL=\"stylesheet\" HREF=\"javascript:alert('XSS');\">", "");
        outputs.put("<STYLE type=\"text/css\">BODY{background:url(\"javascript:alert('XSS')\")}</STYLE>", "BODY{background:url(\"javascript:alert('XSS')\")}");
        outputs.put("<STYLE>.XSS{background-image:url(\"javascript:alert('XSS')\");}</STYLE><A CLASS=XSS></A>", ".XSS{background-image:url(\"javascript:alert('XSS')\");}");
        outputs.put("<XSS STYLE=\"xss:expression(alert('XSS'))\">", "");
        outputs.put("<IMG STYLE=\"xss:expr/*XSS*/ession(alert('XSS'))\">", "");
        outputs.put("<STYLE TYPE=\"text/javascript\">alert('XSS');</STYLE>", "alert('XSS');");
        outputs.put("a=\"get\";&#10;b=\"URL(\"\";&#10;c=\"javascript:\";&#10;d=\"alert('XSS');\")\";&#10;eval(a+b+c+d);", "a=\"get\";&#10;b=\"URL(\"\";&#10;c=\"javascript:\";&#10;d=\"alert('XSS');\")\";&#10;eval(a+b+c+d);");
        outputs.put("<EMBED SRC=\"http://ha.ckers.org/xss.swf\" AllowScriptAccess=\"always\"></EMBED>", "");
        outputs.put("<OBJECT classid=clsid:ae24fdae-03c6-11d1-8b76-0080c744f389><param name=url value=javascript:alert('XSS')></OBJECT>", "");
        outputs.put("<OBJECT TYPE=\"text/x-scriptlet\" DATA=\"http://ha.ckers.org/scriptlet.html\"></OBJECT>", "");
        outputs.put("<IMG SRC=\"mocha:[code]\">", "");
        outputs.put("<META HTTP-EQUIV=\"refresh\" CONTENT=\"0; URL=http://;URL=javascript:alert('XSS');\">", "");
        outputs.put("<META HTTP-EQUIV=\"refresh\" CONTENT=\"0;url=data:text/html;base64,PHNjcmlwdD5hbGVydCgnWFNTJyk8L3NjcmlwdD4K\">", "");
        outputs.put("<META HTTP-EQUIV=\"refresh\" CONTENT=\"0;url=javascript:alert('XSS');\">", "");
        outputs.put("%BCscript%BEalert(%A2XSS%A2)%BC/script%BE", "%BCscript%BEalert(%A2XSS%A2)%BC/script%BE");
        outputs.put("<IMG SRC=\"livescript:[code]\">", "");
        outputs.put("<LAYER SRC=\"http://ha.ckers.org/scriptlet.html\"></LAYER>", "");
        outputs.put("<IMG SRC='vbscript:msgbox(\"XSS\")'>", "");
        outputs.put("<STYLE>li {list-style-image: url(\"javascript:alert(&#39;XSS&#39;)\");}</STYLE><UL><LI>XSS", "li {list-style-image: url(\"javascript:alert(&#39;XSS&#39;)\");}XSS");
        outputs.put("exp/*<XSS STYLE='no\\xss:noxss(\"*//*\");xss:&#101;x&#x2F;*XSS*//*/*/pression(alert(\"XSS\"))'>", "exp/*");
        outputs.put("Redirect 302 /a.jpg http://victimsite.com/admin.asp&deleteuser", "Redirect 302 /a.jpg http://victimsite.com/admin.asp&deleteuser");
        outputs.put("<IMG SRC=\"http://www.thesiteyouareon.com/somecommand.php?somevariables=maliciouscode\">", "");
        outputs.put("<IMG LOWSRC=\"javascript:alert('XSS');\">", "");
        outputs.put("<IMG DYNSRC=\"javascript:alert('XSS');\">", "");
        outputs.put("<IMG SRC=javascript:alert('XSS')>", "");
        outputs.put("<IMG SRC=\"javascript:alert('XSS');\">", "");
        outputs.put("<INPUT TYPE=\"IMAGE\" SRC=\"javascript:alert('XSS');\">", "");
        outputs.put("<IFRAME SRC=\"javascript:alert('XSS');\"></IFRAME>", "");
        outputs.put("<FRAMESET><FRAME SRC=\"javascript:alert('XSS');\"></FRAMESET>", "");
        outputs.put("<DIV STYLE=\"width: expression(alert('XSS'));\">", "");
        outputs.put("<DIV STYLE=\"background-image: url(&#1;javascript:alert('XSS'))\">", "");
        outputs.put("<DIV STYLE=\"background-image: url(javascript:alert('XSS'))\">", "");
        outputs.put("<BODY ONLOAD=alert('XSS')>", "");
        outputs.put("<BODY BACKGROUND=\"javascript:alert('XSS');\">", "");
        outputs.put("<BGSOUND SRC=\"javascript:alert('XSS');\">", "");
        outputs.put("<BASE HREF=\"javascript:alert('XSS');//\">", "");
        outputs.put("<SCRIPT>alert(String.fromCharCode(88,83,83))</SCRIPT>", "alert(String.fromCharCode(88,83,83))");
        outputs.put("<SCRIPT SRC=http://ha.ckers.org/xss.js></SCRIPT>", "");
        outputs.put("<SCRIPT>alert('XSS')</SCRIPT>", "alert('XSS')");
        outputs.put("'';!--\"<XSS>=&{()}", "'';!--\"=&{()}");
        outputs.put("';alert(String.fromCharCode(88,83,83))//\';alert(String.fromCharCode(88,83,83))//\";alert(String.fromCharCode(88,83,83))//\\\";alert(String.fromCharCode(88,83,83))//--></SCRIPT>\">'><SCRIPT>alert(String.fromCharCode(88,83,83))</SCRIPT>=&{}", "';alert(String.fromCharCode(88,83,83))//\';alert(String.fromCharCode(88,83,83))//\";alert(String.fromCharCode(88,83,83))//\\\";alert(String.fromCharCode(88,83,83))//-->\">'>alert(String.fromCharCode(88,83,83))=&{}");
        for (Map.Entry<String, String> entry : outputs.entrySet()) {
            String input = entry.getKey();
            String output = entry.getValue();
            String filteredOutput = xssCleaner.filterString(input, acceptedTags);
            System.out.println("Received output " + filteredOutput + " for " + input);
            Assert.assertEquals("Input ("+input+") is not the same as expected output ("+output+"). Filtered output was \"" + filteredOutput + "\".", output, filteredOutput); 
        }
    }
}