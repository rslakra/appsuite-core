package com.rslakra.appsuite.core.enums;

/**
 * The family of data structures (not symbologies) comprising GTIN include:
 *
 * <pre>
 *  GTIN-12 (UPC-A): this is a 12-digit number used primarily in North America
 *  GTIN-8 (EAN/UCC-8): this is an 8-digit number used predominately outside of North America
 *  GTIN-13 (EAN/UCC-13): this is a 13-digit number used predominately outside of North America
 *  GTIN-14 (EAN/UCC-14 or ITF-14): this is a 14-digit number used to identify trade items at various packaging levels
 * </pre>
 * <p>
 *
 * <pre>
 *  https://www.gtin.info/
 *  https://en.wikipedia.org/wiki/Global_Trade_Item_Number
 * </pre>
 *
 * @author Rohtash Lakra
 * @version 1.0.0
 * @since Dec 18, 2022 21:20:43
 */
public enum BarCodeType {
    JAN,
    EAN,
    /*  Global Trade Item Number (GTIN) */
    GTIN,
    /*  International Standard Book Number (ISBN) */
    ISBN,
    /*  International Standard Serial Number (ISSN) */
    ISSN,
    /* Universal Product Codes (UPCs) */
    UPC;
}
