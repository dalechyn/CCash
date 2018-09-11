package com.example.vladislav.ccash.Frontend;

public class Debtor
{
    private String DebtorName;

    private String DebtorUID;

    private Float DebtorDebt;

    public Debtor(String debtorName, String debtorUID, Float debtorDebt)
    {
        DebtorName = debtorName;
        DebtorUID = debtorUID;
        DebtorDebt = debtorDebt;
    }

    public String getDebtorName()
    {
        return DebtorName;
    }

    public void setDebtorName(String debtorName)
    {
        DebtorName = debtorName;
    }

    public String getDebtorUID()
    {
        return DebtorUID;
    }

    public void setDebtorUID(String debtorUID)
    {
        DebtorUID = debtorUID;
    }

    public Float getDebtorDebt()
    {
        return DebtorDebt;
    }

    public void setDebtorDebt(Float debtorDebt)
    {
        DebtorDebt = debtorDebt;
    }
}
