package dubbo;

import org.junit.Test;

/**
 * @author JinJing
 * @date 2019/2/13 下午3:29
 */
public class HelloServiceInterTest {

    DubboTester tester = new DubboTester("localhost",20880);

    @Test
    public void test() throws Exception{
        String sayHello = tester.invoke("com.jinjing.springboot.springbootstudy.api.HelloService", "sayHello");
        System.out.println(sayHello);
    }
}
