package org.avaje.metric.util;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class LikeMatcherTest {

  @Test
  public void testStartsWith() throws Exception {

    LikeMatcher startsWith = new LikeMatcher("web.*");
    assertTrue(startsWith.matches("Web.foo"));
    assertTrue(startsWith.matches("Web."));
    assertTrue(startsWith.matches("web.foo"));
    assertFalse(startsWith.matches("xweb.foo"));
    assertFalse(startsWith.matches("webx.foo"));
  }

  @Test
  public void testEndsWith() throws Exception {

    LikeMatcher startsWith = new LikeMatcher("*Resource");
    assertTrue(startsWith.matches("Web.foo.SomeResource"));
    assertTrue(startsWith.matches("Web.Resource"));
    assertTrue(startsWith.matches("Resource"));
    assertTrue(startsWith.matches("SomeResource"));
    assertTrue(startsWith.matches("resource"));
    assertTrue(startsWith.matches("someresource"));
    assertTrue(startsWith.matches("some_resource"));

    assertFalse(startsWith.matches("xweb.fooResources"));
    assertFalse(startsWith.matches("webx.fooResourc"));
  }

  @Test
  public void testContains() throws Exception {

    LikeMatcher startsWith = new LikeMatcher("*Resource*");
    assertTrue(startsWith.matches("Web.foo.SomeResource"));
    assertTrue(startsWith.matches("Web.Resource"));
    assertTrue(startsWith.matches("Resource"));
    assertTrue(startsWith.matches("SomeResource"));
    assertTrue(startsWith.matches("resource"));
    assertTrue(startsWith.matches("someresource"));
    assertTrue(startsWith.matches("some_resource"));

    assertTrue(startsWith.matches("xweb.fooResources"));
    assertTrue(startsWith.matches("xweb.fooResourceBar"));
    assertTrue(startsWith.matches("xweb.fooresourcebar"));
    assertFalse(startsWith.matches("webx.fooResourc"));
  }

  @Test
  public void testStartsAndContains() throws Exception {

    LikeMatcher like = new LikeMatcher("web.*Customer*");
    assertTrue(like.matches("Web.foo.CustomerResource"));
    assertTrue(like.matches("web.Customer"));
    assertTrue(like.matches("Web.CustomerService"));
    assertTrue(like.matches("Web.CustomerService"));
    assertTrue(like.matches("web.Customer"));
    assertTrue(like.matches("Web.Customer.Service"));

    assertFalse(like.matches("webCustomerService"));
    assertFalse(like.matches("srv.CustomerService"));
    assertFalse(like.matches("Customer"));

    assertFalse(like.matches("webx.fooCustomer"));
  }

  @Test
  public void testStartsContainsEnds() throws Exception {

    LikeMatcher like = new LikeMatcher("web.*customer*resource");
    assertTrue(like.matches("web.foo.CustomerResource"));
    assertTrue(like.matches("web.CustomerResource"));
    assertFalse(like.matches("webCustomerResource"));
    assertTrue(like.matches("Web.foo.CustomerResource"));
    assertFalse(like.matches("web.Customer"));
    assertFalse(like.matches("Web.CustomerService"));
    assertFalse(like.matches("eb.foo.CustomerResource"));
    assertFalse(like.matches("zeb.foo.CustomerResource"));
    assertFalse(like.matches("xweb.foo.CustomerResource"));
    assertFalse(like.matches("webCustomeResource"));
    assertFalse(like.matches("webCustomerResourc"));

    assertFalse(like.matches("webCustomerService"));
    assertFalse(like.matches("srv.CustomerService"));
    assertFalse(like.matches("Customer"));

    assertFalse(like.matches("webx.fooCustomer"));
  }
}