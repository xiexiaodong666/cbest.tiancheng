package com.welfare.serviceaccount;

import com.welfare.ServiceAccountApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Description： sprintboot base test
 * Date： 2020/7/21 9:58
 *
 * @author changchun.xue
 */
@SpringBootTest(classes = ServiceAccountApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class BaseTest {
}
