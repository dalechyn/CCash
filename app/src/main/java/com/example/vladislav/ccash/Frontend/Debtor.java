package com.example.vladislav.ccash.Frontend;


public class Debtor extends Contact
{
    private double DebtorDebt;

    public Debtor(String debtorName, String debtorUID, double debtorDebt)
    {
        super(debtorName, debtorUID);
        super.setName(debtorName);
        super.setName(debtorUID);
        DebtorDebt = debtorDebt;
    }

    public String getDebtorName()
    {
        return super.getName();
    }

    public Contact getContact()
    {
        return super.returnContact();
    }

    public void setDebtorName(String debtorName)
    {
        super.setName(debtorName);
    }

    public String getDebtorUID()
    {
        return super.getUID();
    }

    public void setDebtorUID(String debtorUID)
    {
        super.setUID(debtorUID);
    }

    public double getDebtorDebt()
    {
        return DebtorDebt;
    }

    public void setDebtorDebt(double debtorDebt)
    {
        DebtorDebt = debtorDebt;
    }
}
