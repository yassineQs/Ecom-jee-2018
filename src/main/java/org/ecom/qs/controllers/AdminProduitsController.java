package org.ecom.qs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.ecom.qs.entities.Produit;
import org.ecom.qs.metier.IAdminProduitsMetier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value="/adminProd")
public class AdminProduitsController {
	@Autowired
	private IAdminProduitsMetier metier;
	@RequestMapping(value="/index")
	public String index(Model model) {
		model.addAttribute("produit", new Produit());
		model.addAttribute("produits",metier.listproduits());
		model.addAttribute("categories",metier.listCategories());
		return "produits";
	}
	/*
	@RequestMapping(value="/saveProd")
	public String saveProd(@Valid Produit p,BindingResult bindingResult,
		Model model,MultipartFile file) throws Exception{
		if(bindingResult.hasErrors()){
		model.addAttribute("categories", metier.listCategories());
		model.addAttribute("produits", metier.listproduits());
		return "produits";
		}
		
		if(!file.isEmpty()){
			String path=System.getProperty("java.io.tmpdir");
			p.setPhoto(file.getOriginalFilename());
			Long idP=null;
			if(p.getIdProduit()==null) {
				idP = metier.ajouterProduit(p, p.getCategorie().getIdCategorie());
			}
			else {
				metier.modifierProduit(p);
			}
			file.transferTo(new File(path+"/"+"PROD_"+idP+"_"+file.getOriginalFilename()));
			
		}else {
			metier.ajouterProduit(p, p.getCategorie().getIdCategorie());
		}
		model.addAttribute("produit", new Produit());
		model.addAttribute("produits", metier.listproduits());
		model.addAttribute("categories", metier.listCategories());
		return "produits";
	}*/
	@RequestMapping(value="/saveProd")
	public String enregistrer(@Valid Produit p, BindingResult bindingResult,
			MultipartFile file,
			Model model) throws Exception{
			if(bindingResult.hasErrors()) {
			model.addAttribute("produits", metier.listproduits());
			return "produits" ; 
			}
			if(!file.isEmpty()){ p.setPhoto(file.getOriginalFilename()); }
			if(p.getIdProduit()==null){
			metier.ajouterProduit(p, p.getCategorie().getIdCategorie());
			} else{ metier.modifierProduit(p); }
			if(!file.isEmpty()){
			String path=System.getProperty("java.io.tmpdir");
			file.transferTo(new File(path+"/"+"PROD_"+p.getIdProduit()+"_"+file.getOriginalFilename()));
			}
			model.addAttribute("produit", new Produit());
			model.addAttribute("produits", metier.listproduits());
			model.addAttribute("produit", new Produit());
			model.addAttribute("produits", metier.listproduits());
			model.addAttribute("categories", metier.listCategories());
			return "produits";
			}
	
	@RequestMapping(value="/photoProd",produces=MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public byte[] getPhoto(Long idProd) throws IOException{
	Produit p=metier.getProduit(idProd);
	File f = new File(System.getProperty("java.io.tmpdir")+"/PROD_"+idProd+"_"+p.getPhoto());
		return IOUtils.toByteArray(new FileInputStream(f));
	}
	
	@RequestMapping(value="/editProd")
	public String editProduit(Long idProd,Model model){
		Produit p=metier.getProduit(idProd);
		model.addAttribute("produit",p);
		model.addAttribute("produits", metier.listproduits());
		model.addAttribute("categories", metier.listCategories());
		return "produits";
	}
	
	
	@RequestMapping(value="/suppProd")
	public String suppProd(Long idProd,Model model){
		metier.supprimerProduit(idProd);
		model.addAttribute("produits", metier.listproduits());
		model.addAttribute("produit",new Produit());
		return "produits";
	}
	
	
}
