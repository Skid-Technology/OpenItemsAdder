package dev.lone.itemsadder.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

public final class LogMessageFilter extends AbstractFilter {
   private static final String TEST_PASSED_MESSAGE = "Test passed";
   private static final String TEST_FAILED_MESSAGE = "Test failed";

   public Result filter(Logger logger, Level level, Marker marker, Message message, Throwable throwable) {
      return message.getFormat().equals("Test passed") ? Result.DENY : Result.NEUTRAL;
   }

   public Result filter(LogEvent e) {
      String var2 = e.getMessage().getFormat();
      return "Test passed".equals(var2) ? Result.DENY : Result.NEUTRAL;
   }

   public Result filter(
      Logger logger, Level level, Marker marker, String msg, Object o, Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7
   ) {
      return msg.startsWith("Tried to load unrecognized recipe: ") ? Result.DENY : Result.NEUTRAL;
   }

   public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
      return Result.NEUTRAL;
   }

   public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
      return msg.startsWith("Tried to load unrecognized recipe: ") ? Result.DENY : Result.NEUTRAL;
   }

   public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0) {
      return msg.startsWith("Tried to load unrecognized recipe: ") ? Result.DENY : Result.NEUTRAL;
   }

   public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1) {
      return msg.startsWith("Tried to load unrecognized recipe: ") ? Result.DENY : Result.NEUTRAL;
   }

   public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2) {
      return msg.startsWith("Tried to load unrecognized recipe: ") ? Result.DENY : Result.NEUTRAL;
   }

   public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3) {
      return msg.startsWith("Tried to load unrecognized recipe: ") ? Result.DENY : Result.NEUTRAL;
   }

   public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4) {
      return msg.startsWith("Tried to load unrecognized recipe: ") ? Result.DENY : Result.NEUTRAL;
   }

   public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
      return msg.startsWith("Tried to load unrecognized recipe: ") ? Result.DENY : Result.NEUTRAL;
   }

   public Result filter(Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
      return msg.startsWith("Tried to load unrecognized recipe: ") ? Result.DENY : Result.NEUTRAL;
   }

   public Result filter(
      Logger logger, Level level, Marker marker, String msg, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8
   ) {
      return msg.startsWith("Tried to load unrecognized recipe: ") ? Result.DENY : Result.NEUTRAL;
   }

   public Result filter(
      Logger logger,
      Level level,
      Marker marker,
      String msg,
      Object p0,
      Object p1,
      Object p2,
      Object p3,
      Object p4,
      Object p5,
      Object p6,
      Object p7,
      Object p8,
      Object p9
   ) {
      return msg.startsWith("Tried to load unrecognized recipe: ") ? Result.DENY : Result.NEUTRAL;
   }
}
