package ddc.util;

public class StrUtil {
	
	public static String[] splitByChunk(String s, int chunkSize){
	    int chunkCount = (s.length() / chunkSize) + (s.length() % chunkSize == 0 ? 0 : 1);
	    String[] returnVal = new String[chunkCount];
	    for(int i=0;i<chunkCount;i++){
	        returnVal[i] = s.substring(i*chunkSize, Math.min((i+1)*(chunkSize), s.length()));
	    }
	    return returnVal;
	}
}
