package com.intrafab.cartomoneya;

/**
 * Created by Artemiy Terekhov on 06.05.2015.
 */
public class Constants {
    public static final String TAG = Constants.class.getName();

    public static final boolean RELEASE_MODE = false;

    public static final String EMAIL_PATTERN = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Za-z]{2,4}$";
    public static final String PASSWORD_PATTERN = "((?!\\s)\\A)(\\s|(?<!\\s)\\S){6,20}\\Z";

    public static class Prefs {
        public static final String PREF_PARAM_ME = "pref_param_me";
        public static final String PREF_PARAM_BUSINESS_CARDS = "pref_param_business_cards";
        public static final String PREF_PARAM_BUSINESS_CARDS_POPULATED = "pref_business_card_populated";
        public static final String PREF_PARAM_SHOPPING_CARDS = "pref_param_shopping_cards";
        public static final String PREF_PARAM_SHOP_BRANDS = "pref_param_shop_brands";
        public static final String PREF_PARAM_SHOPPING_LIST = "pref_param_shopping_list";
        public static final String PREF_PARAM_SHOP_OFFERS = "pref_param_shop_offers";
    }

    public static class Extras {
        public static final String PARAM_USER_DATA = "param_user_data";
        public static final String PARAM_INTERNET_AVAILABLE = "param_internet_available";
        public static final String PARAM_SHOP_CARD = "param_shop_card";
        public static final String PARAM_BUSINESS_CARD = "param_business_card";
        public static final String PARAM_TOKEN = "param_token";
    }
}
