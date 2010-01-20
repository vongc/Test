package com.efrei.content_provider;
import java.io.Serializable;
import java.util.Set;

public class Oeuvre implements Serializable{
    private Long id;
    public Long getId()
    {   return id; }
    public void setId(Long id)
    {   this.id=id;   }

    private String titre;
    public String getTitre()
    {   return titre;   }
    public void setTitre(String titre)
    {   this.titre=titre;   }

    private String auteur;
    public String getAuteur()
    {   return auteur;   }
    public void setAuteur(String auteur)
    {   this.auteur=auteur;   }

    private Set<Exemplaire> exemplaires;
    public Set<Exemplaire> getExemplaires()
    {   return exemplaires;   }
    public void setExemplaire(Set<Exemplaire> exemplaires)
    {   this.exemplaires=exemplaires;   }

}

