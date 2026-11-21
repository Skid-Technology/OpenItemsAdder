package dev.lone.itemsadder.util.serial;

public class SerialParseException extends SerialException {
   private static final long serialVersionUID = 1616151763072450476L;
   private static final String bRx = "While parsing a protocol message, the input ended unexpectedly in the middle of a field.  This could mean either than the input has been truncated or that an embedded message misreported its own length.";

   public SerialParseException(String var1) {
      super(var1);
   }

   public SerialParseException(String var1, Throwable var2) {
      super(var1, var2);
   }

   static SerialParseException wrap(Throwable var0) {
      return new SerialParseException(
         "While parsing a protocol message, the input ended unexpectedly in the middle of a field.  This could mean either than the input has been truncated or that an embedded message misreported its own length.",
         var0
      );
   }

   static SerialParseException afj() {
      return new SerialParseException(
         "While parsing a protocol message, the input ended unexpectedly in the middle of a field.  This could mean either than the input has been truncated or that an embedded message misreported its own length."
      );
   }

   static SerialParseException truncatedMessage() {
      return new SerialParseException("CodedInput encountered an embedded string or bytes that misreported its size.");
   }

   static SerialParseException negativeSize() {
      return new SerialParseException("CodedInput encountered an embedded string or message which claimed to have negative size.");
   }

   static SerialParseException malformedVarint() {
      return new SerialParseException("CodedInput encountered a malformed varint.");
   }

   static SerialParseException invalidTag() {
      return new SerialParseException("Protocol message contained an invalid tag (zero).");
   }

   static SerialParseException mismatchedTag() {
      return new SerialParseException("Protocol message end-group tag did not match expected tag.");
   }

   static SerialParseException invalidWireType() {
      return new SerialParseException("Protocol message tag had invalid wire type.");
   }

   static SerialParseException afq() {
      return new SerialParseException(
         "Protocol message had too many levels of nesting.  May be malicious.  Use CodedInput.setRecursionLimit() to increase the depth limit."
      );
   }

   static SerialParseException afr() {
      return new SerialParseException("Protocol message was too large.  May be malicious.  Use CodedInput.setSizeLimit() to increase the size limit.");
   }
}
