package gr.aueb.cf.mobilecontacts.validation;

import gr.aueb.cf.mobilecontacts.dto.MobileContactInsertDTO;
import gr.aueb.cf.mobilecontacts.dto.MobileContactUpdateDTO;

public class ValidationUtil {

    /**
     * No instances of this class should be available
     */


    private  ValidationUtil(){

    }

    public static  String validateDTO(MobileContactInsertDTO insertDTO) {
    String errorResponce = "";

        if (insertDTO.getPhoneNumber().length() <= 5) errorResponce += "Phone number must be more than 5 numbers";
        if (insertDTO.getFirstname().length() < 2) errorResponce += " Firstname must be 2 or more characters";
        if (insertDTO.getLastname().length() < 2) errorResponce += " Lastname must be 2 or more characters";
        return errorResponce;

    }

    public static  String validateDTO(MobileContactUpdateDTO updateDTO) {
        String errorResponce = "";

        if (updateDTO.getPhoneNumber().length() <= 5) errorResponce += "Phone number must be more than 5 numbers";
        if (updateDTO.getFirstname().length() < 2) errorResponce += " Firstname must be 2 or more characters";
        if (updateDTO.getLastname().length() < 2) errorResponce += " Lastname must be 2 or more characters";
        return errorResponce;

    }
}
