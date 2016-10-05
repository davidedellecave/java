package ddc.util;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ddc.dao._LiteFields;

public class ParseUtil {
	private static Logger logger = Logger.getLogger(ParseUtil.class);

	public static Double parseDouble(Object value) {
		try {
			return Double.parseDouble(String.valueOf(value));
		} catch(NumberFormatException e) {
			System.err.println("parseDouble() Exception:[" + e.getMessage() + "]");
			return (double)0;
		}
	}

	public static Double parseDouble(Object o, double defaultValue) {
		try {
			return Double.parseDouble(String.valueOf(o));
		} catch(NumberFormatException e) {
			return defaultValue;
		}
	}
	
	public static Float parseFloat(Object value) {
		try {
			return Float.parseFloat(String.valueOf(value));
		} catch(NumberFormatException e) {
			System.err.println("parseFloat() Exception:[" + e.getMessage() + "]");
			return (float)0;
		}
	}
	
	public static Long parseLong(Object value) {
		try {
			return Long.parseLong(String.valueOf(value));
		} catch(NumberFormatException e) {
			System.err.println("parseLong() Exception:[" + e.getMessage() + "]");
			return (long)0;
		}
	}
	
	public static String parseString(Object o, String defaultValue) {
		if (o == null)
			return defaultValue;
		else
			return o.toString();
	}

