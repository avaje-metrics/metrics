package org.avaje.metric;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.avaje.metric.core.DefaultMetricManager;

/**
 * Manages the metrics creation and registration.
 * <p>
 * Provides methods to allow agents to go through the registered metrics and
 * gather/report the statistics.
 * </p>
 */
public class MetricManager {

  private static final DefaultMetricManager mgr = new DefaultMetricManager();

  /**
   * Force the statistics to be updated.
   * <p>
   * Typically this is not called but left to the underlying MetricManager to
   * update the statistics periodically using a background thread.
   * </p>
   */
  public static void updateStatistics() {
    mgr.updateStatistics();
  }

  /**
   * Return a MetricNameCache for the given class.
   * <p>
   * The MetricNameCache can be used to derive MetricName objects dynamically
   * with relatively less overhead.
   * </p>
   */
  public static MetricNameCache getMetricNameCache(Class<?> klass) {
    return mgr.getMetricNameCache(klass);
  }

  /**
   * Return a MetricNameCache for a given base metric name.
   * <p>
   * The MetricNameCache can be used to derive MetricName objects dynamically
   * with relatively less overhead.
   * </p>
   */
  public static MetricNameCache getMetricNameCache(MetricName baseName) {
    return mgr.getMetricNameCache(baseName);
  }

  /**
   * Return a TimedMetric using the Class, name and scope to derive the
   * MetricName.
   */
  public static TimedMetric getTimedMetric(Class<?> cls, String eventName, String scope, TimeUnit rateUnit) {
    return getTimedMetric(new MetricName(cls, eventName, scope), rateUnit, null);
  }

  /**
   * Return a TimedMetric given its metricName.
   */
  public static TimedMetric getTimedMetric(MetricName metricName) {
    return getTimedMetric(metricName, null, null);
  }

  /**
   * Return a CounterMetric using the Class and name to derive the MetricName.
   */
  public static CounterMetric getCounterMetric(Class<?> cls, String eventName) {
    return getCounterMetric(cls, eventName, null);
  }

  /**
   * Return a CounterMetric using the Class, name and scope to derive the
   * MetricName.
   */
  public static CounterMetric getCounterMetric(Class<?> cls, String eventName, String scope) {
    return mgr.getCounterMetric(new MetricName(cls, eventName, scope));
  }

  /**
   * Return a CounterMetric given the name.
   */
  public static CounterMetric getEventMetric(MetricName name) {
    return mgr.getCounterMetric(name);
  }

  /**
   * Return a ValueMetric given the name, rateUnit.
   */
  public static ValueMetric getValueMetric(MetricName name, TimeUnit rateUnit) {
    return mgr.getValueMetric(name, rateUnit);
  }

  /**
   * Return a TimedMetric given the name, rateUnit and clock.
   */
  public static TimedMetric getTimedMetric(MetricName name, TimeUnit rateUnit, Clock clock) {
    return mgr.getTimedMetric(name, rateUnit, clock);
  }

  /**
   * Return a TimedMetricGroup.
   * <p>
   * This is used when a group of TimedMetric's have a common base name,
   * rateUnit and clock. These TimedMetric's only differ by the name (all share
   * the same group and type etc).
   * </p>
   * 
   * @param baseName
   *          the common part of the metric name
   * @param rateUnit
   *          the rateUnit for all the TimedMetric's
   * @param clock
   *          the clock to use
   * 
   * @return the TimedMetricGroup used to create TimedMetric's that have a
   *         common base name.
   */
  public static TimedMetricGroup getTimedMetricGroup(MetricName baseName, TimeUnit rateUnit, Clock clock) {
    return new TimedMetricGroup(baseName, rateUnit, clock);
  }

  /**
   * Return a TimedMetricGroup with a common group and type name.
   * <p>
   * This uses the default clock.
   * </p>
   * @param group
   *          the common group name
   * @param type
   *          the common type name
   * @param rateUnit
   *          the rateUnit used
   * @return the TimedMetricGroup used to create TimedMetric's that have a
   *         common base name.
   */
  public static TimedMetricGroup getTimedMetricGroup(String group, String type, TimeUnit rateUnit) {
    return new TimedMetricGroup(new MetricName(group, type, "dummy"), rateUnit, Clock.defaultClock());
  }

  /**
   * Clear the registered metrics.
   */
  protected static void clear() {
    mgr.clear();
  }

  /**
   * Return all the metrics registered.
   */
  public static Collection<Metric> getAllMetrics() {
    return mgr.getAllMetrics();
  }

  /**
   * Visit all the metrics.
   */
  public static void visitAll(MetricVisitor visitor) {

    Collection<Metric> metrics = mgr.getAllMetrics();
    for (Metric metric : metrics) {
      metric.visit(visitor);
    }
  }

  /**
   * Visit all the metrics that match the matcher.
   */
  public static void visit(MetricMatcher matcher, MetricVisitor visitor) {

    Collection<Metric> metrics = mgr.getAllMetrics();
    for (Metric metric : metrics) {
      if (matcher.isMatch(metric)) {
        metric.visit(visitor);
      }
    }
  }

}
