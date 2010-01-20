package com.efrei.content_provider;
import java.io.Serializable;
import java.util.Set;

public class Adherent implements Serializable{
    private Long id;
    public Long getId()
    {   return id; }
    public void setId(Long id)
    {   this.id=id;   }

    private String nom;
    public String getNom()
    {   return nom;   }
    public void setNom(String nom)
    {   this.nom=nom;   }

    private String adresse;
    public String getAdresse()
    {   return adresse;   }
    public void setAdresse(String adresse)
    {   this.adresse=adresse;   }

    private Set<Exemplaire> copies;
    public Set<Exemplaire> getCopies()
    {   return copies;   }
    public void setExemplaire(Set<Exemplaire> copies)
    {   this.copies=copies;   }

}

