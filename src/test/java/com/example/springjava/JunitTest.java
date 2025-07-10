package com.example.springjava;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class JunitTest {
    /*
    * 테스트 오브젝트는 테스트 메소드를 실행할 때마다 오브젝트를 생성을 함.
    * 애플리케이션 컨텍스트는 시작할 때 한번만 만들어 놓고 모두가 공유를 함.(싱글톤 레지스트리)
    * 그래서 애플리케이션 컨텍스트는 구성이나 상태를 테스트 내에서 변경하지 않는 것이 원칙이다.
    * 만약에 번경하려면 @DirtiesContext애노테이션을 추가해 테스트 컨텍스트 프레임워크에게 전달하면 그 클래스틑 애플리케이션 컨텍스트를 공유하지않는다.
    *
    * */

    @Autowired
    ApplicationContext context;

    static Set<JunitTest> testObjects = new HashSet<JunitTest>();
    static ApplicationContext contextObject = null;

    @Test
    public void test1() {
        assertThat(testObjects).doesNotContain(this);
        testObjects.add(this);

        if (contextObject == null) {
            contextObject = this.context;
        }
        assertThat(contextObject).isSameAs(context);
        contextObject = this.context;
    }

    @Test
    public void test2() {
        assertThat(testObjects).doesNotContain(this);
        testObjects.add(this);

        assertThat(contextObject).isSameAs(context);
    }

    @Test
    public void test3() {
        assertThat(testObjects).doesNotContain(this);
        testObjects.add(this);

        assertThat(contextObject).isSameAs(context);
    }
}
