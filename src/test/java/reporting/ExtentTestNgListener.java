package reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.*;

import java.nio.file.Paths;

public class ExtentTestNgListener implements ITestListener, ISuiteListener {

  private static ExtentReports extent;
  private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

  public static ExtentTest currentTest() {
    return test.get();
  }

  @Override
  public void onStart(ISuite suite) {
    String out = Paths.get("test-output", "ExtentReport", "index.html").toString();
    ExtentSparkReporter spark = new ExtentSparkReporter(out);
    extent = new ExtentReports();
    extent.attachReporter(spark);
    extent.setSystemInfo("framework", "TestNG + Selenium + Extent");
  }

  @Override
  public void onFinish(ISuite suite) {
    if (extent != null) extent.flush();
  }

  @Override
  public void onTestStart(ITestResult result) {
    String name = result.getMethod().getMethodName();
    test.set(extent.createTest(name));
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    if (currentTest() != null) currentTest().pass("Passed");
  }

  @Override
  public void onTestFailure(ITestResult result) {
    if (currentTest() != null) currentTest().fail(result.getThrowable());
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    if (currentTest() != null) {
      Throwable t = result.getThrowable();
      if (t != null) currentTest().skip(t); else currentTest().skip("Skipped");
    }
  }

  @Override public void onTestFailedButWithinSuccessPercentage(ITestResult result) { }
  @Override public void onStart(ITestContext context) { }
  @Override public void onFinish(ITestContext context) { }
}
