package org.eclipse.emf.emfstore.performance.test;

import org.eclipse.emf.emfstore.performance.test.memory.MemoryLoadTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
	PerformanceTest.class,
	MemoryLoadTest.class
})
public class AllPerformanceTests {

}
