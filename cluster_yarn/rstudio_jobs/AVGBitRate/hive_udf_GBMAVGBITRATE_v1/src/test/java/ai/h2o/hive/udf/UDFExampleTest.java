package ai.h2o.hive.udf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF.DeferredJavaObject;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF.DeferredObject;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.JavaDoubleObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import org.junit.Test;
import junit.framework.Assert;

public class UDFExampleTest {
  @Test public void testUDFReturnsCorrectValues() throws HiveException {
    // set up the models we need
    ScoreDataUDFGBMAVGM17 example = new ScoreDataUDFGBMAVGM17();

    //From the test data set: "CAPSULE", "AGE", "RACE", "DPROS", "DCAPS", "PSA", "GLEASON"
    ObjectInspector CAPSULE_OI = PrimitiveObjectInspectorFactory.javaIntObjectInspector;
    ObjectInspector AGE_OI = PrimitiveObjectInspectorFactory.javaIntObjectInspector;
    ObjectInspector RACE_OI = PrimitiveObjectInspectorFactory.javaIntObjectInspector;
    ObjectInspector DPROS_OI = PrimitiveObjectInspectorFactory.javaStringObjectInspector;
    ObjectInspector DCAPS_OI = PrimitiveObjectInspectorFactory.javaStringObjectInspector;
    ObjectInspector PSA_OI = PrimitiveObjectInspectorFactory.javaDoubleObjectInspector;
    ObjectInspector GLEASON_OI = PrimitiveObjectInspectorFactory.javaIntObjectInspector;
    JavaDoubleObjectInspector resultInspector = (JavaDoubleObjectInspector) example.initialize(new ObjectInspector[]{CAPSULE_OI,AGE_OI,RACE_OI,DPROS_OI,DCAPS_OI,PSA_OI,GLEASON_OI});
    // test our results
    // Data from first line of test file: 0, 65, 1, 2, 1, 1.4, 6
    Object result1 =
            example.evaluate(
                    new DeferredObject[]{
                            new DeferredJavaObject(0), //CAPSULE
                            new DeferredJavaObject(65), // AGE
                            new DeferredJavaObject(1), // RACE
                            new DeferredJavaObject(2), //DPROS
                            new DeferredJavaObject(1), // DCAPS
                            new DeferredJavaObject(1.4), // PSA
                            new DeferredJavaObject(6)}); // GLEASON
    double tolerance = 1e-8;
    Assert.assertEquals(0.0D, resultInspector.get(result1), tolerance);
    // Wrong number of arguments

    try {
      example.evaluate(new DeferredObject[]{new DeferredJavaObject(0), new DeferredJavaObject(65)});
      Assert.fail();
    } catch (UDFArgumentException expected) { Assert.assertTrue(true);}
    // Arguments are null
    Object result3 = example.evaluate(new DeferredObject[]{
            new DeferredJavaObject(null), new DeferredJavaObject(null), // CAPSULE, AGE
            new DeferredJavaObject(null), new DeferredJavaObject(null), // RACE, DPROS
            new DeferredJavaObject(null), new DeferredJavaObject(null), // DCAPS, PSA
            new DeferredJavaObject(null)}); // GLEASON
    Assert.assertNull(result3);

  }
}


