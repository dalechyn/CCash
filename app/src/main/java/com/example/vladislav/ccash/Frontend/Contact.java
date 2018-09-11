package com.example.vladislav.ccash.Frontend;

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
}
