package gr.aueb.cf.mobilecontacts.service;

import gr.aueb.cf.mobilecontacts.dao.IMobileContactDAO;
import gr.aueb.cf.mobilecontacts.dto.MobileContactInsertDTO;
import gr.aueb.cf.mobilecontacts.dto.MobileContactUpdateDTO;
import gr.aueb.cf.mobilecontacts.exceptions.ContactNotFoundException;
import gr.aueb.cf.mobilecontacts.exceptions.PhoneNumberAlreadyExistsException;
import gr.aueb.cf.mobilecontacts.model.MobileContact;

import java.util.List;

public class MobileContactServiceImpl implements IMobileContactService{

    private final IMobileContactDAO dao;


    public MobileContactServiceImpl(IMobileContactDAO dao) {
        this.dao = dao;
    }


    @Override
    public MobileContact insertMobileContact(MobileContactInsertDTO dto) throws PhoneNumberAlreadyExistsException {
        MobileContact mobileContact;

        try {
            if (dao.phoneNumberExists(dto.getPhoneNumber())) {
                throw new PhoneNumberAlreadyExistsException("Contact with phone number" + dto.getPhoneNumber() + "already exists");
            }
            mobileContact = mapInsertDTOToContact(dto);

            System.err.printf("MobileContactServiceImpl Logger: %s was insert", mobileContact);
            return dao.insert(mobileContact);

        } catch (PhoneNumberAlreadyExistsException e) {
            System.err.printf("MobileContactServiceImpl Logger: contact with phone number: %s\n already exists", dto.getPhoneNumber());
            throw  e;
        }
    }

    @Override
    public MobileContact updateMobileContact(MobileContactUpdateDTO dto) throws PhoneNumberAlreadyExistsException, ContactNotFoundException {
        MobileContact mobileContact;
        MobileContact newContact;

        try {
            if (dao.userIdExists(dto.getId())){
                throw new ContactNotFoundException("Contact with id : " + dto.getId() + " not found");
            }

            mobileContact = dao.getById(dto.getId());
            boolean isPhoneNumberOurOwn = mobileContact.getPhoneNumber().equals(dto.getPhoneNumber());
            boolean isPhoneNumberExists = dao.phoneNumberExists(dto.getPhoneNumber());

            if (isPhoneNumberExists && !isPhoneNumberOurOwn) {
                throw new PhoneNumberAlreadyExistsException("Contact with phone number: " + dto.getPhoneNumber() + " Already exists");
            }

            newContact = mapUpdateDTOToContact(dto);
            System.err.printf("MobileContactServiceImpl Logger: %s\n", mobileContact, newContact);
            return dao.update(dto.getId(), newContact);
        } catch (ContactNotFoundException | PhoneNumberAlreadyExistsException e) {
            System.err.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteContactById(Long id) throws ContactNotFoundException {
    try{
        if(!dao.userIdExists(id)) {
            throw new ContactNotFoundException("Contact with id" + id + "not found for delete");
        }
        System.err.printf("MobileContactServiceImpl Logger: contact with id : " + id +"was deleted");
        dao.deleteById(id);

        }catch (ContactNotFoundException e ) {
        System.err.printf("MobileContactServiceImpl Logger : " + e.getMessage());
        throw e;
    }
    }

    @Override
    public MobileContact getContactById(Long id) throws ContactNotFoundException {
        MobileContact mobileContact;
        try {
            mobileContact = dao.getById(id);
            if (mobileContact == null) {
                throw new ContactNotFoundException("Contact with id: "  + id + "not found");
            }
            return mobileContact;
        } catch (ContactNotFoundException e) {
            System.err.printf(" Contact with id" + id  +" was not found to get returned");
            throw e;
        }
    }

    @Override
    public List<MobileContact> getAllContacts() {
        return dao.getAll();
    }

    @Override
    public MobileContact getContactByPhoneNumber(String phoneNumber) throws ContactNotFoundException {
       MobileContact mobileContact;
            try {
                mobileContact = dao.getByPhoneNumber(phoneNumber);
                if (mobileContact == null) {
                    throw new ContactNotFoundException("Contact with id: "  + phoneNumber + "not found");
                }
                return mobileContact;
            } catch (ContactNotFoundException e) {
                System.err.printf(" Contact with id" + phoneNumber  +" was not found to get returned");
                throw e;
            }

    }

    @Override
    public void deleteContactByPhoneNumber(String phoneNumber) throws ContactNotFoundException {
        try{
            if(!dao.phoneNumberExists(phoneNumber)) {
                throw new ContactNotFoundException("Contact with id" + phoneNumber + "not found for delete");
            }
            System.err.printf("MobileContactServiceImpl Logger: contact with id : " + phoneNumber +"was deleted");
            dao.deleteByPhoneNumber(phoneNumber);

        }catch (ContactNotFoundException e ) {
            System.err.printf("MobileContactServiceImpl Logger : %s\n " + e.getMessage());
            throw e;
        }
    }

    private MobileContact mapInsertDTOToContact(MobileContactInsertDTO dto) {
        return new MobileContact(null, dto.getFirstname(), dto.getLastname(), dto.getPhoneNumber());
    }

    private MobileContact mapUpdateDTOToContact(MobileContactUpdateDTO dto) {
        return new MobileContact(dto.getId(), dto.getFirstname(), dto.getLastname(), dto.getPhoneNumber());
    }
}
