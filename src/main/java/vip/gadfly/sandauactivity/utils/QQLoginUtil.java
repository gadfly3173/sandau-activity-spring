package vip.gadfly.sandauactivity.utils;

public class QQLoginUtil {
    public static String getQQLoginInfo(String type){
        String s = "";
        switch (type){
            case "client_id":
                s = "ci";
            case "client_secret":
                s = "cs";
            case "redirect_uri":
                s = "ru";
        }
        return s;
    }
}