/*

$ mvn package -Dmaven.test.skip=true
[INFO] Scanning for projects...
[WARNING]
[WARNING] Some problems were encountered while building the effective model for ai.h2o.hive.udf:ScoreDataUDFGBMAVGM17:jar:1.0-SNAPSHOT
[WARNING] 'dependencies.dependency.systemPath' for water:h2o-genmodel:jar should not point at files within the project directory, ${project.basedir}/localjars/h2o-genmodel.jar will be unresolvable by dependent projects @ line 37, column 37
[WARNING]
[WARNING] It is highly recommended to fix these problems because they threaten the stability of your build.
[WARNING]
[WARNING] For this reason, future Maven versions might no longer support building such malformed projects.
[WARNING]
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building ScoreDataUDFGBMAVGM17 1.0-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[WARNING] The POM for org.apache.hadoop:hadoop-common:jar:0.22.0-SNAPSHOT is missing, no dependency information available
[INFO]
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ ScoreDataUDFGBMAVGM17 ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory /Users/joao.cerqueira/gitstash/bda/bda_exadata_samples/SAMPLE_IXPBDAOPTA01_TOOLS/jpac-sparklyr/rstudio_jobs/hive_udf_pojo_template/src/main/resources
[INFO]
[INFO] --- maven-compiler-plugin:2.3.2:compile (default-compile) @ ScoreDataUDFGBMAVGM17 ---
[INFO] Nothing to compile - all classes are up to date
[INFO]
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ ScoreDataUDFGBMAVGM17 ---
[INFO] Not copying test resources
[INFO]
[INFO] --- maven-compiler-plugin:2.3.2:testCompile (default-testCompile) @ ScoreDataUDFGBMAVGM17 ---
[INFO] Not compiling test sources
[INFO]
[INFO] --- maven-surefire-plugin:2.12.4:test (default-test) @ ScoreDataUDFGBMAVGM17 ---
[INFO] Tests are skipped.
[INFO]
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ ScoreDataUDFGBMAVGM17 ---
[INFO] Building jar: /Users/joao.cerqueira/gitstash/bda/bda_exadata_samples/SAMPLE_IXPBDAOPTA01_TOOLS/jpac-sparklyr/rstudio_jobs/hive_udf_pojo_template/target/ScoreDataUDFGBMAVGM17-1.0-SNAPSHOT.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 4.538 s
[INFO] Finished at: 2018-06-01T15:20:08+01:00
[INFO] Final Memory: 20M/358M
[INFO] ------------------------------------------------------------------------


$ mvn package

-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Forking command line: /bin/sh -c cd /Users/joao.cerqueira/gitstash/bda/bda_exadata_samples/SAMPLE_IXPBDAOPTA01_TOOLS/jpac-sparklyr/rstudio_jobs/hive_udf_pojo_template && /Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/bin/java -jar /Users/joao.cerqueira/gitstash/bda/bda_exadata_samples/SAMPLE_IXPBDAOPTA01_TOOLS/jpac-sparklyr/rstudio_jobs/hive_udf_pojo_template/target/surefire/surefirebooter8454842685365683756.jar /Users/joao.cerqueira/gitstash/bda/bda_exadata_samples/SAMPLE_IXPBDAOPTA01_TOOLS/jpac-sparklyr/rstudio_jobs/hive_udf_pojo_template/target/surefire/surefire6070441649139223708tmp /Users/joao.cerqueira/gitstash/bda/bda_exadata_samples/SAMPLE_IXPBDAOPTA01_TOOLS/jpac-sparklyr/rstudio_jobs/hive_udf_pojo_template/target/surefire/surefire_02256982326777821649tmp
Running ai.h2o.hive.udf.UDFExampleTest
log4j:WARN No appenders could be found for logger (org.apache.hadoop.util.Shell).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
Tests run: 1, Failures: 0, Errors: 1, Skipped: 0, Time elapsed: 0.417 sec <<< FAILURE!
testUDFReturnsCorrectValues(ai.h2o.hive.udf.UDFExampleTest)  Time elapsed: 0.361 sec  <<< ERROR!
java.lang.NoClassDefFoundError: Could not initialize class org.apache.hadoop.hive.ql.ErrorMsg
	at org.apache.hadoop.hive.ql.metadata.HiveException.<init>(HiveException.java:31)
	at org.apache.hadoop.hive.ql.parse.SemanticException.<init>(SemanticException.java:37)
	at org.apache.hadoop.hive.ql.exec.UDFArgumentException.<init>(UDFArgumentException.java:42)
	at ai.h2o.hive.udf.ScoreDataUDFGBMAVGM17.evaluate(ScoreDataUDFGBMAVGM17.java:103)
	at ai.h2o.hive.udf.UDFExampleTest.testUDFReturnsCorrectValues(UDFExampleTest.java:34)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:44)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:15)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:41)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:20)
	at org.junit.runners.BlockJUnit4ClassRunner.runNotIgnored(BlockJUnit4ClassRunner.java:79)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:71)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:49)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:193)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:52)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:191)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:42)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:184)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:236)
	at org.apache.maven.surefire.junit4.JUnit4Provider.execute(JUnit4Provider.java:252)
	at org.apache.maven.surefire.junit4.JUnit4Provider.executeTestSet(JUnit4Provider.java:141)
	at org.apache.maven.surefire.junit4.JUnit4Provider.invoke(JUnit4Provider.java:112)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.apache.maven.surefire.util.ReflectionUtils.invokeMethodWithArray(ReflectionUtils.java:189)
	at org.apache.maven.surefire.booter.ProviderFactory$ProviderProxy.invoke(ProviderFactory.java:165)
	at org.apache.maven.surefire.booter.ProviderFactory.invokeProvider(ProviderFactory.java:85)
	at org.apache.maven.surefire.booter.ForkedBooter.runSuitesInProcess(ForkedBooter.java:115)
	at org.apache.maven.surefire.booter.ForkedBooter.main(ForkedBooter.java:75)


Results :

Tests in error:
  testUDFReturnsCorrectValues(ai.h2o.hive.udf.UDFExampleTest): Could not initialize class org.apache.hadoop.hive.ql.ErrorMsg

Tests run: 1, Failures: 0, Errors: 1, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 4.623 s

*/