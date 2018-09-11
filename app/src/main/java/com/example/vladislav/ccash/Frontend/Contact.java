package com.example.vladislav.ccash.Frontend;

import java.util.ArrayList;

public class Contact
{
    private String Name;
    private String UID;

    public Contact(String name, String UID)
    {
        Name = name;
        this.UID = UID;
    }

    public String getName()
    {
        return Name;
    }

    public void setName(String name)
    {
        Name = name;
    }

    public String getUID()
    {
        return UID;
    }

    public void setUID(String UID)
    {
        this.UID = UID;
    }

    public Contact returnContact()
    {
        return this;
    }

    public static String getUIDbyName(ArrayList<Contact> contacts, String Name)
    {
        for(Contact contact : contacts)
        {
            if(contact.getName().equals(Name))
                return contact.getUID();

        }
        return null;
    }
}
