package com.efrei.content_provider;
import java.io.Serializable;
import java.util.Set;

public class Exemplaire implements Serializable{
    private Long id;
    public Long getId()
    {   return id; }
    public void setId(Long id)
    {   this.id=id;   }

    private String numero;
    public String getNumero()
    {   return numero;   }
    public void setNumero(String numero)
    {   this.numero=numero;   }

    private Oeuvre oeuvre;
    public getOeuvre()
    {   return oeuvre;   }
    public void setOeuvre(Oeuvre oeuvre)
    {   this.oeuvre=oeuvre;   }

    private Adherent adherent;
    public getAdherent()
    {   return adherent;   }
    public void setAdherent(Adherent adherent)
    {   this.adherent=adherent;   }

}

