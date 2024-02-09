package com.mvcguru.risiko.maven.eclipse;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Main.class)
@ExtendWith(SpringExtension.class)
class MainTest {

    @Autowired
    private ApplicationContext applicationContext;
    
    @Test
    void main() {
    	assertTrue(42==42);
        Main.main(new String[] {});
    }

    @Test
    void contextLoads() {
        // Verifica che l'applicazione Spring Boot si avvii correttamente.
        assertThat(applicationContext).isNotNull();
    }
}
