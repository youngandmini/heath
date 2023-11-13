package heavysnow.heath.alias;

import java.text.ParseException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * mysql 전용 sql 문법을 h2 DB에서도 구동될 수 있도록 매핑하기위한 클래스
 */
public class H2DateFormatAlias {

    /**
     *mysql의 date_format 형태의 sql 문법을 h2 DB에서 구동될 수 있도록 매핑
     */
    public static String date_format(Date date, String mysqlFormatPattern) throws ParseException {
        if (date == null) return null;
        String dateFormatPattern = mysqlFormatPattern
                .replace("%Y", "yyyy")
                .replace("%m", "MM")
                .replace("%d", "dd");
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate().format(DateTimeFormatter.ofPattern(dateFormatPattern));
    }
}
