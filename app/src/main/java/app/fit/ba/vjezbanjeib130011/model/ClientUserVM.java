package app.fit.ba.vjezbanjeib130011.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by HOME on 18.10.2016.
 */

public class ClientUserVM implements Serializable
{
    public int ClientId;
    public String RegisterDate;
    
    public int UserId;
    public String FirstName;
    public String LastName;
    public String Username;
    public String Password;
    public String BirthDate;
    public String Address;
    public String Phone; 
    public boolean Active;
}