	public static char parseChar(String o, char defaultValue) {
		try {
			if (o == null || o.length() != 1)
				return defaultValue;
			return o.charAt(0);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static int parseInt(Object n, int defaultValue) {
		return parseInt(parseString(n, String.valueOf(defaultValue)), defaultValue);
	}

	public static int parseInt(String n, int defaultValue) {
		try {
			return Integer.parseInt(n);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static long parseLong(Object n, long defaultValue) {
		return parseLong(parseString(n, String.valueOf(defaultValue)), defaultValue);
	}

	public static long parseLong(String n, long defaultValue) {
		try {
			return Long.parseLong(n);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static Object parseEnum(String enumValue, Enum defaultValue) {
		try {
			return Enum.valueOf(defaultValue.getClass(), enumValue.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static boolean parseBoolean(Object o, boolean defaultValue) {
		return parseBoolean(parseString(o, String.valueOf(false)), false);
	}

	public static boolean parseBoolean(String s, boolean defaultValue) {
		try {
			return Boolean.parseBoolean(s);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	// public static DateTime parseDateTime(String s, DateTime defaultValue) {
	// try {
	// return DateTime.Parse(s);
	// } catch {
	// return defaultValue;
	// }
	// }

	// public static DateTime parseDateTime(Object s, DateTime defaultValue) {
	// return parseDateTime(parseString(s, defaultValue.ToString()),
	// defaultValue);
	// }

	/**
	 * node.getChildNodes()
	 * 
	 * @param node
	 * @return
	 */
	public static _LiteFields parseXmlNode(Node node) {
		_LiteFields res = new _LiteFields();
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node child = list.item(i);
			res.put(child.getNodeName(), child.getTextContent());
		}
		return res;
	}

	// / <summary>
	// / Convert NameValueCollection as plain text using the format:
	// key<equal>value<separator>key<equal>value<separator>...
	// / If NameValueCollection contains array the format is:
	// key[0]<equal>value<separator>key[1]<equal>value<separator>...
	// / </summary>
	// / <param name="nvc">NameValueCollection</param>
	// / <param name="equal">separator between key and value</param>
	// / <param name="separator">separator among NaveValue pair</param>
	// / <returns></returns>
	// public static String plainNameValueCollection(Map nvc, String equal,
	// String separator) {
	// StringBuilder sb = new StringBuilder();
	// for(String key : nvc.AllKeys) {
	// String[] values = nvc.get(key);
	// if (values.length== 1) {
	// sb.append(key).append(equal).append(values[0]).append(separator);
	// } else {
	// for (int i=0; i<values.length; i++) {
	// sb.append(key).append("[" + i +
	// "]").append(equal).append(values[i]).append(separator);
	// }
	// }
	// }
	// return StringUtils.removeLast(sb.toString(), separator);
	// }

	// @Deprecated()//("This properties should not be used because moved to another package. Use CryptoUtils.base64Decode instead",
	// false)]
	// public static String base64Decode(String data) {
	// try {
	// System.Text.UTF8Encoding encoder = new System.Text.UTF8Encoding();/*utf8
	// ascii 8059-1(latin)*/
	// System.Text.Decoder utf8Decode = encoder.GetDecoder();
	//
	// byte[] todecode_byte = Convert.FromBase64String(data);
	// int charCount = utf8Decode.GetCharCount(todecode_byte, 0,
	// todecode_byte.length);
	// char[] decoded_char = new char[charCount];
	// utf8Decode.GetChars(todecode_byte, 0, todecode_byte.length, decoded_char,
	// 0);
	// String result = new String(decoded_char);
	// return result;
	// } catch (Exception e) {
	// throw new Exception("Error in base64Decode" + e.getMessage());
	// }
	// }

	// @Deprecated()//("This properties should not be used because moved to another package. Use CryptoUtils.base64Encode instead",
	// false)]
	// public static String base64Encode(String data) {
	// try {
	// byte[] encData_byte = new byte[data.length()];
	// encData_byte = System.Text.Encoding.UTF8.GetBytes(data);
	// return base64Encode(encData_byte);
	// } catch (Exception e) {
	// throw new Exception("Error in base64Encode" + e.getMessage());
	// }
	// }

	// @Deprecated()//("This properties should not be used because moved to another package. Use CryptoUtils.base64Encode instead",
	// false)]
	// public static String base64Encode(byte[] data) {
	// try {
	// return Convert.ToBase64String(data);
	// } catch (Exception e) {
	// throw new Exception("Error in base64Encode" + e.getMessage());
	// }
	// }
	//
	// public static String splitAndClean(String source, int leftLength, String
	// separator, int rigthLength) {
	// return ParseUtils.removeZeros(ParseUtils.splitNumber(source, leftLength,
	// separator, rigthLength),separator);
	// }

	// / <summary>
	// / Split continuos digit Example "12345678012345678AA00"
	// / using the "separator" resulting as 12345678012345678,AA
	// / </summary>
	// / <param name="currency"></param>
	// / <param name="leftLength"></param>
	// / <param name="separator"></param>
	// / <param name="rigthLength"></param>
	// / <returns></returns>
	public static String splitNumber(String source, int leftLength, String separator, int rigthLength) {
		String left = "";
		String right = "";
		if (source.length() < leftLength + rigthLength) {
			logger.warn("splitNumber source is less than " + leftLength + rigthLength + " length:[" + source.length() + "]");
		}
		if (source.length() >= leftLength) {
			left = source.substring(0, leftLength);
		}
		if (source.length() >= leftLength + rigthLength) {
			right = source.substring(leftLength, rigthLength);
		}
		String value = left;
		if (right.length() > 0) {
			value = left + separator + right;
		}
		logger.debug("splitNumber source:[" + source + "] target:[" + value + "]");
		return value;
	}

	// / <summary>
	// / Format number separated by 'separator' padding to left and right using
	// zeros 0
	// / If left/right number is greater than left/right padding the number is
	// not truncated
	// / </summary>
	// / <param name="source"></param>
	// / <param name="padLeftLength"></param>
	// / <param name="separator"></param>
	// / <param name="padRightLength"></param>
	// / <returns></returns>

	// public static String padNumber(String source, int padLeftLength, String
	// separator, int padRightLength) {
	// String value = source;
	// if (source.IndexOf(separator) >= 0) {
	// String left = value.Substring(0, value.IndexOf(separator));
	// String right = value.Substring(value.IndexOf(separator) + 1);
	// try {
	// left = long.Parse(left).ToString();
	// right = long.Parse(right).ToString();
	// } catch (Exception e) {
	// logger.error("padNumber source:[" + source + "] exception:[" + e.Message
	// + "]");
	// };
	// left = left.PadLeft(padLeftLength, '0');
	// right = right.PadRight(padRightLength, '0');
	// value = left + separator + right;
	// }
	// logger.debug("padNumber source:[" + source + "] target:[" + value + "]");
	// return value;
	// }

	// public static String removeZeros(String source, String separator) {
	// String value = source;
	// if (source.IndexOf(separator)>= 0) {
	// String left = value.Substring(0, value.IndexOf(separator));
	// String right = value.Substring(value.IndexOf(separator) + 1);
	// try {
	// left = long.Parse(left).ToString();
	// right = long.Parse(right).ToString();
	// } catch (Exception e) {
	// logger.error("removeZeros source:[" + source + "] exception:[" +
	// e.Message + "]");
	// };
	// left = left.TrimStart('0');
	// right = right.TrimEnd('0');
	// if (right.Length == 0) right = "00";
	// if (right.Length == 1) right += "0";
	// value = left + separator + right;
	// }
	// logger.debug("removeZeros source:[" + source + "] target:[" + value +
	// "]");
	// return value;
	// }
}
