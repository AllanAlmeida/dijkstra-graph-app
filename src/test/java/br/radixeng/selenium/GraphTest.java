package br.radixeng.selenium;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import br.radixeng.Application;

@RunWith(SpringRunner.class)	
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public class GraphTest {

	private WebDriver driver;
	
	JavascriptExecutor js;
	
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "src/main/resources/driver/chromedriver80.exe");
		driver = new ChromeDriver();
		driver.get("http://localhost:8080/distanciaMinima");
		js = (JavascriptExecutor) driver;
	}
	
	@After
	public void tearDown() {
		driver.quit();
	}
	
	@Test
	public void graph2ac() throws Exception, InterruptedException {
		
		Thread.sleep(3000);
		driver.manage().window().setSize(new Dimension(516, 708));
    	Thread.sleep(200);
    	driver.findElement(By.id("graphId")).click();
    	driver.findElement(By.id("graphId")).sendKeys("2");
    	driver.findElement(By.id("town1")).sendKeys("A");
    	driver.findElement(By.id("town2")).sendKeys("C");
    	Thread.sleep(200);
    	driver.findElement(By.id("bth-search")).click();
    
    	Thread.sleep(200);
    	driver.findElement(By.id("graphId")).clear();
    	driver.findElement(By.id("town1")).clear();
    	driver.findElement(By.id("town2")).clear();
    	Thread.sleep(3000);
    	
    	driver.findElement(By.id("graphId")).click();
    	driver.findElement(By.id("graphId")).sendKeys("2");
    	driver.findElement(By.id("town1")).sendKeys("B");
    	driver.findElement(By.id("town2")).sendKeys("B");
    	Thread.sleep(200);
    	driver.findElement(By.id("bth-search")).click();
    	Thread.sleep(3000);
    	
    	Thread.sleep(200);
    	driver.findElement(By.id("graphId")).clear();
    	driver.findElement(By.id("town1")).clear();
    	driver.findElement(By.id("town2")).clear();
    	Thread.sleep(3000);
    	
    	driver.findElement(By.id("graphId")).click();
    	driver.findElement(By.id("graphId")).sendKeys("2");
    	driver.findElement(By.id("town1")).sendKeys("A");
    	driver.findElement(By.id("town2")).sendKeys("Z");
    	Thread.sleep(200);
    	driver.findElement(By.id("bth-search")).click();
    	Thread.sleep(3000);
	}
}
