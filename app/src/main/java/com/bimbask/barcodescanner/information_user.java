package com.bimbask.barcodescanner;

/**
 * Created by dverdugo on 29-06-16.
 */
public class information_user {

    static String unique_id="";
    static String name ="";
    static String email="";

    public static void set_information_user(String unique_id_,String name_, String email_){
        email=email_;
        name=name_;
        unique_id=unique_id_;
    }
    public static String get_email(){
        return email;
    }
    public static String get_name(){
        return name;
    }
    public  static String get_unique_id(){
        return unique_id;
    }

}
