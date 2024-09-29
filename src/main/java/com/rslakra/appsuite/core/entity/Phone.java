package com.rslakra.appsuite.core.entity;

import com.rslakra.appsuite.core.exception.InvalidRequestException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Rohtash Lakra
 * @created 12/1/22 1:51 PM
 */
@Getter
@NoArgsConstructor
public class Phone {

    public static final String REGEX_AREA = "^[0-9]{3}$";
    public static final String REGEX_EXCHANGE = "^[0-9]{3}$";
    public static final String REGEX_EXTENSION = "^[0-9]{3}$";

    private String area;         // 3-digit
    private String exchange;     // 3-digit
    private String extension;    // 4-digit

    /**
     * Returns true if, and only if, a subsequence of the input sequence matches this matcher's pattern.
     *
     * @param regex
     * @param value
     * @return
     */
    public static boolean isMatches(String regex, String value) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }

    /**
     * @param area
     */
    public void setArea(String area) {
        if (!isMatches(REGEX_AREA, area)) {
            throw new InvalidRequestException("Expects a 3 - digit area number");
        }
        this.area = area;
    }

    /**
     * @param exchange
     */
    public void setExchange(String exchange) {
        if (!isMatches(REGEX_EXCHANGE, exchange)) {
            throw new InvalidRequestException("Expects a 3 - digit exchange number");
        }
        this.exchange = exchange;
    }

    /**
     * @param extension
     */
    public void setExtension(String extension) {
        if (!isMatches(REGEX_EXTENSION, extension)) {
            throw new InvalidRequestException("Expects a 4 - digit extension number");
        }
        this.extension = extension;
    }

    /**
     * @return
     */
    public String toPhone() {
        return String.format("(%s) %s-%s", getArea(), getExchange(), getExtension());
    }
}
