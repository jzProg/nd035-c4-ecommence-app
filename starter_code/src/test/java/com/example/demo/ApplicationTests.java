package com.example.demo;

import com.example.demo.controllers.CartControllerTests;
import com.example.demo.controllers.ItemControllerTests;
import com.example.demo.controllers.OrderControllerTests;
import com.example.demo.controllers.UserControllerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
		{CartControllerTests.class, UserControllerTest.class,
				ItemControllerTests.class, OrderControllerTests.class}
)
public class ApplicationTests {

	@Test
	public void contextLoads() {
	}

}
