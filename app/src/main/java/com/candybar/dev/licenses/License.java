package com.candybar.dev.licenses;

import com.candybar.dev.BuildConfig;

import candybar.lib.items.InAppBilling;

public class License {

    /*
     * License Checker
     * private static final boolean ENABLE_LICENSE_CHECKER = true; --> enabled
     * Change to private static final boolean ENABLE_LICENSE_CHECKER = false; if you want to disable it
     *
     * NOTE: If you disable license checker you need to remove LICENSE_CHECK permission inside AndroidManifest.xml
     */

    /*
     * NOTE: If license checker is disabled (above), just ignore this
     *
     * Generate 20 random bytes
     * For easy way, go to https://www.random.org/strings/
     * Set generate 20 random strings
     * Each string should be 2 character long
     * Check numeric digit (0-9)
     * Choose each string should be unique
     * Get string
     */
    private static final byte[] SALT = new byte[]{
            // Put generated random bytes below, separate with comma, ex: 14, 23, 58, 85, ...
            // 1, 4, 81, 99, 95, 66, 64, 85, 51, 17, 39, 86, 61, 79, 19, 82, 74, 33, 47, 25,
    };

    /*
     * Your license key
     * If your app hasn't published at play store, publish it first as beta, get license key
     */

    /*
     * NOTE: Make sure your app name in project same as app name at play store listing
     * NOTE: Your InApp Purchase will works only after the apk published
     */

    /*
     * NOTE: If premium request disabled, just ignored this
     *
     * InApp product id for premium request
     * Product name displayed the same as product name displayed at play store
     * So make sure to name it properly, like include number of icons
     * Format: new InAppBilling("premium request product id", number of icons)
     */

    /*
     * NOTE: If donation disabled, just ignored this
     *
     * InApp product id for donation
     * Product name displayed the same as product name displayed at play store
     * So make sure to name it properly
     * Format: new InAppBilling("donation product id")
     */

    public static byte[] getRandomString() {
        return SALT;
    }



}
