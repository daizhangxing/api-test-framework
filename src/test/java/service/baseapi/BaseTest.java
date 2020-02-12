package service.baseapi;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by dai on 2020/2/3.
 */
@ContextConfiguration(locations = {"classpath:mybatis/*.xml", "classpath:mapper/openapi/*.xml", "classpath:application-context.xml"})
public class BaseTest extends AbstractTestNGSpringContextTests {

}
