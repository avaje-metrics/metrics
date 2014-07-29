package org.avaje.metric.core;

import java.util.concurrent.TimeUnit;

import org.avaje.metric.MetricManager;
import org.avaje.metric.TimedEvent;
import org.avaje.metric.TimedMetric;
import org.avaje.metric.ValueStatistics;
import org.junit.Assert;
import org.junit.Test;

public class TimedMetricTest {

  @Test
  public void addEventDuration() {
    
    TimedMetric metric = MetricManager.getTimedMetric(new DefaultMetricName("org", "test", "mytimed"));
    
    Assert.assertEquals("org",metric.getName().getGroup());
    Assert.assertEquals("test",metric.getName().getType());
    Assert.assertEquals("mytimed",metric.getName().getName());
     
    metric.clearStatistics();
    Assert.assertEquals(0, metric.getSuccessStatistics(false).getCount());
    Assert.assertEquals(0, metric.getSuccessStatistics(false).getTotal());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getCount());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getTotal());
    Assert.assertEquals(0, metric.getSuccessStatistics(false).getMean());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getMean());

    metric.addEventDuration(true, TimeUnit.MICROSECONDS.toNanos(1000));
    Assert.assertEquals(1, metric.getSuccessStatistics(false).getCount());
    Assert.assertEquals(1000, metric.getSuccessStatistics(false).getTotal());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getCount());
    
    metric.addEventDuration(true, TimeUnit.MICROSECONDS.toNanos(2000));
    Assert.assertEquals(2, metric.getSuccessStatistics(false).getCount());
    Assert.assertEquals(3000, metric.getSuccessStatistics(false).getTotal());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getCount());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getTotal());

    metric.addEventDuration(false, TimeUnit.MICROSECONDS.toNanos(5000));
    Assert.assertEquals(2, metric.getSuccessStatistics(false).getCount());
    Assert.assertEquals(3000, metric.getSuccessStatistics(false).getTotal());
    Assert.assertEquals(1, metric.getErrorStatistics(false).getCount());
    Assert.assertEquals(5000, metric.getErrorStatistics(false).getTotal());

    Assert.assertEquals(1500, metric.getSuccessStatistics(false).getMean());
    Assert.assertEquals(5000, metric.getErrorStatistics(false).getMean());

    
    Assert.assertTrue(metric.collectStatistics());
    
    ValueStatistics collectedSuccessStatistics = metric.getCollectedSuccessStatistics();
    ValueStatistics collectedErrorStatistics = metric.getCollectedErrorStatistics();

    Assert.assertEquals(2, collectedSuccessStatistics.getCount());
    Assert.assertEquals(3000, collectedSuccessStatistics.getTotal());
    Assert.assertEquals(1, collectedErrorStatistics.getCount());
    Assert.assertEquals(5000, collectedErrorStatistics.getTotal());

    Assert.assertEquals(1500, collectedSuccessStatistics.getMean());
    Assert.assertEquals(5000, collectedErrorStatistics.getMean());


    Assert.assertEquals(0, metric.getSuccessStatistics(false).getCount());
    Assert.assertEquals(0, metric.getSuccessStatistics(false).getTotal());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getCount());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getTotal());
    Assert.assertEquals(0, metric.getSuccessStatistics(false).getMean());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getMean());

  }
  
  @Test
  public void addEventSince() {
      
      TimedMetric metric = MetricManager.getTimedMetric(new DefaultMetricName("org", "test", "mytimed.since"));
      
       
      metric.clearStatistics();
      
      metric.addEventSince(true, System.nanoTime() - 950000);
      ValueStatistics valueStatistics = metric.getSuccessStatistics(false);
      Assert.assertEquals(1, valueStatistics.getCount());
      System.out.println("Should be close to 1000: "+valueStatistics.getTotal());
      Assert.assertTrue(valueStatistics.getTotal() > 0);
      Assert.assertTrue(valueStatistics.getTotal() < 1000);
      Assert.assertEquals(0, metric.getErrorStatistics(false).getCount());
  }
  
  @Test
  public void startEvent() {
    
    TimedMetric metric = MetricManager.getTimedMetric(new DefaultMetricName("org", "test", "mytimed"));
     
    metric.clearStatistics();
    Assert.assertEquals(0, metric.getSuccessStatistics(false).getCount());
    Assert.assertEquals(0, metric.getSuccessStatistics(false).getTotal());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getCount());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getTotal());
    Assert.assertEquals(0, metric.getSuccessStatistics(false).getMean());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getMean());

    TimedEvent startEvent = metric.startEvent();
    startEvent.endWithSuccess();
    
    Assert.assertEquals(1, metric.getSuccessStatistics(false).getCount());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getCount());
    
    startEvent = metric.startEvent();
    startEvent.endWithSuccess();
    Assert.assertEquals(2, metric.getSuccessStatistics(false).getCount());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getCount());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getTotal());

    startEvent = metric.startEvent();
    startEvent.endWithError();
    
    Assert.assertEquals(2, metric.getSuccessStatistics(false).getCount());
    Assert.assertEquals(1, metric.getErrorStatistics(false).getCount());

    
    Assert.assertTrue(metric.collectStatistics());
    
    ValueStatistics collectedSuccessStatistics = metric.getCollectedSuccessStatistics();
    ValueStatistics collectedErrorStatistics = metric.getCollectedErrorStatistics();

    Assert.assertEquals(2, collectedSuccessStatistics.getCount());
    Assert.assertEquals(1, collectedErrorStatistics.getCount());


    Assert.assertEquals(0, metric.getSuccessStatistics(false).getCount());
    Assert.assertEquals(0, metric.getSuccessStatistics(false).getTotal());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getCount());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getTotal());
    Assert.assertEquals(0, metric.getSuccessStatistics(false).getMean());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getMean());

  }
  
  @Test
  public void operationEnd() {
    
    TimedMetric metric = MetricManager.getTimedMetric(new DefaultMetricName("org", "test", "mytimed"));
         
    metric.clearStatistics();
    Assert.assertEquals(0, metric.getSuccessStatistics(false).getCount());
    Assert.assertEquals(0, metric.getSuccessStatistics(false).getTotal());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getCount());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getTotal());
    Assert.assertEquals(0, metric.getSuccessStatistics(false).getMean());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getMean());

    int SUCCESS_OPCODE = 1;
    int ERROR_OPCODE = 191;
    
    metric.operationEnd(TimeUnit.MICROSECONDS.toNanos(1000), SUCCESS_OPCODE);
    Assert.assertEquals(1, metric.getSuccessStatistics(false).getCount());
    Assert.assertEquals(1000, metric.getSuccessStatistics(false).getTotal());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getCount());
    
    metric.operationEnd(TimeUnit.MICROSECONDS.toNanos(2000), SUCCESS_OPCODE);
    Assert.assertEquals(2, metric.getSuccessStatistics(false).getCount());
    Assert.assertEquals(3000, metric.getSuccessStatistics(false).getTotal());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getCount());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getTotal());

    metric.operationEnd(TimeUnit.MICROSECONDS.toNanos(5000), ERROR_OPCODE);
    Assert.assertEquals(2, metric.getSuccessStatistics(false).getCount());
    Assert.assertEquals(3000, metric.getSuccessStatistics(false).getTotal());
    Assert.assertEquals(1, metric.getErrorStatistics(false).getCount());
    Assert.assertEquals(5000, metric.getErrorStatistics(false).getTotal());

    Assert.assertEquals(1500, metric.getSuccessStatistics(false).getMean());
    Assert.assertEquals(5000, metric.getErrorStatistics(false).getMean());

    
    Assert.assertTrue(metric.collectStatistics());
    
    ValueStatistics collectedSuccessStatistics = metric.getCollectedSuccessStatistics();
    ValueStatistics collectedErrorStatistics = metric.getCollectedErrorStatistics();

    Assert.assertEquals(2, collectedSuccessStatistics.getCount());
    Assert.assertEquals(3000, collectedSuccessStatistics.getTotal());
    Assert.assertEquals(1, collectedErrorStatistics.getCount());
    Assert.assertEquals(5000, collectedErrorStatistics.getTotal());

    Assert.assertEquals(1500, collectedSuccessStatistics.getMean());
    Assert.assertEquals(5000, collectedErrorStatistics.getMean());


    Assert.assertEquals(0, metric.getSuccessStatistics(false).getCount());
    Assert.assertEquals(0, metric.getSuccessStatistics(false).getTotal());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getCount());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getTotal());
    Assert.assertEquals(0, metric.getSuccessStatistics(false).getMean());
    Assert.assertEquals(0, metric.getErrorStatistics(false).getMean());

  }
}