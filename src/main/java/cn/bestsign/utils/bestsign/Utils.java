package cn.bestsign.utils.bestsign;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {



	public static byte[] getFileContent(String file) throws IOException {
		File f = new File(file);
		FileInputStream s = new FileInputStream(f);
		byte[] buffer = new byte[(int) f.length()];
		try {
			s.read(buffer);
		} catch (IOException e) {
			throw e;
		} finally {
			s.close();
		}
		return buffer;
	}

	/**
	 * 写文件数据
	 *
	 * @param filePath
	 * @param data
	 * @throws IOException
	 */
	public static void putFileContent(final String filePath, final byte[] data) throws IOException {
		FileOutputStream s = new FileOutputStream(filePath);
		try {
			s.write(data);
			s.flush();
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			try {
				s.close();
			}
			catch (IOException e) {
				;
			}
		}
	}
	
	public static byte[] getResource(String path) throws IOException {
        InputStream inputStream = Utils.class.getResourceAsStream(path);
        if (inputStream == null) {
            String path2 = "/resources" + path; // resources目录, 兼容jar包
            inputStream = Utils.class.getResourceAsStream(path2);
            if (inputStream == null) {
                return null;
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read = 0;

        try {
            for (;;) {
                read = inputStream.read(buffer);
                if (read < 1) {
                    break;
                }
                outputStream.write(buffer, 0, read);
            }
        }
        catch (IOException e) {
            throw e;
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            try {
                outputStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return outputStream.toByteArray();
    }
	
	/**
	 * 判断一个对象是不是空. (null, "", "0", "false", 0, false 这些都是空)
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(final Object value) {
		if (null == value || "".equals(value) || "0".equals(value) || "0.0".equals(String.valueOf(value))) {
			return true;
		}
		
		//a. 转成字符串检测
		String test = String.valueOf(value).replace(" ", "").toLowerCase();
		while (true) {
			int a = test.indexOf("00");
			if (a < 0) {
				break;
			}
			test = test.replace("00", "0");
		}
		if ("".equals(test) || "false".equals(test) || "0".equals(test) || "0.0".equals(test)) {
			return true;
		}
		
		Class<?> clazz = value.getClass();
		
		//b. 检测数组
		if (clazz.isArray()) {
			return java.lang.reflect.Array.getLength(value) == 0;
		}
		
		//c. 检测method
		String[] methodTestList = {"size", "length"};
		for (int i = 0; i < methodTestList.length; i++) {
			String testName = methodTestList[i];
			Method method;
			try {
				method = clazz.getMethod(testName);
			} catch (NoSuchMethodException e) {
				continue;
			} catch (SecurityException e) {
				continue;
			}
			try {
				test = String.valueOf(method.invoke(value));
			} catch (IllegalAccessException e) {
				continue;
			} catch (IllegalArgumentException e) {
				continue;
			} catch (InvocationTargetException e) {
				continue;
			}
			return "0".equals(test);
		}
		
		return false;
	}

	public static boolean isBlank(final String value) {
		return (value == null || value.trim().length() < 1);
	}
	
	/**
	 * isInteger
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isInteger(final String value) {
		if (value == null || value.length() < 1) {
			return false;
		}
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * isLong
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isLong(final String value) {
		if (value == null || value.length() < 1) {
			return false;
		}
		try {
			Long.parseLong(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * isFloat
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isFloat(final String value) {
		if (value == null || value.length() < 1) {
			return false;
		}
		try {
			Float.parseFloat(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * isEmail
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmail(final String value) {
		if (value == null || value.length() < 1) {
			return false;
		}
		
		int a = value.indexOf("@");
		if (a <= 0) {
			return false;
		}
		String[] items = value.split("@");
		if (items.length != 2) {
			return false;
		}
		
		String name = items[0];
		Pattern pattern = Pattern.compile("[A-Za-z0-9_\\-\\.]+");
		Matcher matcher = pattern.matcher(name);
		if (!matcher.matches()) {
			return false;
		}
		
		String domain = items[1];
		if (!isDomain(domain)) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * isPhone
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isPhone(final String value) {
		if (value == null || value.length() < 1) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]+");
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
	
	/**
	 * isMobile
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isMobile(final String value) {
		if (!isPhone(value)) {
			return false;
		}
		if (value.length() != 11) {
			return false;
		}
		String s = value.substring(0, 2);
		Pattern pattern = Pattern.compile("(13|14|15|17|18)");
		Matcher matcher = pattern.matcher(s);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}
	
	/**
	 * isQQ (6 ~ 20位纯数字)
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isQQ(final String value) {
		if (value == null || value.length() < 6 || value.length() > 20) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]+");
		Matcher matcher = pattern.matcher(value);
		if (!matcher.matches()) {
			return false;
		}
		if (value.substring(0, 1).equals("0")) {
			return false;
		}
		return true;
	}
	
	/**
	 * 是否是域名
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isDomain(final String value) {
		if (value == null || value.length() < 1) {
			return false;
		}
		String[] domainItems = value.split("\\.");
		if (domainItems.length < 2) {
			return false;
		}
		Pattern pattern = Pattern.compile("[A-Za-z0-9\\-]+");
		Matcher matcher;
		for (int i = 0; i < domainItems.length; i++) {
			String s = domainItems[i];
			if (s.length() < 1) {
				return false;
			}
			matcher = pattern.matcher(s);
			if (!matcher.matches()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 是否是ip
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isIp(final String value) {
		if (value == null || value.length() < 1) {
			return false;
		}
		String[] items = value.split("\\.");
		if (items.length != 4) {
			return false;
		}
		for (int i = 0; i < items.length; i++) {
			String s = items[i];
			try {
				int n = Integer.parseInt(s);
				if (n < 0 || n > 255) {
					return false;
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * isDateTime 
	 * date format: (yyyymmdd, yyyy-mm-dd, yyyy/mm/dd, yymmdd, yy-mm-dd, yy/mm/dd)
	 * time format: (mmddss, mm:dd:ss, mmdd, mm:dd)
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isDateTime(final String value) {
		if (value == null || value.length() < 1) {
			return false;
		}
		int a = value.indexOf(" ");
		if (a <= 0) {
			if (isDate(value) || isTime(value)) {
				return true;
			}
		}
		else {
			String d = value.substring(0, a);
			String t = value.substring(a + 1);
			if (isDate(d) && isTime(t)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * isDate. (yyyymmdd, yyyy-mm-dd, yyyy/mm/dd, yymmdd, yy-mm-dd, yy/mm/dd)
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isDate(final String value) {
		if (value == null) {
			return false;
		}
		String[] dateRegxList = {
			"[0-9]{4}[0-9]{2}[0-9]{2}",
			"[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}",
			"[0-9]{4}/[0-9]{1,2}/[0-9]{1,2}",
			
			"[0-9]{2}[0-9]{2}[0-9]{2}",
			"[0-9]{2}-[0-9]{1,2}-[0-9]{1,2}",
			"[0-9]{2}/[0-9]{1,2}/[0-9]{1,2}",
			
		};
		for (int i = 0; i < dateRegxList.length; i++) {
			String regx = dateRegxList[i];
			Pattern pattern = Pattern.compile(regx);
			Matcher matcher = pattern.matcher(value);
			if (matcher.matches()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * isTime. (mmddss, mm:dd:ss, mmdd, mm:dd)
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isTime(final String value) {
		if (value == null) {
			return false;
		}
		String[] dateRegxList = {
			"[0-9]{4}[0-9]{2}[0-9]{2}",
			"[0-9]{4}:[0-9]{1,2}:[0-9]{1,2}",
			
			"[0-9]{2}[0-9]{2}",
			"[0-9]{1,2}:[0-9]{1,2}",
		};
		for (int i = 0; i < dateRegxList.length; i++) {
			String regx = dateRegxList[i];
			Pattern pattern = Pattern.compile(regx);
			Matcher matcher = pattern.matcher(value);
			if (matcher.matches()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否是身份证
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isIdentity(final String value) {
		if (value == null) {
			return false;
		}
		if (value.length() == 15) {
			Pattern pattern = Pattern.compile("[0-9]+");
			Matcher matcher = pattern.matcher(value);
			if (!matcher.matches()) {
				return false;
			}
			String partA = value.substring(0, 6);
			String partB = value.substring(6, 12);
			String partC = value.substring(12);
			if (!isInteger(partA) || Integer.parseInt(partA) < 100000) {
				return false;
			}
			if (!isInteger(partB) || !isDate(partB)) {
				return false;
			}
			if (!isInteger(partC)) {
				return false;
			}
			return true;
		}
		else if (value.length() == 18) {
			String valueA = value.substring(0, 17);
			String valueB = value.substring(17);
			Pattern pattern = Pattern.compile("[0-9]+");
			Matcher matcher = pattern.matcher(valueA);
			if (!matcher.matches()) {
				return false;
			}
			String partA = valueA.substring(0, 6);
			String partB = valueA.substring(6, 14);
			String partC = valueA.substring(14);
			if (!isInteger(partA) || Integer.parseInt(partA) < 100000) {
				return false;
			}
			if (!isInteger(partB) || !isDate(partB)) {
				return false;
			}
			if (!isInteger(partC)) {
				return false;
			}
			
			//计算校验码
			String partD = null;
			try {
				partD = getIdentityVCode(valueA);
			} catch (Exception e) {
				return false;
			}
			
			return valueB.equals(partD);
		}
		return false;
	}
	
	/**
	 * 获取18位身份证号码最后一位校验码
	 * 
	 * @param value
	 * @return
	 */
	public static String getIdentityVCode(final String value) {
		if (value == null || value.length() < 17) {
			return null;
		}
		
		Pattern pattern = Pattern.compile("[0-9]+");
		Matcher matcher = pattern.matcher(value);
		if (!matcher.matches()) {
			return null;
		}
		
		int[] nums = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
		int m = 0;
		String s = value.substring(0, 17);
		for (int i = 0; i < s.length(); i++) {
			String c = s.substring(i, i + 1);
			int n = Integer.parseInt(c);
			int n2 = nums[i];
			m += n * n2;
		}
		m = m % 11;
		
		String[] codes = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
		String code = codes[m];
		return code;
	}

	/**
	 * 获取随机字符串
	 *
	 * @param length
	 * @return
	 */
	public static String randString(final int length) {
		final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		return randString(length, chars);
	}

	/**
	 * 获取随机字符串
	 *
	 * @param length
	 * @param chars
	 * @return
	 */
	public static String randString(final int length, final String chars) {
		int charsLen = chars.length() - 1;
		StringBuffer s = new StringBuffer(length);
		for (int i = 0; i < length; i++) {
			int n = rand(0, charsLen);
			s.append(chars.charAt(n));
		}
		return s.toString();
	}

	/**
	 * 把字符串数组用分隔符连接成一个字符串
	 * 
	 * @param items
	 * @param split
	 * @return
	 */
	public static String join(String[] items, String split) {
		if (items.length == 0) {
			return "";
		}
		StringBuffer s = new StringBuffer();
		int i;
		for (i = 0; i < items.length - 1; i++) {
			s.append(items[i]);
			s.append(split);
		}
		s.append(items[i]);
		return s.toString();
	}
	
	/**
	 * 获取随机数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int rand(int min, int max) {
		return (int)((max - min + 1) * Math.random() + min);
	}
	
	/**
	 * 获取随机数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static long rand(long min, long max) {
		return (long)((max - min + 1) * Math.random() + min);
	}
	
	
	
	/**
	 * 数据是否是pdf格式
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isPdf(final byte[] data) {
		//检查pdf数据格式
		int returnPosition = 0;
    	StringBuilder headerLineBuilder = new StringBuilder();
    	for (int i = 0; i < Math.min(data.length, 16); i++) {
    		if (data[i] == 10 || data[i] == 13) {
    			returnPosition = i;
    			break;
    		}
    		headerLineBuilder.append((char)(data[i]));
    	}
    	if (0 == returnPosition) {
    		return false;
    	}
    	String headerLine = headerLineBuilder.toString();
    	Pattern p1 = Pattern.compile("\\%PDF-[0-9]+?\\.[0-9]+?");
    	if (!p1.matcher(headerLine).matches()) {
    		return false;
    	}
    	return true;
	}
	
	/**
	 * 转换字符集到utf8
	 * 
	 * @param src
	 * @return
	 */
	public static String convertToUtf8(String src) {
	    if (src == null || src.length() == 0) {
            return src;
        }
	    if ("UTF-8".equalsIgnoreCase(Charset.defaultCharset().name())) {
	        return src;
	    }
	    
	    byte[] srcData = src.getBytes();
		try {
            return new String(srcData, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
	}
	
	/**
	 * 从utf8转换字符集
	 * 
	 * @param src
	 * @return
	 */
	public static String convertFromUtf8(String src) {
		if (src == null || src.length() == 0) {
			return src;
		}
		if ("UTF-8".equalsIgnoreCase(Charset.defaultCharset().name())) {
            return src;
        }
		byte[] srcData;
        try {
            srcData = src.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        };
        return new String(srcData);
	}


}
