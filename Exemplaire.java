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

}

