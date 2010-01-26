import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Parser_dom {

	/**
	 * @param args
	 */
	//**************************************
	//*********Le main du programme*********
	//**************************************
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			System.out.println("début");
				Parser_dom parseur  = new Parser_dom();
			
			String filename = ".\\xmi.xml";
			
			parseur.parse(filename);
			System.out.println("fin");
			
			}
			catch (Exception e ){
				e.printStackTrace();
			}
	}
	
	
	
	//****************************************
	//**************Les variables*************
	//****************************************
	public static PrintWriter out = null;       //Le flux qui va permettre de genérer le fichier en sortie
	public static PrintWriter out2 = null;      //Le flux qui va permettre de générer les fichiers java
	private String nomClasse = new String();  //Contient le nom d'une classe
	private Hashtable<String, Vector<String>> CA = new Hashtable<String, Vector<String>>();  //Permet de stocker les clases associées à une autre
	private String[] tab_nom = {"./mapping1.hbm.xml", "./mapping2.hbm.xml", "./mapping3.hbm.xml"};
	private String[] tab_classe = {"./Oeuvre.java", "./Adherent.java", "./Exemplaire.java"};
	private static int cpt = 0;

	//*******************************************
	//**********On charge le fichier xml*********
	//***Rem changer Package par ModelElement****
	//*******************************************
	public void parse(String _fichier)
		throws SAXException, ParserConfigurationException, IOException {

		// Charger le document
		FileInputStream _xml_input_file = new FileInputStream(_fichier);

		parse(_xml_input_file);
	}
	
	
	//************************************************
	//******** On parcourt le fichier xml ************
	//************************************************
	public void parse(InputStream _xml_input_file) throws SAXException, ParserConfigurationException, IOException {

	//instancier le contrcuteur de parseurs
	DocumentBuilderFactory _factory = DocumentBuilderFactory.newInstance();

	//ignorer les commentaires dans les fichiers XML parser
	_factory.setIgnoringComments(true);
	
	// crée un parseur
	DocumentBuilder _builder = _factory.newDocumentBuilder();

	// Charger le document
	Document doc = _builder.parse(_xml_input_file);

	// Parser le document
	Element racine = doc.getDocumentElement();  //On récupère la racine
	
	//Récupère la liste des membres
	NodeList listemem = racine.getChildNodes();
	
	//Pour chaque élément fils de la racine
	for(int i=0; i<listemem.getLength(); i++)
	{
		Node NodeMem = listemem.item(i);
		//On vérifie que c'est un élément (ownedmember dans notre cas)
		if(NodeMem.getNodeType()==Node.ELEMENT_NODE)
		{
			//********************************
			//*****On cherche les classes*****
			Element c = (Element) NodeMem;
			if(c.hasAttribute("xmi:type") && c.getAttribute("xmi:type").compareTo("uml:Class")==0)
			{
				//Affichage entête
				template_entete();
				
				//On récupère le nom de la classe
				nomClasse = c.getAttribute("name"); 
				//Affichage classe
				template_classe();
				
				//On va récupérer les fils de cet élément (donc les attributs de cette classe en langage objet et ses associations)
				NodeList listeFilsClasse = NodeMem.getChildNodes();
				//Pour chaque fils de la classe (attributs de la classe)
				for(int j=0; j<listeFilsClasse.getLength(); j++)
				{
					Node NodeClassAttr = listeFilsClasse.item(j);
					//On vérifie que c'est un élément (ownedattribute dans notre cas)
					if(NodeClassAttr.getNodeType()==Node.ELEMENT_NODE)
					{
						//***********************************************
						//*****On cherche les attributs de la classe*****
						Element a = (Element) NodeClassAttr;
						if(!a.hasAttribute("association"))
						{
							//Affichage attribut
							template_attribute(a.getAttribute("name"));
							
							//Cas spécial : Exemplaire
							//Si c'est une classe qui est associée à une autre (ex : Exemplaire)
							if(CA.get(nomClasse)!=null)
							{
								for(int cpt=0; cpt<CA.get(nomClasse).size();cpt++)
								{
									//Affichage de ses associations
									template_associe(CA.get(nomClasse).elementAt(cpt));
								}
								template_end();
							}
						}
						//**************************************************
						//*****On cherche les associations de la classe*****
						else
						{
							//On récupère le nom de l'association grâce à la valeur de l'association et au ownedmembers
							String nom_assos = a.getAttribute("name");   //On récupère l'assos'
							String nom_class_assos = new String();
							//On cherche pour cela les associations (qui sont des owned members)
							for(int k=0; k<listemem.getLength(); k++)
							{
								Node NodeAssos = listemem.item(k);
								if(NodeAssos.getNodeType()==Node.ELEMENT_NODE)
								{
									Element as = (Element) NodeAssos; 
									/*//Nom de l'association
									if(as.getAttribute("xmi:type").compareTo("uml:Association")==0 && as.getAttribute("xmi:id").compareTo(a.getAttribute("association"))==0)  //Si c'est la bonne association
									{
										nom_assos = as.getAttribute("name"); 
									}*/
									//Nom de la classe associée
									if(as.getAttribute("xmi:type").compareTo("uml:Class")==0 && as.getAttribute("xmi:id").compareTo(a.getAttribute("type"))==0)  //Si c'est la bonne classe associée
									{
										nom_class_assos = as.getAttribute("name");
										//Si c'est une classe qui est associée à une autre (ex: Exemplaire)
										//On stocke les classes qui lui sont associées dans un hashtable
										if(CA.get(nom_class_assos)!=null)  
										{
											CA.get(nom_class_assos).add(nomClasse);
										}
										else
										{
											Vector<String> temp = new Vector<String>();
											temp.add(nomClasse);
											CA.put(nom_class_assos, temp);
										}
									}
								}
							}
							//Affichage des associations
							template_assos(nom_assos, nom_class_assos);
							template_end();
						}					
					}
				}
			}
		}
			
	}
	}
	
	//********************************************
	//********Affichage entête********************
	//********************************************
	public void template_entete()
	{
		//création du fichier output
		try {
			out = new PrintWriter(new FileOutputStream(tab_nom[cpt]));
			out2 = new PrintWriter(new FileOutputStream(tab_classe[cpt]));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//On crée l'entête
		out.println("<?xml version=\"1.0\"?>");
		out.println("<!DOCTYPE hibernate-mapping PUBLIC");
		out.println("\"-//Hibernate/Hibernate Mapping DTD 3.0//EN\"");
		out.println("\"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd\">");
		out.println(""); out.println("");
		System.out.println(cpt+1);
	}
	
	
	//********************************************
	//*********Affichage début + classe***********
	//********************************************
	public void template_classe()
	{
		out.println("");
		out.println("<hibernate-mapping package=\"com.efrei.content_provider\">");
		out.println("   <class name=\""+nomClasse+"\">");
		out.println("      <id name=\"id\" column=\""+nomClasse.toUpperCase()+"_ID"+"\">");
		out.println("         <generator class=\"native\"/>");
		out.println("      </id>");
		entete_classe();  //Création fichier java
	}
	
	
	//********************************************
	//***********Affichage des attributs**********
	//********************************************
	public void template_attribute(String attr)
	{
		out.println("      <property name=\""+attr+"\"/>");
		contenu_classe(attr, "String");   //Contenu fichier java
	}
	
	
	//********************************************
	//***********Affichage des associations*******
	//********************************************
	public void template_assos(String nom_assos, String nom_class_assos)
	{
		out.println("      <set name=\""+nom_assos+"\" inverse=\"true\" cascade=\"all\">");
		out.println("         <key column=\""+nomClasse.toUpperCase()+"_ID"+"\"/>");
		out.println("         <one-to-many class=\""+nom_class_assos+"\"/>");
		out.println("      </set>");
		cardinalite_classe(nom_assos,nom_class_assos);
	}	
	public void template_associe(String nom_classe)
	{
		out.println("      <many-to-one name=\""+nom_classe.toLowerCase()+"\" class=\""+nom_classe+"\" column=\""+nom_classe.toUpperCase()+"_ID\""+" not-null=\"true\"/>");
		classassos_association(nom_classe);
	}
	
	//*********************************************
	//********Affichage fin des classes************
	//*********************************************
	public void template_end()
	{
		out.println("   </class>");
		out.println("</hibernate-mapping>");
		out.println("");
		out2.println("}");
		out2.println("");
		//On ferme le flux
		out.close();out.flush();
		out2.close();out2.flush();
		cpt++;
	}
	
	//*********************************************
	//*******Affichage entete fichier java*********
	//*********************************************
	public void entete_classe()
	{
		out2.println("package com.efrei.content_provider;");
		out2.println("import java.io.Serializable;");
		out2.println("import java.util.Set;\n");
		out2.println("public class "+nomClasse+" implements Serializable{");
		out2.println("    private Long id;");
		out2.println("    public Long getId()");
		out2.println("    {   return id; }");
		out2.println("    public void setId(Long id)");
		out2.println("    {   this.id=id;   }");
		out2.println("");
	}
	
	//*********************************************
	//*******Affichage contenu fichier java*********
	//*********************************************
	public void contenu_classe(String attr, String type)
	{
		String attrtemp = attr.substring(0, 1).toUpperCase()+attr.substring(1);
		out2.println("    private "+type+" "+attr+";");
		out2.println("    public String get"+attrtemp+"()");
		out2.println("    {   return "+attr+";   }");
		out2.println("    public void set"+attrtemp+ "("+type+" "+attr+")");
		out2.println("    {   this."+attr+"="+attr+";   }");
		out2.println("");
	}
	
	public void cardinalite_classe(String nom_assos, String nom_class_assos)
	{
		String nom_assos_temp = nom_assos.substring(0,1).toUpperCase()+nom_assos.substring(1);
		out2.println("    private Set<"+nom_class_assos+">"+" "+nom_assos+";");
		out2.println("    public Set<"+nom_class_assos+">"+" get"+nom_assos_temp+"()");
		out2.println("    {   return "+nom_assos+";   }");
		out2.println("    public void set"+nom_class_assos+ "("+"Set<"+nom_class_assos+"> "+nom_assos+")");
		out2.println("    {   this."+nom_assos+"="+nom_assos+";   }");
		out2.println("");
	}
	
	public void classassos_association(String nom_classe)
	{
		String nom_classe_minuscule = nom_classe.substring(0,1).toLowerCase()+nom_classe.substring(1);
		out2.println("    private "+nom_classe+" "+nom_classe_minuscule+";");
		out2.println("    public get"+nom_classe+"()");
		out2.println("    {   return "+nom_classe_minuscule+";   }");
		out2.println("    public void set"+nom_classe+ "("+nom_classe+" "+nom_classe_minuscule+")");
		out2.println("    {   this."+nom_classe_minuscule+"="+nom_classe_minuscule+";   }");
		out2.println("");
	}
}





