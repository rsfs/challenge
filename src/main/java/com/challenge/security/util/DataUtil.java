package com.challenge.security.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataUtil {

   public static final String PATERN_DDBMMYYYY = "dd/MM/YYYY";
		   
   public static final Locale LOCALE = new Locale("pt","BR");
   
   public static String formatDate(final Date date){
	   final SimpleDateFormat dateFormat = new SimpleDateFormat(PATERN_DDBMMYYYY, LOCALE);
	   return dateFormat.format(date);
   }
	
}
