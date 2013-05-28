package com.example.library.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

// @ContextConfiguration(locations={"file:///D:/resin-4.0.32/webapps/ROOT/META-INF/spring/test-config.xml"})
@ContextConfiguration(locations={"/test-config.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class AbstractControllerTest extends AbstractTransactionalJUnit4SpringContextTests {

}